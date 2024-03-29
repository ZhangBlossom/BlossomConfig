package blossom.project.config.client;


import blossom.project.config.common.constants.BlossomConstants;
import blossom.project.config.common.entity.Result;
import blossom.project.config.common.enums.EventTypeEnum;
import blossom.project.config.common.exception.BlossomException;
import blossom.project.config.common.utils.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static blossom.project.config.common.constants.BlossomConfigPropertiesKeyConstants.*;
import static blossom.project.config.common.constants.BlossomConstants.SEPARATOR;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 18:51
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigService类
 * 项目配置中心客户端服务
 * 用于提供基础的向配置中心发送请求的方法
 * 是真正的API提供者
 */
@Slf4j
public class BlossomConfigService implements ConfigService {

    //设定namespace 之后的当前项目的查询都基于此123
    private String namespace;
    private final Properties properties;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ConcurrentHashMap<String, ConfigCache> cacheMap = new ConcurrentHashMap<>();

    //可以考虑用AtomicReference
    //private final ConcurrentHashMap<String, AtomicReference<ConfigCache>> cacheMap = new ConcurrentHashMap<>();


    public BlossomConfigService(Properties properties) {
        this.properties = properties;
        initNamespace();
    }

    private void initNamespace() {
        this.namespace = (String) this.properties.get(NAMESPACE);
    }


    @Override
    public boolean publishConfig(String configId, String group, String content) throws BlossomException {
        String url = buildPublishUrl(configId, group); // 构建URL
        try {
            return HttpUtils.sendPostRequest(url, content);
        } catch (Exception e) {
            throw new BlossomException(BlossomException.PUBLISH_CONFIG_ERROR, "Failed to publish config", e);
        }
    }

    @Override
    public boolean removeConfig(String configId, String group) throws BlossomException {
        String url = buildPublishUrl(configId, group); // 构建URL
        try {
            return HttpUtils.sendDeleteRequest(url);
        } catch (Exception e) {
            throw new BlossomException(BlossomException.REMOVE_CONFIG_ERROR, "Failed to remove config", e);
        }
    }

    /**
     * 查询配置
     *
     * @param configId
     * @param group
     * @param fileExtension
     * @return
     * @throws BlossomException
     */
    @Override
    public String getConfig(String configId, String group, String fileExtension) throws BlossomException {
        final String key = namespace + SEPARATOR + group + SEPARATOR + configId;

        // 尝试从本地文件读取配置
        String content = ReadWriteLockUtil.withReadLock(key, () -> {
            try {
                return FileUtil.readConfigFromFile(key);
            } catch (IOException e) {
                log.error("read local config file erroe...,Exception:{}",e);
                throw new RuntimeException(e);
            }
        });

        // 如果本地没有配置或读取失败，则从配置中心获取
        if (StringUtils.isBlank(content)) {
            String url = buildGetConfigUrl(group, configId); // 构建URL
            try {
                content = HttpUtils.sendGetRequest(url);
                // 更新缓存
                ConfigCache cache = ConfigCache.builder()
                        .content(content)
                        .key(key)
                        .lastCallMd5(MD5Util.toMD5(content))
                        .type(fileExtension)
                        .modifyTimestamp(new Date())
                        .build();
                cacheMap.put(key, cache);

                // 将配置信息写入到本地文件
                String finalContent = content;
                ReadWriteLockUtil.withWriteLock(key, id -> {
                    try {
                        FileUtil.writeConfigToFile(key, finalContent);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to write config to file for key: " + key, e);
                    }
                });
            } catch (Exception e) {
                throw new BlossomException(BlossomException.GET_CONFIG_ERROR, "Failed to get config", e);
            }
        }

        return content;
    }


    /**
     * 订阅监听配置中心的某一个配置文件
     * 并且在这里，需要有一个机制，通过比对MD5之后
     * 如果发现配置文件变更，还需要可以发布一个事件来更新项目的Environment
     *
     * @param group
     * @param configId
     */
    @Override
    public void subscribeConfigChangeEvent(String group, String configId, Publish publish) {
        String url = buildSubScribeUrl(group, configId);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    System.out.println("Received content: " + content);
                    // 处理收到的内容
                    Result result = JSON.parseObject(content, Result.class);
                    String data = (String) result.getData();
                    String key = namespace + SEPARATOR + group + SEPARATOR + configId;
                    String eventType = result.getExt();
                    //当前是一个配置删除事件
                    if (StringUtils.equals(EventTypeEnum.REMOVE.getValue(), eventType)) {
                        handleRemoveEvnet(key, publish);
                    } else if (StringUtils.equals(EventTypeEnum.PUBLISH.getValue(), eventType)) {
                        //TODO 比较缓存中的MD5 如果改变
                        //配置发生改变
                        //就需要发生一个变更事件 通知项目修改Environment的值
                        //并且需要对@Value的值进行刷新
                        //TODO 这里需要考虑的是 Client模块是可以不整合SpringBoot的
                        //那么就意味着不能直接使用ApplicationListener/Event了
                        String newMd5 = MD5Util.toMD5(data);
                        ConfigCache configCache = cacheMap.get(key);
                        String lastCallMd5 = configCache != null ? configCache.getLastCallMd5() : null;
                        if (!StringUtils.equals(lastCallMd5, newMd5)) {
                            ConfigCache updatedConfigCache =
                                    ConfigCache
                                            .builder()
                                            .content(data)
                                            .key(key)
                                            .lastCallMd5(newMd5)
                                            .modifyTimestamp(new Date())
                                            .build();
                            handlePublishEvent(key, updatedConfigCache, publish);
                        }
                    }
                    //这里可能监听到超时了但是还是没有发生变更事件
                    // 最后依旧需要立即再次发起长轮询请求
                    subscribeConfigChangeEvent(group, configId, publish);
                }).exceptionally(e -> {
                    e.printStackTrace();
                    // 立即再次发起长轮询请求
                    subscribeConfigChangeEvent(group, configId, publish);
                    return null;
                });
        System.out.println("finish a longpolling and send a longpolling again...");
    }


    /**
     * 处理配置变更事件
     *
     * @param key
     * @param configCache
     * @param publish
     */
    private void handlePublishEvent(String key, ConfigCache configCache, Publish publish) {
        try {
            // 将新的配置写入到本地文件
            // 同步更新本地文件
            ReadWriteLockUtil.withWriteLock(key, id -> {
                // 具体的文件写操作
                try {
                    FileUtil.writeConfigToFile(key, configCache.getContent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // 更新内存中的缓存
            cacheMap.put(key, configCache);
            //发布一个配置变更事件
            //刷新Spring容器上下文
            publish.publishPublishEvent(key, configCache);
        } catch (Exception e) {
            log.error("Error writing config to file: {}", key, e);
        }
    }


    // 如下两个方法必须实现基于自己手写的监听器的方式来同时Core模块完成配置变更

    private void handleRemoveEvnet(String key, Publish publish) {
        cacheMap.remove(key);
        //发布一个配置变更事件
        //刷新Spring容器上下文
        publish.publishRemoveEvent(key);
    }


    @Override
    public Properties getProperties() {
        return this.properties;
    }

    private String buildSubScribeUrl(String group, String configId) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://").append(this.properties.getProperty("server-addr")).append(BlossomConstants.CONFIG_CONTROLLER_PATH).append("/subscribe").append("?namespace=").append(namespace).append("&group=").append(group).append("&configId=").append(configId);
        return sb.toString();
    }


    private String buildGetConfigUrl(String group, String configId) {
        // 构建用于获取配置的URL
        // 例如: return "http://" + serverAddress + "/config?configId=" + configId + "&group=" + group;
        StringBuffer sb = new StringBuffer();
        sb.append("http://").append(this.properties.getProperty("server-addr")).append(BlossomConstants.CONFIG_CONTROLLER_PATH).append("?namespace=").append(namespace).append("&group=").append(group).append("&configId=").append(configId);
        return sb.toString();
    }

    private String buildPublishUrl(String configId, String group) {
        // 构建用于发布配置的URL
        // 例如: return "http://" + serverAddress + "/config/publish?configId=" + configId + "&group=" + group;
        return null;
    }
}

package blossom.project.config.core;

import blossom.project.config.common.constants.BlossomConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static blossom.project.config.common.constants.BlossomConstants.DEFAULT_EXTENSION;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/29 21:20
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigDataHandler类
 * 当前类的作用是用来解析处理从配置中心拉取的配置
 * 特别需要注意如下几点的使用
 * 1：由于从配置中心加载配置的时机在spring上下文容器创建之前
 * 所以这里不能用@Autowired 很多数据必须依靠Spring原生的能力进行获取
 * 2：使用单例模式和工厂模式
 * 2.1：单例可以用枚举/常规单例/内部类单例
 */
@Slf4j

public class BlossomConfigDataHandler {

    //如果不存在文件扩展名称 默认使用yaml作为文件扩展名称
    private static List<PropertySourceLoader> propertySourceLoaders;

    private BlossomConfigDataHandler(){
        //使用spring原生能力加载
        propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class,
                this.getClass().getClassLoader());
    }


    /**
     * 解析配置信息并且返回PropertySource集合
     * @param configId
     * @param configData
     * @param fileExtension
     * @return
     * @throws IOException
     */
    public List<PropertySource<?>> parseConfigData(String configId,String configData,String fileExtension) throws IOException {
        //configId 其实就是配置文件的完整文件名称
        if (StringUtils.isBlank(fileExtension)){
            fileExtension = getFileExtension(configId);
        }
        //遍历所有当前项目可以对配置文件进行解析的解析器
        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders) {
            //判断是否可以解析当前格式
            if (!canParseFileExtension(propertySourceLoader, fileExtension)) {
                continue;
            }
            //spring提供基于ByteArrayResource将配置转换为PropertySource的解析器
            ByteArrayResource resource = new ByteArrayResource(configData.getBytes(),configId);
            //解析器会返回OriginTrackedMapPropertySource的集合
            List<PropertySource<?>> propertySourceList = propertySourceLoader
                    .load(configId, resource);
            if (CollectionUtils.isEmpty(propertySourceList)) {
                return Collections.emptyList();
            }
            return propertySourceList.stream().filter(Objects::nonNull)
                    .map(propertySource -> {
                        if (propertySource instanceof EnumerablePropertySource) {
                            String[] propertyNames = ((EnumerablePropertySource) propertySource)
                                    .getPropertyNames();
                            if (propertyNames != null && propertyNames.length > 0) {
                                Map<String, Object> map = new LinkedHashMap<>();
                                Arrays.stream(propertyNames).forEach(name -> {
                                    map.put(name, propertySource.getProperty(name));
                                });
                                return new OriginTrackedMapPropertySource(
                                        propertySource.getName(), map, true);
                            }
                        }
                        return propertySource;
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public String getFileExtension(String name) {
        if (!org.springframework.util.StringUtils.hasLength(name)) {
            return DEFAULT_EXTENSION;
        }
        int idx = name.lastIndexOf(BlossomConstants.DOT);
        if (idx > 0 && idx < name.length() - 1) {
            return name.substring(idx + 1);
        }
        return DEFAULT_EXTENSION;
    }

    /**
     * 判断当前解析器是否可以解析当前文件类型
     * @param loader
     * @param extension
     * @return
     */
    private boolean canParseFileExtension(PropertySourceLoader loader, String extension) {
        return Arrays.stream(loader.getFileExtensions())
                .anyMatch((fileExtension) -> StringUtils.endsWithIgnoreCase(extension,
                        fileExtension));
    }

    public static BlossomConfigDataHandler getInstance() {
        return BlossomConfigDataHandler.ParserHandler.HANDLER;
    }

    //使用静态内部类实现单例
    private static class ParserHandler {

        private static final BlossomConfigDataHandler HANDLER = new BlossomConfigDataHandler();

    }

}

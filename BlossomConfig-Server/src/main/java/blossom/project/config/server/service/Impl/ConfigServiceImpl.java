package blossom.project.config.server.service.Impl;

import blossom.project.config.common.constants.BlossomConstants;
import blossom.project.config.common.enums.EventTypeEnum;
import blossom.project.config.server.entity.Config;
import blossom.project.config.server.mapper.ConfigMapper;
import blossom.project.config.server.service.ConfigService;
import blossom.project.config.server.service.LongPollingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * @author: ZhangBlossom
 * @date: 2023-12-28 23:01:27
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * Config类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    private final ConfigMapper configMapper;

    private final LongPollingService longPollingService;

    /**
     * 获取某个文件的配置
     * @param configId
     * @param namespace
     * @param group
     * @param type
     * @return
     */
    @Override
    public String getConfig(String configId, String namespace, String group, String type) {
        if (StringUtils.isBlank(type)){
            type = BlossomConstants.DEFAULT_EXTENSION;
        }
        Config config = configMapper.getConfig(configId, namespace, group, type);
        if (Objects.isNull(config)){
            return "";
        }
        return config.getContent();
    }


    /**
     * 发布配置
     * @param config
     */
    @Override
    public void publishConfig(Config config) {
        if (checkParams(config)) {
            log.error("the param of config is valid...");
            return;
        }
        String configId = config.getConfigId();
        String namespace = config.getNamespace();
        String group = config.getGroup();
        String type = config.getType();
        String content = config.getContent();
        //先检查之前是否已经存在config
        Config preConfig = configMapper.getConfig(configId,namespace,group,type);
        //不存在就创建
        if (Objects.isNull(preConfig)) {
            configMapper.publishConfig(config);
        }else{
            //否则就是修改
            configMapper.editConfig(config);
        }
        //配置发布之后同时提示对应的监听请求
        longPollingService.notifySubscriber(EventTypeEnum.PUBLISH,namespace,group,configId,content);
    }


    @Override
    public void removeConfig(String configId, String namespace, String group) {
        configMapper.removeConfig(configId, namespace, group);
        longPollingService.notifySubscriber(EventTypeEnum.REMOVE,namespace,group,configId,null);
    }

    /**
     * 检查参数 这里写的比较随意
     * 就不用@Validated这些注解了
     *
     * @param config
     * @return
     */
    private boolean checkParams(Config config) {
        return (StringUtils.isBlank(config.getConfigId())
                || StringUtils.isBlank(config.getGroup())
                || StringUtils.isBlank(config.getNamespace())
                || StringUtils.isBlank(config.getConfigId()));
    }

    @Override
    public boolean removeConfig(Long id) {
        return configMapper.deleteById(id) > 0;
    }

    @Override
    public boolean removeConfigs(List<Long> ids) {
        return configMapper.deleteBatchIds(ids) > 0;
    }

}


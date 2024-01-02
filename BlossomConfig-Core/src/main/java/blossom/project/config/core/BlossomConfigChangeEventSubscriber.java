package blossom.project.config.core;

import blossom.project.config.client.ConfigService;
import blossom.project.config.client.Publish;
import blossom.project.config.common.constants.BlossomConfigPropertiesKeyConstants;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 19:52
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 *  BlossomConfigChangeEventSubscriber类
 *  当前类用于基于ConfigService来监听配置文件的各种事件
 *
 */
@Slf4j
public class BlossomConfigChangeEventSubscriber {

    private ConfigService configService;

    private Properties properties;

    private Publish publish;

    public BlossomConfigChangeEventSubscriber(ConfigService configService, BlossomConfigChangePublisher publisher) {
        this.configService = configService;
        this.properties = configService.getProperties();
        this.publish = publisher;
    }

    @PostConstruct
    public void listen() {
        //得到当前项目所有生效的配置
        List<BlossomConfigProperties.BlossomConfig> configLists =
                (List<BlossomConfigProperties.BlossomConfig>) this.properties.get(BlossomConfigPropertiesKeyConstants.CONFIG_LISTS);
        if (configLists.isEmpty()) {
            return;
        }
        //对这些配置进行遍历，为他们添加监听器
        //使得这些配置发生变更之后我能监听到对应的事件 从而对这些事件进行处理
        configLists.stream().forEach(config -> {
            this.configService.subscribeConfigChangeEvent(config.getGroup(),config.getConfigId(),this.publish);
        });
    }


}

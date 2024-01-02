package blossom.project.config.core;

import blossom.project.config.client.ConfigCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 22:49
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */
public class BlossomConfigChangePublisher extends AbstractConfigChangePublish {

    private final ApplicationEventPublisher applicationEventPublisher;

    public BlossomConfigChangePublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishRemoveEvent(String key) {
        BlossomConfigChangeEvent event = new BlossomConfigChangeEvent(this, key);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishPublishEvent(String key, ConfigCache configCache) {
        BlossomConfigChangeEvent event = new BlossomConfigChangeEvent(this, key, configCache);
        applicationEventPublisher.publishEvent(event);
    }
}

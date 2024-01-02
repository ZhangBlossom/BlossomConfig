package blossom.project.config.core;

import blossom.project.config.client.ConfigCache;
import blossom.project.config.client.Publish;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 22:49
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * AbstractConfigChangePublish 先制定一个抽象类接口把 后续有可能会扩展
 */
public abstract class AbstractConfigChangePublish implements Publish {

    @Override
    public abstract void publishRemoveEvent(String key);

    @Override
    public abstract void publishPublishEvent(String key, ConfigCache configCache);
}

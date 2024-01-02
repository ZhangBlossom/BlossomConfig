package blossom.project.config.client;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 22:47
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * Listener类
 */
public interface Publish {


    void publishRemoveEvent(String key);

    void publishPublishEvent(String key, ConfigCache configCache);
}

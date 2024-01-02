package blossom.project.config.core;

import blossom.project.config.client.ConfigCache;
import org.springframework.context.ApplicationEvent;


/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 22:49
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */

public class BlossomConfigChangeEvent extends ApplicationEvent {

    private final String key;
    private final ConfigCache configCache;

    // 构造函数用于删除事件
    public BlossomConfigChangeEvent(Object source, String key) {
        super(source);
        this.key = key;
        this.configCache = null;
    }

    // 构造函数用于发布事件
    public BlossomConfigChangeEvent(Object source, String key, ConfigCache configCache) {
        super(source);
        this.key = key;
        this.configCache = configCache;
    }

    public String getKey() {
        return key;
    }

    public ConfigCache getConfigCache() {
        return configCache;
    }
}

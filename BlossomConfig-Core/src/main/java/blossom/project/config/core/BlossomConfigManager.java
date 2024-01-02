package blossom.project.config.core;

import blossom.project.config.client.BlossomConfigFactory;
import blossom.project.config.client.ConfigService;
import blossom.project.config.common.exception.BlossomException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 17:33
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigManager类
 */
@Data
@Builder
@Slf4j
public class BlossomConfigManager {

    private BlossomConfigProperties properties;
    
    private static ConfigService configService;

    public BlossomConfigManager(BlossomConfigProperties properties){
        this.properties = properties;
        createBlossomConfig(properties);
    }

    private static ConfigService createBlossomConfig(BlossomConfigProperties properties) {
        if (Objects.isNull(configService)) {
            //双检索保证单例
            synchronized (BlossomConfigManager.class) {
                try {
                    if (Objects.isNull(configService)) {
                        configService = BlossomConfigFactory.createConfigService(
                                properties.parseProperties());
                    }
                }
                catch (BlossomException e) {
                    log.error(e.getMessage());
                    throw new BlossomException(BlossomException.CONNECTION_FAILED, e.getMessage(), e);
                }
            }
        }
        return configService;
        
    }


    /**
     * get the config service
     * @return
     */
    public ConfigService getConfigService(){
        if (Objects.isNull(configService)) {
            createBlossomConfig(properties);
        }
        return configService;
    }


}

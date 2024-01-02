package blossom.project.config.client;

import blossom.project.config.common.exception.BlossomException;


import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 18:41
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigFactory类
 */
public class BlossomConfigFactory {

    /**
     * 用于创建ConfigService配置中心
     * @param properties 配置中心的创建需要用到配置文件
     * @return
     * @throws BlossomException
     */
    public static ConfigService createConfigService(Properties properties) throws BlossomException {
        try {
            Class<?> configServiceClass = Class.forName("blossom.project.config.client.BlossomConfigService");
            Constructor constructor = configServiceClass.getConstructor(Properties.class);
            ConfigService configService = (ConfigService) constructor.newInstance(properties);
            return configService;
        } catch (Throwable e) {
            throw new BlossomException(BlossomException.REFLECT_CREATE_ERROR,e.getMessage(), e);
        }
    }

}

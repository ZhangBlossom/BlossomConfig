package blossom.project.config.client;

import blossom.project.config.common.exception.BlossomException;

import java.util.Properties;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 18:43
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigService类
 */
public interface ConfigService {

    //参数无需namespace 因为发送请求的时候我会让请求内部自带namespace

    String getConfig(String configId, String group,String fileExtension) throws BlossomException;

 
    boolean publishConfig(String configId, String group, String content) throws BlossomException;

    
    boolean removeConfig(String configId, String group) throws BlossomException;

    /**
     * 监听某个配置文件
     * 由于Client模块并没有引入springboot
     * 因此监听器的代码应该由core提供
     * @param group
     * @param configId
     */
    void subscribeConfigChangeEvent(String group, String configId, Publish publish);

    Properties getProperties();

}

package blossom.project.config.server.service;


import blossom.project.config.server.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: ZhangBlossom
 * @date: 2023-12-28 23:01:24
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * Config类
 */
public interface ConfigService extends IService<Config> {

    /**
     * 获取配置信息
     * @param configId
     * @param namespace
     * @param group
     * @param type
     * @return
     */
    String getConfig(String configId, String namespace, String group,String type);

    /**
     * 发布配置
     * @param config
     * @return
     */
    void publishConfig(Config config);

    /**
     * 删除配置 - api调用
     * @param configId
     * @param namespace
     * @param group
     * @return
     */
    void removeConfig(String configId, String namespace, String group);

    /**
     * 删除配置 - console调用
     * @param id
     * @return
     */
    boolean removeConfig(Long id);

    /**
     * 删除配置 - console调用
     * @param ids
     * @return
     */
    boolean removeConfigs(List<Long> ids);

}


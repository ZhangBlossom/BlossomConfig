package blossom.project.config.core;


import blossom.project.config.client.ConfigService;
import blossom.project.config.common.exception.BlossomException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.PropertySource;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/29 20:16
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomPropertySourceBuilder类
 */
@Data
@Slf4j
public class BlossomPropertySourceBuilder {

    private ConfigService configService;

    private BlossomConfigProperties properties;

    public BlossomPropertySourceBuilder(ConfigService configService, BlossomConfigProperties properties) {
        this.configService = configService;
        this.properties = properties;
    }

    /**
     * 当前方法会完成BlossomPropertySource的构建
     *
     * @param configId
     * @param group
     * @param fileExtension
     * @return
     */
    BlossomPropertySource buildBlossomPropertySource(String configId, String group, String fileExtension) {
        //从配置中心得到配置并且封装为List类型的PropertySource
        List<PropertySource<?>> propertySources = loadBlossomConfigData(configId, group, fileExtension);
        //将List转换为最后我们需要的BlossomPropertySource
        BlossomPropertySource blossomPropertySource = new BlossomPropertySource(group, configId, new Date(),
                propertySources);
        return blossomPropertySource;
    }

    /**
     * 当前方法完成对配置中心配置的加载和解析
     * 并且最终返回PropertySource集合
     *
     * @param configId
     * @param group
     * @param fileExtension
     * @return
     */
    private List<PropertySource<?>> loadBlossomConfigData(String configId, String group, String fileExtension) {
        List<PropertySource<?>> propertySources = Collections.emptyList();
        try {
            //得到配置文件的内容
            String configData = this.configService.getConfig(configId, group, fileExtension);
            if (StringUtils.isBlank(configData)) {
                log.warn("the data from ConfigService is empty, configId: {}, group:{}", configId, group);
                return Collections.emptyList();
            }
            //在spring中想要将配置解析为PropertySource可以用自带的解析器--只提供了yaml和properties
            //也就是说json/xml等其他格式需要自己实现

            propertySources =
                    BlossomConfigDataHandler.getInstance().parseConfigData(configId, configData, fileExtension);
        } catch (BlossomException e) {
            log.error("get the config data from ConfigService failed, configId: {}, group:{},Exception:{}", configId,
                    group, e);
        } catch (Exception e) {
            log.error("parse the config data failed. Exception:{}", e);
        }
        return propertySources;
    }


}

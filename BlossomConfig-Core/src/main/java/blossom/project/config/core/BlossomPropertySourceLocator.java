package blossom.project.config.core;


import blossom.project.config.client.ConfigService;
import blossom.project.config.common.constants.BlossomConstants;
import blossom.project.config.common.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import static blossom.project.config.common.constants.BlossomConstants.DOT;
import static blossom.project.config.common.constants.BlossomConstants.SEPARATOR;


/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 17:35
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomPropertySourceLocator类
 * 在编写这个类之前应该先将ConfigService实现类编写完毕
 * 然后在这个类里面得到ConfigService之后
 * 调用里面的方法获取到配置中心的配置之后
 * 将配置加载到本地 同时考虑编写一套缓存
 */
@Slf4j
@Order(0)
public class BlossomPropertySourceLocator implements PropertySourceLocator {

    private BlossomConfigManager manager;

    private BlossomConfigProperties properties;

    //使用builder的方式得到来自各种地方的BlossomPropertySource
    //最后将BlossomPropertySource放入到CompositePropertySource即可
    private BlossomPropertySourceBuilder blossomPropertySourceBuilder;

    public BlossomPropertySourceLocator(BlossomConfigManager manager) {
        this.manager = manager;
        this.properties = manager.getProperties();
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        this.properties.setEnvironment(environment);
        ConfigService configService = manager.getConfigService();
        if (Objects.isNull(configService)) {
            log.warn("No instance of ConfigService was found,can not load config from ConfigService");
            return null;
        }
        this.blossomPropertySourceBuilder = new BlossomPropertySourceBuilder(configService, properties);
        CompositePropertySource ps = new CompositePropertySource(BlossomConstants.BLOSSOM_PROPERTY_SOURCE_NAME);
        loadApplicationConfig(ps);
        loadConfigLists(ps);
        return ps;
    }

    /**
     * 加载项目所有配置
     * @param ps
     */
    private void loadConfigLists(CompositePropertySource ps) {
        List<BlossomConfigProperties.BlossomConfig> configLists = this.properties.getConfigLists();
        configLists.forEach(config -> {
            loadConfigIfPresent(ps, config.getConfigId(), config.getGroup(), this.properties.getFileExtension());
        });

    }

    /**
     * 在项目项目原生配置
     * @param ps
     */
    private void loadApplicationConfig(CompositePropertySource ps) {
        Environment env = this.properties.getEnvironment();
        String applicationName = env.getProperty("spring.application.name");
        String group = this.properties.getGroup();
        String fileExtension = this.properties.getFileExtension();
        for (String profile : env.getActiveProfiles()) {
            //blossom-core-dev.yaml
            String configId = applicationName + SEPARATOR + profile + DOT + this.properties.getFileExtension();
            loadConfigIfPresent(ps, configId, group, fileExtension);
        }

    }

    private void loadConfigIfPresent(CompositePropertySource ps, String configId, String group, String fileExtension) {
        if (StringUtils.isBlank(configId)) {
            return;
        }
        if (StringUtils.isBlank(group)) {
            return;
        }
        Boolean validType = ConfigType.isValidType(fileExtension);
        //the file extension is unvalid;
        if (!validType) {
            return;
        }
        this.loadBlossomConfig(ps, configId, group, fileExtension);
    }

    private void loadBlossomConfig(CompositePropertySource ps, String configId, String group, String fileExtension) {
        //1:从配置中心获取配置 并且封装为BlossomPropertySource
        BlossomPropertySource blossomPropertySource =
                this.blossomPropertySourceBuilder.buildBlossomPropertySource(configId, group, fileExtension);
        //2：将配置转换为PropertySource ---能得到Properties类型即可
        //3:将配置添加到CompositePropertySource
        ps.addFirstPropertySource(blossomPropertySource);
    }


}

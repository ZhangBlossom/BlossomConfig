package blossom.project.config.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 17:30
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigBootstrapConfiguration类
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.blossom.config.enabled", matchIfMissing = true)
public class BlossomConfigBootstrapConfiguration {


    /**
     * 项目对配置中心配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public BlossomConfigProperties blossomConfigProperties(){
        return new BlossomConfigProperties();
    }

    /**
     * 配置中心管理器
     * @param blossomConfigProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public BlossomConfigManager blossomConfigManager(
            BlossomConfigProperties blossomConfigProperties) {
        return new BlossomConfigManager(blossomConfigProperties);
    }


    @Bean
    public BlossomConfigChangePublisher blossomConfigChangePublisher(ApplicationEventPublisher applicationEventPublisher){
        return new BlossomConfigChangePublisher(applicationEventPublisher);
    }

    /**
     * 配置中心配置加载器
     * @param blossomConfigManager
     * @return
     */
    @Bean
    public BlossomPropertySourceLocator blossomPropertySourceLocator(
            BlossomConfigManager blossomConfigManager) {
        return new BlossomPropertySourceLocator(blossomConfigManager);
    }
    @Bean
    public BlossomConfigChangeEventSubscriber blossomConfigChangeEventSubscriber(BlossomConfigManager manager,
                                                                         BlossomConfigChangePublisher publisher){
        return new BlossomConfigChangeEventSubscriber(manager.getConfigService(),publisher);
    }
    
}

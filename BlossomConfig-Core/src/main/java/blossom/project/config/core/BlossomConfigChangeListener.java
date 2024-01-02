package blossom.project.config.core;

import blossom.project.config.common.exception.BlossomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static blossom.project.config.common.constants.BlossomConstants.SEPARATOR;


/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 22:49
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */
@Slf4j
@Component
public class BlossomConfigChangeListener implements ApplicationListener<BlossomConfigChangeEvent> {

    private static final String REFRESH_SCOPE = "RefreshScope";

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private BeanDefinitionRegistry beanDefinitionRegistry;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        ScopeRegistry scopeRegistry = applicationContext.getBean(ScopeRegistry.class);
        this.beanDefinitionRegistry = scopeRegistry.getBeanDefinitionRegistry();
    }


    @Override
    public void onApplicationEvent(BlossomConfigChangeEvent event) {
        // 处理配置更改事件
        if (event.getConfigCache() != null) {
            // 处理发布事件
            System.out.println("Config published: " + event.getKey());
            doPublishEvent(event);

        } else {
            // 处理删除事件
            System.out.println("Config removed: " + event.getKey());
            doRemoveEvent(event);
        }
    }

    /**
     * 处理配置变更事件
     *
     * @param event
     */
    private void doPublishEvent(BlossomConfigChangeEvent event) {
        //1：根据event中的key 找到对应的配置
        String key = event.getKey();
        //2：根据新的content信息，解析完毕之后，重新添加到Environemnt中
        String content = event.getConfigCache().getContent();
        //得到文件解析格式
        String type = event.getConfigCache().getType();

        List<PropertySource<?>> newPropertySources = Collections.emptyList();
        //得到配置文件的内容
        if (StringUtils.isBlank(content)) {
            log.warn("the data from ConfigService is empty, key:{}", key);
            return;
        }
        //在spring中想要将配置解析为PropertySource可以用自带的解析器--只提供了yaml和properties
        //也就是说json/xml等其他格式需要自己实现
        String configId = parseKey(key);
        try {
            newPropertySources =
                    BlossomConfigDataHandler.getInstance().parseConfigData(configId, content, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 将新的PropertySource添加到Environment中
        for (PropertySource<?> propertySource : newPropertySources) {
            ((ConfigurableEnvironment) environment).getPropertySources().addFirst(propertySource);
        }

        // 触发环境变更事件，以刷新@Value注解的值
        applicationContext.publishEvent(new EnvironmentChangeEvent(
                applicationContext, Collections.singleton(event.getKey())
        ));

        // 刷新带有RefreshScope注解的Bean
        refreshScopedBeans();
    }

    /**
     * 刷新@Value注解的值
     */
    private void refreshScopedBeans() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            if (REFRESH_SCOPE.equalsIgnoreCase(beanDefinition.getScope())) {
                applicationContext.getBeanFactory().destroyScopedBean(beanDefinitionName);
                applicationContext.getBean(beanDefinitionName);
            }
        }
    }

    /**
     * 处理配置删除事件
     *
     * @param event
     */
    private void doRemoveEvent(BlossomConfigChangeEvent event) {
        //1：删除对应的Environment
        //2: 不刷新@Value
    }

    /**
     * 根据key返回configid
     * @param key
     * @return
     */
    private String parseKey(String key){
        return key.substring(key.lastIndexOf(SEPARATOR));
    }

}

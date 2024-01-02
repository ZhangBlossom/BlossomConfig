package blossom.project.config.core.value;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 21:20
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */
@Component
public class ConfigChangeListener implements ApplicationListener<ConfigChangeEvent> {

    private final ConfigurableApplicationContext applicationContext;

    public ConfigChangeListener(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        // 获取发生变更的配置项
        Map<String, Object> changedProperties = event.getChangedProperties();

        // 更新应用上下文中所有Bean的@Value注解字段
        updateValueAnnotatedFields(changedProperties);
    }

    private void updateValueAnnotatedFields(Map<String, Object> changedProperties) {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        String[] beanNames = beanFactory.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            Object bean = beanFactory.getBean(beanName);
            Class<?> targetClass = bean.getClass();

            // 遍历所有字段，查找@Value注解
            ReflectionUtils.doWithFields(targetClass, field -> {
                Value valueAnnotation = field.getAnnotation(Value.class);
                if (valueAnnotation != null) {
                    String key = extractKeyFromValueAnnotation(valueAnnotation);
                    if (changedProperties.containsKey(key)) {
                        field.setAccessible(true);
                        Object newValue = changedProperties.get(key);
                        ReflectionUtils.setField(field, bean, newValue);
                    }
                }
            });
        }
    }

    private String extractKeyFromValueAnnotation(Value valueAnnotation) {
        // 提取和解析@Value注解的值，可能需要解析Spring表达式
        String value = valueAnnotation.value();
        // TODO: 实现对Spring表达式的解析，这里只处理简单情况
        return value.startsWith("${") && value.endsWith("}") ?
               value.substring(2, value.length() - 1) : value;
    }
}

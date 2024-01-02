package blossom.project.config.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 21:20
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */
@SpringBootTest(classes = ConfigApplication.class)
public class ConfigurableEnvironmentTest
{

    @Autowired
    private ConfigurableEnvironment environment;

    // 添加或更新配置项
    public void addOrUpdateProperty(String key, String value) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> properties = new HashMap<>();
        properties.put(key, value);

        PropertySource<?> propertySource = new MapPropertySource("customPropertySource", properties);

        if (propertySources.contains("customPropertySource")) {
            // 更新现有的属性源
            ((MapPropertySource) propertySources.get("customPropertySource")).getSource().putAll(properties);
        } else {
            // 添加新的属性源
            propertySources.addLast(propertySource);
        }
    }

    // 获取配置项
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    // 删除配置项
    public void removeProperty(String key) {
        MutablePropertySources propertySources = environment.getPropertySources();
        if (propertySources.contains("customPropertySource")) {
            MapPropertySource propertySource = (MapPropertySource) propertySources.get("customPropertySource");
            propertySource.getSource().remove(key);
        }
    }

    // 列出所有配置项
    public Map<String, Object> listProperties() {
        Map<String, Object> properties = new HashMap<>();
        environment.getPropertySources().forEach(propertySource -> {
            if (propertySource instanceof MapPropertySource) {
                properties.putAll(((MapPropertySource) propertySource).getSource());
            }
        });
        return properties;
    }
}

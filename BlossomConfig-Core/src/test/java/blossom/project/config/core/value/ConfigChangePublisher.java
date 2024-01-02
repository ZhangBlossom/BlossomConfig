package blossom.project.config.core.value;

import org.springframework.context.ApplicationEventPublisher;
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
@Component
public class ConfigChangePublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ConfigChangePublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishConfigChange() {
        Map<String, Object> changedProperties = new HashMap<>();
        changedProperties.put("test.property", "updatedValue");

        ConfigChangeEvent event = new ConfigChangeEvent(this, changedProperties);
        eventPublisher.publishEvent(event);
    }
}

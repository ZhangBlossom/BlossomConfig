package blossom.project.config.core.value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 21:20
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom

 * 代表配置更改的事件，继承自 ApplicationEvent。
 */
@Component
public class ConfigChangeEvent extends ApplicationEvent {

    private final Map<String, Object> changedProperties;

    /**
     * 构造配置更改事件。
     *
     * @param source 事件源
     * @param changedProperties 包含变更的配置项及其值的映射
     */
    public ConfigChangeEvent(Object source, Map<String, Object> changedProperties) {
        super(source);
        this.changedProperties = Collections.unmodifiableMap(changedProperties);
    }

    /**
     * 获取发生变更的配置项及其值。
     *
     * @return 变更的配置项映射
     */
    public Map<String, Object> getChangedProperties() {
        return changedProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigChangeEvent)) return false;
        if (!super.equals(o)) return false;
        ConfigChangeEvent that = (ConfigChangeEvent) o;
        return Objects.equals(changedProperties, that.changedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), changedProperties);
    }

    @Override
    public String toString() {
        return "ConfigChangeEvent{" +
                "source=" + source +
                ", changedProperties=" + changedProperties +
                '}';
    }
}

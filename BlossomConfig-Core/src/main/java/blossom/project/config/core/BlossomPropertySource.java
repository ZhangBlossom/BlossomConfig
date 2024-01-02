package blossom.project.config.core;

import blossom.project.config.common.constants.BlossomConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/29 20:18
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomPropertySource类
 * 项目统一封装一个PS对象，方便后续的处理
 * 不然后续如果扩展代码的时候就需要一直new新对象
 * 但是代码是冗余的
 */
@Slf4j
public class BlossomPropertySource extends MapPropertySource {

    private final String group;
    private final String configId;
    private final Date timestamp;

    public BlossomPropertySource(String group, String configId, Date timestamp,
                              Map<String, Object> source) {
        super(String.join(BlossomConstants.SEPARATOR, group, configId), source);
        this.group = group;
        this.configId = configId;
        this.timestamp = timestamp;

    }

    public BlossomPropertySource(String group, String configId, Date timestamp,
                                 List<PropertySource<?>> propertySources){
        //super的操作要在第一行
        this(group,configId,timestamp, getPropertySourceMap(propertySources));
    }

    /**
     * @param propertySources
     * @return
     */
    private static  Map<String, Object> getPropertySourceMap(List<PropertySource<?>> propertySources) {
        if (CollectionUtils.isEmpty(propertySources)) {
            return Collections.emptyMap();
        }
        //get map from source directly
        if (propertySources.size() == 1) {
            PropertySource propertySource = propertySources.get(0);
            if (propertySource != null && propertySource.getSource() instanceof Map) {
                return (Map<String, Object>) propertySource.getSource();
            }
        }
        //more than one
        Map<String, Object> sourceMap = new LinkedHashMap<>();
        for (PropertySource<?> propertySource : propertySources) {
            if (propertySource == null) {
                continue;
            }
            //check the class
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                Map<String, Object> source = mapPropertySource.getSource();
                sourceMap.putAll(source);
            }
        }

        return sourceMap;
    }

}

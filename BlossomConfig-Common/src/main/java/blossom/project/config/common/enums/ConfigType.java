package blossom.project.config.common.enums;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum ConfigType {
    
    /**
     * config type is "properties".
     */
    PROPERTIES("properties"),
    
    /**
     * config type is "xml".
     */
    XML("xml"),
    
    /**
     * config type is "json".
     */
    JSON("json"),
    
    /**
     * config type is "text".
     */
    TEXT("text"),
    
    /**
     * config type is "yaml".
     */
    YAML("yaml");

    
    private final String type;
    
    private static final Map<String, ConfigType> LOCAL_MAP = new HashMap<>();
    
    static {
        for (ConfigType configType : values()) {
            LOCAL_MAP.put(configType.getType(), configType);
        }
    }
    
    ConfigType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public static ConfigType getDefaultType() {
        return YAML;
    }
    
    /**
     * check input type is valid.
     *
     * @param type config type
     * @return it the type valid
     */
    public static Boolean isValidType(String type) {
        if (StringUtils.isBlank(type)) {
            return false;
        }
        return null != LOCAL_MAP.get(type);
    }
}

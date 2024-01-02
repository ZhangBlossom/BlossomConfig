package blossom.project.config.common.constants;

import java.util.regex.Pattern;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 17:34
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConstants类
 */
public interface BlossomConstants {

    /**
     * the ProperSource name for this project
     */
    String BLOSSOM_PROPERTY_SOURCE_NAME ="BLOSSOM-PROPERTY-SOURCE";

    /**
     * config prefix
     */
    String SPRING_CLOUD_BLOSSOM_CONFIG_PREFIX = "spring.cloud.blossom.config";

    /**
     * COMMAS , .
     */
    String COMMA = ",";

    /**
     * SEPARATOR -
     */
    String SEPARATOR = "-";

    /**
     * DOT
     */
    String DOT = ".";


    /**
     * the basic develop paradigm request prefix
     */
    String BASE_PATH = "/v1";


    /**
     * this project default config file extension
     */
    String DEFAULT_EXTENSION = "yaml";

    /**
     * config controller
     */
    String CONFIG_CONTROLLER_PATH = BASE_PATH + "/config";

    /**
     * long-polling test controller
     * used to test the long polling
     */
    String LONG_POLLING_CONTROLLER_PATH = BASE_PATH + "/long-polling";

}

package blossom.project.config.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 18:44
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomException类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BlossomException extends RuntimeException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3913902031489277776L;

    /**
     * error code
     */
    private Integer code;

    /**
     * error message(tips)
     */
    private String message;

    /**
     * error class
     */
    private Throwable ex;

    // the code like 6xx means the init exception
    /**
     * Failed to create using reflection
     */
    public static final int REFLECT_CREATE_ERROR = 601;

    /**
     * Failed to connect the blossom config server
     */
    public static final int CONNECTION_FAILED = 602;

    //the code like 7xx means the runtimeexception server-exception
    //such as the get/post/delete request error

    /**
     * Failed to get config
     */
    public static final int GET_CONFIG_ERROR = 701;

    /**
     * Failed to publish config
     */
    public static final int PUBLISH_CONFIG_ERROR = 702;

    /**
     * Failed to remove config
     */
    public static final int REMOVE_CONFIG_ERROR = 703;
}

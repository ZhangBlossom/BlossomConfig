package blossom.project.config.common.constants;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/29 18:49
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigPropertiesKeyConstants接口
 * 当前常量的接口用于初始化创建配置中心的时候的配置
 * 作为key的方式进行提供
 */
public interface BlossomConfigPropertiesKeyConstants {

    // 配置中心的服务器地址
    String SERVER_ADDR = "server-addr";

    // 配置中心的用户名
    String USERNAME = "username";

    // 配置中心的密码
    String PASSWORD = "password";

    // 配置分组
    String GROUP = "group";

    // 配置中心的命名空间
    String NAMESPACE = "namespace";

    // 配置文件的扩展名
    String FILE_EXTENSION = "file-extension";

    // 从配置中心获取配置的超时时间
    String TIMEOUT = "timeout";

    // 获取配置的最大重试次数
    String MAX_RETRY = "max-retry";

    // 长轮询的超时时间
    String CONFIG_LONG_POLL_TIMEOUT = "config-long-poll-timeout";

    // 重试间隔时间
    String CONFIG_RETRY_TIME = "config-retry-time";

    // 集群名称
    String CLUSTER_NAME = "cluster-name";

    // 集群节点名称
    String CLUSTER_NODE_NAME = "cluster-node-name";

    // 是否启用配置刷新
    String REFRESH_ENABLED = "refresh-enabled";

    //项目全部配置
    String CONFIG_LISTS = "config-lists";
}


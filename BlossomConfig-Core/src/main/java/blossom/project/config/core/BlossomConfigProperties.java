package blossom.project.config.core;

import blossom.project.config.common.constants.BlossomConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import static blossom.project.config.common.constants.BlossomConfigPropertiesKeyConstants.*;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


/**
 * @author: ZhangBlossom
 * @date: 2023/12/28 17:31
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomConfigProperty类
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(prefix = BlossomConstants.SPRING_CLOUD_BLOSSOM_CONFIG_PREFIX)
public class BlossomConfigProperties {


    // 当前项目直连的项目配置id
    private String configId;

    // 配置中心的服务器地址 格式 localhost:9999
    private String serverAddr;

    // 配置中心的用户名，用于访问认证
    private String username;

    // 配置中心的密码，用于访问认证
    private String password;

    // 配置分组，默认为"DEFAULT_GROUP"
    private String group = "DEFAULT_GROUP";

    // 配置中心的命名空间，默认为"DEFAULT"
    private String namespace = "DEFAULT";

    // 配置文件的扩展名，默认为"yaml"
    private String fileExtension = "yaml";

    // 从配置中心获取配置的超时时间（毫秒），默认为5000毫秒
    private int timeout = 5000;

    // 获取配置的最大重试次数
    private int maxRetry = 3;

    // 长轮询的超时时间
    private int configLongPollTimeout = 5000;

    // 重试间隔时间
    private int configRetryTime = 5000;

    // 集群名称
    private String clusterName = "DEFAULT";

    // 集群节点名称
    private String clusterNodeName;

    // 是否启用配置刷新，默认为true
    private boolean refreshEnabled = true;

    //项目全部配置信息
    private List<BlossomConfig> configLists = Collections.emptyList();

    @Autowired
    @JsonIgnore
    private Environment environment;


    @Data
    public static class BlossomConfig {

        private String configId;

        private String group = "DEFAULT_GROUP";

    }


    @PostConstruct
    public void init() {
        this.overrideFromEnv();
        this.setDefaultClusterNodeName();
    }

    private void overrideFromEnv() {
        if (environment == null) {
            return;
        }
        if (StringUtils.isEmpty(this.getServerAddr())) {

                serverAddr = environment.resolvePlaceholders(
                        "${spring.cloud.blossom.server-addr:127.0.0.1:9999}");
            this.setServerAddr(serverAddr);
        }
        if (StringUtils.isEmpty(this.getUsername())) {
            this.setUsername(
                    environment.resolvePlaceholders("${spring.cloud.blossom.username:}"));
        }
        if (StringUtils.isEmpty(this.getPassword())) {
            this.setPassword(
                    environment.resolvePlaceholders("${spring.cloud.blossom.password:}"));
        }
    }

    private void setDefaultClusterNodeName() {
        if (StringUtils.isEmpty(this.clusterNodeName)) {
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                String port = environment.getProperty("server.port", "8080"); // 获取应用端口号，默认8080
                this.clusterNodeName = "node-" + hostName + "-" + port; // 例如: myhostname-8080
            } catch (Exception e) {
                log.error("Error while setting default cluster node name", e);
                // 设置备用默认值
                this.clusterNodeName = "node-" + System.currentTimeMillis();
            }
        }
    }

    /**
     * 将BlossomConfigProperties转换为Properties
     * 然后用于创建ConfigService
     * @return
     */
    public Properties parseProperties() {
        Properties properties = new Properties();

        // 为每个配置项赋值
        properties.put(SERVER_ADDR, Objects.toString(this.serverAddr, ""));
        properties.put(USERNAME, Objects.toString(this.username, ""));
        properties.put(PASSWORD, Objects.toString(this.password, ""));
        properties.put(GROUP, Objects.toString(this.group, "DEFAULT_GROUP"));
        properties.put(NAMESPACE, Objects.toString(this.namespace, "DEFAULT"));
        properties.put(FILE_EXTENSION, Objects.toString(this.fileExtension, "yml"));
        properties.put(TIMEOUT, Integer.toString(this.timeout));
        properties.put(MAX_RETRY, Integer.toString(this.maxRetry));
        properties.put(CONFIG_LONG_POLL_TIMEOUT, Integer.toString(this.configLongPollTimeout));
        properties.put(CONFIG_RETRY_TIME, Integer.toString(this.configRetryTime));
        properties.put(CLUSTER_NAME, Objects.toString(this.clusterName, "DEFAULT"));
        properties.put(CLUSTER_NODE_NAME, Objects.toString(this.clusterNodeName, ""));
        properties.put(REFRESH_ENABLED, Boolean.toString(this.refreshEnabled));
        properties.put(CONFIG_LISTS,this.configLists);

        return properties;
    }

    @Override
    public String toString() {
        return "BlossomConfigProperties{" +
                "configId='" + configId + '\'' +
                ", serverAddr='" + serverAddr + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", group='" + group + '\'' +
                ", namespace='" + namespace + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", timeout=" + timeout +
                ", maxRetry='" + maxRetry + '\'' +
                ", configLongPollTimeout='" + configLongPollTimeout + '\'' +
                ", configRetryTime='" + configRetryTime + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", clusterNodeName='" + clusterNodeName + '\'' +
                ", refreshEnabled=" + refreshEnabled +
                '}';
    }


}

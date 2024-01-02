package blossom.project.config.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 20:40
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * ConfigApplication类
 */
@SpringBootApplication
public class ConfigApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ConfigApplication.class, args);
        BlossomConfigProperties properties = context.getBean(BlossomConfigProperties.class);
        System.out.println(properties);
        ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println(environment);
    }
}

package blossom.project.config.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 20:40
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * EnvironmentPostProcessorTest类
 */
@SpringBootTest(classes = ConfigApplication.class)
public class EnvironmentPostProcessorTest {

    //ConfigurableEnvironment是Environment的一个子接口，
    // 它不仅提供访问属性值的方法，还允许修改属性源。
    @Autowired
    private ConfigurableEnvironment configurableEnvironment;


    //只要和配置有关基本都会用到这个类
    @Autowired
    private Environment environment;

    @Test
    public void test() {

        String name = environment.getProperty("name");

        System.out.printf("动态加载之前" +name);

        Map<String,String> map = new HashMap<>();

        map.put("name","嘟嘟");

        configurableEnvironment.getPropertySources()
                .addFirst(
                new OriginTrackedMapPropertySource("application.yml", map)
        );

        String property = environment.getProperty("name");

        System.out.printf("动态加载之后" +property);

    }
}

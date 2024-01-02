package blossom.project.config.core.beanregistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/25 17:01
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * RedisDataLoader类
 * 1:当前类的作用是启动的时候加载redis中的配置文件的数据
 * 2:并且判断当前项目中是否有对应的redis配置，有，不添加，无，添加
 * 3:如果redis的配置发生了变更，要确保当前项目的@Value对应的配置也发生变更。
 */
@Component
@Slf4j
public class RedisDataLoader {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //spring项目中所有的配置信息都在这里
    @Autowired
    private Environment environment;
    //他会从上面获取配置信息然后进行对自己的填充
    //并且由于当前注解是在类初始化的时候就已经有值了
    //所以运行时的值的修改，不会影响@Value的值
    @Value("${name1:defaultValue1}")
    private String name1;

    @Value("${name2:defaultValue2}")
    private String name2;

    @Autowired
    private ConfigurableApplicationContext applicationContext;
    private static final String REDIS_CONFIG = "redis-config";

    private static final String REFRESH_SCOPE = "RefreshScope";
    private BeanDefinitionRegistry beanDefinitionRegistry;

    private long hashCode = hashCode();

    @PostConstruct
    public void init() {
        System.out.println("first hashCode:"+hashCode);
        Map<Object, Object> redisConfig =
                redisTemplate.opsForHash().entries(REDIS_CONFIG);

        createPropertySource(redisConfig);
        System.out.println("startup successfully...");
        System.out.println("attempt to get the redis config :" + environment.getProperty("name1"));
        System.out.println("attempt to get the redis config :" + environment.getProperty("name2"));

        ScopeRegistry scopeRegistry = applicationContext.getBean(ScopeRegistry.class);
        beanDefinitionRegistry = scopeRegistry.getBeanDefinitionRegistry();


        while (true) {
            try {
                //在这段时间内对redis的配置进行修改就可以实现配置动态更新
                TimeUnit.SECONDS.sleep(1);
                final Map<Object, Object> newConfig =
                        redisTemplate.opsForHash().entries(REDIS_CONFIG);
                updateConfig(newConfig);

                refreshValue();

                TimeUnit.SECONDS.sleep(1);
                System.out.println("attempt to get the redis config :" + environment.getProperty("name1"));
                System.out.println("attempt to get the redis config :" + environment.getProperty("name2"));

                System.out.println(name1);
                System.out.println(name2);

                System.out.println("new hashCode:"+hashCode);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private void refreshValue() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            if (REFRESH_SCOPE.equalsIgnoreCase(beanDefinition.getScope())){
                //如果存在当前scope属性 销毁
                applicationContext.getBeanFactory().destroyScopedBean(beanDefinitionName);
                //通过get方法就可以重建
                applicationContext.getBean(beanDefinitionName);
            }
        }
    }

    private void updateConfig(Map<Object, Object> newConfig) {
        MutablePropertySources propertySources =
                applicationContext.getEnvironment().getPropertySources();
        if (checkContainsPropertySource()) {
            propertySources.addLast(
                    new OriginTrackedMapPropertySource(REDIS_CONFIG, newConfig));
        }
    }

    private void createPropertySource(Map<Object, Object> redisConfig) {
        MutablePropertySources propertySources =
                applicationContext.getEnvironment().getPropertySources();
        if (!checkContainsPropertySource()) {
            propertySources.addLast(
                    new OriginTrackedMapPropertySource(REDIS_CONFIG, redisConfig));
        }
    }

    private boolean checkContainsPropertySource() {
        MutablePropertySources propertySources =
                applicationContext.getEnvironment().getPropertySources();
        //存在返回true
        return propertySources
                .stream()
                .filter(x -> REDIS_CONFIG.equalsIgnoreCase(x.getName()))
                .count() > 0;
    }

}

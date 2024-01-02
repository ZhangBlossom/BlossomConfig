package blossom.project.config.core.beanregistry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/25 19:42
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * RebuildClass类
 */
@Component
@Scope(scopeName = "myRefreshScope")
public class RebuildClass {
    @Value("${name1:defaultValue1}")
    private String name1;

    @Value("${name2:defaultValue2}")
    private String name2;

    private long hashCode = hashCode();

    @PostConstruct
    public void init() {
        System.out.println("first hashCode:"+hashCode);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        service.schedule(()->{
            System.out.println(name1);
            System.out.println(name2);

            System.out.println("new hashCode:"+hashCode);
        }, 3,TimeUnit.SECONDS);

    }
}

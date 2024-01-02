package blossom.project.config.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 23:16
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * TestCOntroller类
 */

@RestController
@Scope(scopeName = "RefreshScope")
public class TestController {

    @Autowired
    private Environment environment;


    @Value("${nacos.test}")
    private String test;

    @GetMapping("")
    public String test(){
        Environment environment1 = this.environment;
        return test;
    }
}

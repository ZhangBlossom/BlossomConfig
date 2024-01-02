package blossom.project.config.core.value;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 21:20
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */
@SpringBootTest
public class ConfigChangeTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private ConfigChangePublisher configChangePublisher;

    @Test
    public void testConfigChange() {
        assertEquals("default", testConfig.getTestProperty());

        configChangePublisher.publishConfigChange();

        // 这里可能需要一些时间来等待配置更新
        // 可能需要使用 Thread.sleep 或其他机制来等待
        assertEquals("updatedValue", testConfig.getTestProperty());
    }
}

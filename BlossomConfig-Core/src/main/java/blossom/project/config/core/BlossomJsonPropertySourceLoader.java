package blossom.project.config.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/29 21:27
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * BlossomJsonPropertySourceLoader类
 */
@Deprecated
public class BlossomJsonPropertySourceLoader implements PropertySourceLoader {

    @Override
    public String[] getFileExtensions() {
        return new String[]{"json"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        if (!resource.exists()) {
            return Collections.emptyList();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            // 将JSON文件转换为Map
            Map<String, Object> source = mapper.readValue(resource.getInputStream(), Map.class);
            // 创建一个新的PropertySource
            PropertySource<?> propertySource = new MapPropertySource(name, source);
            return Collections.singletonList(propertySource);
        } catch (IOException e) {
            // 处理读取JSON文件时的异常
            throw new IOException("Failed to parse JSON resource: " + resource.getDescription(), e);
        }
    }
}

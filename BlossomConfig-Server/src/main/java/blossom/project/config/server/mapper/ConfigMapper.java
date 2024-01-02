package blossom.project.config.server.mapper;

import blossom.project.config.server.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/27 23:45
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * ConfigMapper
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

    boolean publishConfig(Config config);

    Config getConfig(
            @Param("configId") String configId,
            @Param("namespace") String namespace,
            @Param("group") String group,
            @Param("type") String type);

    boolean removeConfig(
            @Param("configId") String configId,
            @Param("namespace") String namespace,
            @Param("group") String group
    );

    boolean editConfig(Config config);
}


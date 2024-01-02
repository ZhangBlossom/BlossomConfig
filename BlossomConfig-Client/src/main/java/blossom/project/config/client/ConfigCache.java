package blossom.project.config.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 21:54
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * COnfigCache并不存储配置内容，而至存储一下配置文件
 * 的key和md5，用来判断云端配置文件是否和本地配置文件相等
 * 如果相等，不更新，否则更新。
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigCache {

    private String key;

    private String lastCallMd5;

    private String content;

    private Date modifyTimestamp;

    private String type;

    private ConfigReadWriteLock lock;
}

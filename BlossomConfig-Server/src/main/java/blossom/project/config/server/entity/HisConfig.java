package blossom.project.config.server.entity;

import blossom.project.config.common.enums.ConfigType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/27 23:45
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * ConfigMapper
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "his_config")
public class HisConfig {
    //id
    @TableId(type = IdType.AUTO)
    private Long id;

    //config_id 文件配置id，也叫文件名称
    private String configId;
    
    private String namespace;
    
    private String group;

    //content
    private String content;

    //文件类型后缀
    private String type;
    //md5
    private String md5;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //创建人
    private String createBy;
    //修改人
    private String updateBy;
    //状态 0：正常启用 1：后续扩展
    private Integer status;

    //是否删除 0：否 1：是
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

}



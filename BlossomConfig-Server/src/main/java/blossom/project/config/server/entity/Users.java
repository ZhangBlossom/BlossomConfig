package blossom.project.config.server.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName(value = "users")
public class Users  {
    //id
    @TableId(type = IdType.AUTO)
    private Long id;

    
    private String username;
    
    private String password;
    
    private Integer enabled;



}



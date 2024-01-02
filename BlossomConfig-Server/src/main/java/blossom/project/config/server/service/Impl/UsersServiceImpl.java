package blossom.project.config.server.service.Impl;


import blossom.project.config.server.entity.Users;
import blossom.project.config.server.mapper.UsersMapper;
import blossom.project.config.server.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: ZhangBlossom
 * @date: 2023-12-28 23:01:27
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * Users类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
 
    private final UsersMapper usersMapper;

    }


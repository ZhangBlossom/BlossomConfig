package blossom.project.config.server.service.Impl;


import blossom.project.config.server.entity.HisConfig;
import blossom.project.config.server.mapper.HisConfigMapper;
import blossom.project.config.server.service.HisConfigService;
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
 * HisConfig类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HisConfigServiceImpl extends ServiceImpl<HisConfigMapper, HisConfig> implements HisConfigService {

     private final HisConfigMapper hisConfigMapper;
 }

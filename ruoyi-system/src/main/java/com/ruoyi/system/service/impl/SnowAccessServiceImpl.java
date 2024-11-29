package com.ruoyi.system.service.impl;



import com.ruoyi.system.domain.dto.SnowAccessLog;
import com.ruoyi.system.mapper.SnowAccessLogMapper;
import com.ruoyi.system.service.ISnowAccessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("snowAccessService")
public class SnowAccessServiceImpl implements ISnowAccessService {
    @Resource
    private SnowAccessLogMapper snowAccessLogMapper;


    @Override
    public List<SnowAccessLog> list() {
        return snowAccessLogMapper.list();
    }

    @Override
    public int insert(SnowAccessLog snowAccessLog) {
        return snowAccessLogMapper.insert(snowAccessLog);
    }
}

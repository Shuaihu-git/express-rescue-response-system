package com.ruoyi.system.service.impl;



import com.ruoyi.system.domain.dto.ScanLog;
import com.ruoyi.system.domain.dto.SnowAccessLog;
import com.ruoyi.system.mapper.ScanLogMapper;
import com.ruoyi.system.mapper.SnowAccessLogMapper;
import com.ruoyi.system.service.IScanLogService;
import com.ruoyi.system.service.ISnowAccessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("scanLogService")
public class ScanLogServiceImpl implements IScanLogService {
    @Resource
    private ScanLogMapper scanLogMapper;

    @Override
    public List<ScanLog> list(ScanLog scanLog) {
        return scanLogMapper.list(scanLog);
    }

    @Override
    public int insert(ScanLog scanLog) {
        return scanLogMapper.insert(scanLog);
    }

    @Override
    public int update(ScanLog scanLog) {
        return scanLogMapper.update(scanLog);
    }
}

package com.ruoyi.system.service;


import com.ruoyi.system.domain.dto.ScanLog;

import java.util.List;

public interface IScanLogService {
    List<ScanLog> list();
    int insert(ScanLog scanLog);
    int update(ScanLog scanLog);
}

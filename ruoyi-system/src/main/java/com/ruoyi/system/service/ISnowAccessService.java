package com.ruoyi.system.service;


import com.ruoyi.system.domain.dto.SnowAccessLog;

import java.util.List;

public interface ISnowAccessService {
    List<SnowAccessLog> list();
    int insert(SnowAccessLog snowAccessLog);
}

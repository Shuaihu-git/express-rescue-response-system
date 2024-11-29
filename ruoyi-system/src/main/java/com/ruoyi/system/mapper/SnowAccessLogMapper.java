package com.ruoyi.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.SnowAccessLog;

import java.util.List;

public interface SnowAccessLogMapper extends BaseMapper<SnowAccessLog> {
    List<SnowAccessLog> list();

    int insert(SnowAccessLog log);



}

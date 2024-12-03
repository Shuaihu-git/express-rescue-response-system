package com.ruoyi.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.ScanLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScanLogMapper extends BaseMapper<ScanLog> {
    List<ScanLog> list(ScanLog scanLog);

    int insert(ScanLog scanLog);

    int update(ScanLog scanLog);


    ScanLog selectScanLogById(Long id);
    int getCountOnTheMonth();
    int getCountOnTheMonthByType(@Param("type")String type);
    List<Integer> getNumberByHours();
}

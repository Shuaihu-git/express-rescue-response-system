package com.ruoyi.web.controller.rrqc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Weather {
    private WeatherLocation location;
    private Now now;
    private Date lastUpdate; // 数据更新时间（该城市的本地时间）
}

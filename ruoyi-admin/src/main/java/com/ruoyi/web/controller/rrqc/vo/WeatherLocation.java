package com.ruoyi.web.controller.rrqc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherLocation {
    private String id;
    private String name;
    private String country;
    private String path;
    private String timezone;
    private String timezoneOffset;
}
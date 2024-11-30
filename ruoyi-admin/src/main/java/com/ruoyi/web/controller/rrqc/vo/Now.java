package com.ruoyi.web.controller.rrqc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Now {
    private String text; // 天气现象文字
    private String code; // 天气现象代码
    private Double temperature; // 温度，单位为c摄氏度或f华氏度
    private Double feelsLike; // 体感温度，单位为c摄氏度或f华氏度
    private Double pressure; // 气压，单位为mb百帕或in英寸
    private Integer humidity; // 相对湿度，0~100，单位为百分比
    private Double visibility; // 能见度，单位为km公里或mi英里
    private String windDirection; // 风向文字
    private Integer windDirectionDegree; // 风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西
    private Double windSpeed; // 风速，单位为km/h公里每小时或mph英里每小时
    private Integer windScale; // 风力等级
    private Integer clouds; // 云量，单位%，范围0~100，天空被云覆盖的百分比
    private Double dewPoint; // 露点温度

}
package com.ruoyi.web.controller.rrqc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {
    // 经度，用于表示地理位置的东西方向
    private String longitude;

    // 纬度，用于表示地理位置的南北方向
    private String latitude;

}

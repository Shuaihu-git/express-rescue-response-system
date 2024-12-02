package com.ruoyi.web.controller.rrqc.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneNumber extends BaseEntity {
    private String number1;
    private String number2;

}

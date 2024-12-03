package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("scanlog")
public class ScanLog extends BaseEntity {
    /**
     *日志id
     */
    @TableId(value = "id",type = IdType.AUTO)
//    @Excel(name = "扫码日志编号", cellType = Excel.ColumnType.NUMERIC)
    private Long logId;
    @Excel(name = "内容")
    private String content;
    @Excel(name = "桩号")
    private String zhuangNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "扫码时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    @Excel(name = "处理状态", readConverterExp = "0=待处理,1=已处理")
    private String type;

    private String address;
    // 经度，用于表示地理位置的东西方向
    @Excel(name = "经度")
    private String longitude;

    // 纬度，用于表示地理位置的南北方向
    @Excel(name = "纬度")
    private String latitude;
    @Excel(name = "天气")
    private String weather;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "处理人")
    private String dealer;

}

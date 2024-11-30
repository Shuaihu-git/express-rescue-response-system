package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("scanlog")
public class ScanLog {
    /**
     *日志id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long logId;

    private String content;
    private String zhuangNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String type;
    private String address;
    // 经度，用于表示地理位置的东西方向
    private String longitude;

    // 纬度，用于表示地理位置的南北方向
    private String latitude;
    private String weather;
    private String remark;
    private String dealer;

}

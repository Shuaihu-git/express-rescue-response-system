package com.ruoyi.web.controller.rrqc;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.dto.ScanLog;
import com.ruoyi.system.domain.dto.SnowAccessLog;
import com.ruoyi.system.service.IScanLogService;
import com.ruoyi.system.service.ISnowAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
public class ScanLogController extends BaseController {

    @Autowired
    private IScanLogService scanLogService;

    @GetMapping("/qrlog/list")
    @PreAuthorize("@ss.hasPermi('rrqc:scanlog:list')")
    public TableDataInfo list() {
        startPage();
        return getDataTable(scanLogService.list());
    }
    @PostMapping("/qrlog/update")
    @PreAuthorize("@ss.hasPermi('rrqc:scanlog:edit')")
    public AjaxResult update(@RequestBody ScanLog scanLog) {
        if (Objects.isNull(scanLog)){
            return error("错误参数");
        }
        scanLog.setDealer(getUsername());
        return toAjax(scanLogService.update(scanLog));
    }
}

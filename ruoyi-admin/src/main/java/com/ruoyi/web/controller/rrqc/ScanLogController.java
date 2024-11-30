package com.ruoyi.web.controller.rrqc;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.dto.ScanLog;
import com.ruoyi.system.service.IScanLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
public class ScanLogController extends BaseController {

    @Autowired
    private IScanLogService scanLogService;

    @GetMapping("/qrlog/list")
    @PreAuthorize("@ss.hasPermi('rrqc:scanlog:list')")
    @Log(title = "扫码日志列表")
    public TableDataInfo list(ScanLog scanLog) {
        startPage();
        return getDataTable(scanLogService.list(scanLog));
    }
    @PutMapping("/qrlog/update")
    @PreAuthorize("@ss.hasPermi('rrqc:scanlog:edit')")
    @Log(title = "扫码日志编辑")
    public AjaxResult update(@RequestBody ScanLog scanLog) {
        if (Objects.isNull(scanLog)){
            return error("错误参数");
        }
        scanLog.setDealer(getUsername());
        return toAjax(scanLogService.update(scanLog));
    }
    @Log(title = "扫码日志Excel导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('rrqc:scanlog:export')")
    @PostMapping("/qrlog/export")
    public void export(HttpServletResponse response, ScanLog scanLog)
    {
        List<ScanLog> list = scanLogService.list(scanLog);
        ExcelUtil<ScanLog> util = new ExcelUtil<ScanLog>(ScanLog.class);
        util.exportExcel(response, list, "扫码记录");
    }
    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('rrqc:scanlog:query')")
    @GetMapping(value = "/qrlog/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(scanLogService.selectScanLogById(id));
    }
}

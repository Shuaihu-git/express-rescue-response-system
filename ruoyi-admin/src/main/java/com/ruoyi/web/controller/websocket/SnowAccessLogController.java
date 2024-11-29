package com.ruoyi.web.controller.websocket;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.dto.SnowAccessLog;
import com.ruoyi.system.service.ISnowAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
public class SnowAccessLogController extends BaseController {

    @Autowired
    private ISnowAccessService snowAccessService;

    @GetMapping("/qrlog/list")
    public TableDataInfo list() {
        startPage();
        return getDataTable(snowAccessService.list());
    }
    @GetMapping("/qrlog/add")
    public Integer add() {
        SnowAccessLog snowAccessLog = new SnowAccessLog();
        snowAccessLog.setData("G42 K-97898988发起救援服务！！！");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        snowAccessLog.setTime(simpleDateFormat.format(new Date()));
        snowAccessLog.setTag("测试");
        return snowAccessService.insert(snowAccessLog);
    }
}

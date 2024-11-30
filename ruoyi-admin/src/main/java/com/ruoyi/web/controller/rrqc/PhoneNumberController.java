package com.ruoyi.web.controller.rrqc;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.rrqc.dto.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
public class PhoneNumberController extends BaseController {
    @Resource
    private RedisCache redisCache;
    @Value("${tel.number1}")
    private static String number1;
    @Value("${tel.number2}")
    private static String number2;
    @GetMapping("/tel/get")
    public PhoneNumber getPhoneNumber(){
        PhoneNumber cacheList = redisCache.getCacheObject(CacheConstants.PHONE_NAME_KEY);
        return cacheList;
    }
    @PutMapping("/tel/add")
    public AjaxResult getPhoneNumber(@RequestBody PhoneNumber numbers){
        if (Objects.isNull(numbers)){
            return error("请输入救援电话号码");
        }
        redisCache.deleteObject(CacheConstants.PHONE_NAME_KEY);
        redisCache.setCacheObject(CacheConstants.PHONE_NAME_KEY, numbers);
        return success("添加成功");
    }
}

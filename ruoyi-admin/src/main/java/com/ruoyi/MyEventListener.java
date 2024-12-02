package com.ruoyi;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.rrqc.dto.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class MyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private RedisCache redisCache;
    @Value("${tel.number1}")
    private String number1;
    @Value("${tel.number2}")
    private String number2;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        PhoneNumber cache = redisCache.getCacheObject(CacheConstants.PHONE_NAME_KEY);
        if (Objects.isNull(cache)){
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setNumber1(number1);
            phoneNumber.setNumber2(number2);
            redisCache.setCacheObject(CacheConstants.PHONE_NAME_KEY, phoneNumber);
        }

    }
}
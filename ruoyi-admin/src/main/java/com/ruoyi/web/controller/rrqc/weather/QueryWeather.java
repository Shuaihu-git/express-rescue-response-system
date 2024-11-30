package com.ruoyi.web.controller.rrqc.weather;

import com.ruoyi.web.controller.rrqc.vo.Results;
import com.ruoyi.web.controller.rrqc.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("queryWeather")
public class QueryWeather {


    @Value("${weather.address}")
    private String weatherAddress;
    @Value("${weather.key}")
    private String weatherKey;
    @Value("${weather.url}")
    private String weatherUrl;



    public Weather getWeather() {
        String url = weatherUrl+"&key="+weatherKey+"&location="+weatherAddress;
        Results forEntity = new RestTemplate().getForObject(url, Results.class);
        Weather weather = forEntity.getResults().get(0);
        return weather;
    }

}

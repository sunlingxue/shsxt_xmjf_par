package com.shsxt.service;


import com.shsxt.xmjf.api.service.ISmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestSmsService {

    @Resource
    private ISmsService smsService;

    @Test
    public  void test01(){
        smsService.sendSms("18844197942",1);
    }
}

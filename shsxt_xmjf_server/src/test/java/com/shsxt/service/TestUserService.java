package com.shsxt.service;

import com.shsxt.xmjf.api.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestUserService {

    @Resource
    private IUserService userService;

    @Test
    public void test01(){

        System.out.println(userService.queryUserByUserId(93));
    }

}

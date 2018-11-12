package com.shsxt.service;

import cn.hutool.core.date.DateUtil;
import com.shsxt.xmjf.api.po.BasExperiencedGold;
import com.shsxt.xmjf.server.db.dao.BasExperiencedGoldMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class Testgold {

    @Resource
    private BasExperiencedGoldMapper basExperiencedGoldMapper;

    @Test
    public void test01(){
        BasExperiencedGold basExperiencedGold = new BasExperiencedGold();
        basExperiencedGold.setUserId(32);
        basExperiencedGold.setAddtime(new Date());
        basExperiencedGold.setAmount(BigDecimal.valueOf(2888));
        basExperiencedGold.setExpiredTime(DateUtil.offsetDay(new Date(),15));//15天后过期
        basExperiencedGold.setGoldName("注册体验金");
        basExperiencedGold.setStatus(2);
        basExperiencedGold.setUsefulLife(15);
        basExperiencedGold.setWay("注册获取");
        basExperiencedGold.setInvestId(0);
        basExperiencedGoldMapper.save(basExperiencedGold);
    }

}

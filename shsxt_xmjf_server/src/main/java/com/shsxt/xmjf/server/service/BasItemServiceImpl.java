package com.shsxt.xmjf.server.service;

import com.github.pagehelper.PageHelper;
import com.shsxt.xmjf.api.Utils.PageList;
import com.shsxt.xmjf.api.query.BasItemQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import com.shsxt.xmjf.server.db.dao.BasItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class BasItemServiceImpl implements IBasItemService {

    @Resource
    private BasItemMapper basItemMapper;
    @Override
    public PageList queryBasItemByParams(BasItemQuery basItemQuery) {
        PageHelper.startPage(basItemQuery.getPageNum(),basItemQuery.getPageSize());
        List<Map<String, Object>> list = basItemMapper.queryBasItemsByParams(basItemQuery);
        if (list != null && list.size()>0){
            for (Map<String,Object> map:list){
                Integer status = (Integer) map.get("item_status");

                //计算开放状态剩余可投金额
                if (status == 10){
                    BigDecimal itemAccount = (BigDecimal) map.get("item_account");
                    BigDecimal onGoingAccount = (BigDecimal) map.get("item_ongoing_account");
                    map.put("syAccount",itemAccount.subtract(onGoingAccount));

                }

                //当天即将开放项目
                if (status==1){
                    Date relaseTime = (Date) map.get("release_time");
                    long syTime = (relaseTime.getTime()-new Date().getTime())/1000;
                    map.put("syTime",syTime);
                }
            }
        }

        return new PageList(list);
    }

    @Override
    public void updateBasItemStatusToOpen(Integer itemId) {
        basItemMapper.updateBasItemStatusToOpen(itemId);
    }
}

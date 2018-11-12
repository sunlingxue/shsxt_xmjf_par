package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.Utils.PageList;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;

import java.math.BigDecimal;
import java.util.List;

public interface IBusAccountRechargeService {

    public String addBusAccountRecharge(BigDecimal amount,String busiPwd,Integer userId);

    public  void updateBusAccountRecharge(String orderNo,String totalAmount,String tradeNo);

    public  void updateBusAccountRecharge02(String orderNo,String totalAmount,String tradeNo,String appId,String sellId);

    public PageList queryRechageByParams(BusAccountRechargeQuery busAccountRechargeQuery);

}

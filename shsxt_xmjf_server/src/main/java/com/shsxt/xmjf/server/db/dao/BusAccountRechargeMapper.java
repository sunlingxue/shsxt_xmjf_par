package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusAccountRecharge;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;

import java.util.List;
import java.util.Map;

public interface BusAccountRechargeMapper extends BaseMapper<BusAccountRecharge>{


    public BusAccountRecharge queryBusAccountRechargeByOrderNo(String orserNo);

    public List<Map<String,Object>> queryRechargesByParams(BusAccountRechargeQuery busAccountRechargeQuery);
}
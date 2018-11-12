package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusAccount;

public interface BusAccountMapper extends BaseMapper<BusAccount>{
    public BusAccount queryBusAccountByUserId(Integer userId);
}
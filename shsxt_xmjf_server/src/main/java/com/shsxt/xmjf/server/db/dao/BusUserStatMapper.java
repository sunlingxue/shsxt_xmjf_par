package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusUserStat;
import com.shsxt.xmjf.api.po.BusUserStatKey;

public interface BusUserStatMapper extends BaseMapper<BusUserStat> {
    public BusUserStat queryBusUserStatByUserId(Integer userId);
}
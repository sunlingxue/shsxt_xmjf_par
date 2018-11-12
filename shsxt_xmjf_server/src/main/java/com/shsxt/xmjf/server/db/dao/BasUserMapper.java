package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasUser;

public interface BasUserMapper extends BaseMapper<BasUser>{

    public BasUser queryUserByPhone(String phone);

}
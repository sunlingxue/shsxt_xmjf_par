package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasUserSecurity;

public interface BasUserSecurityMapper extends BaseMapper<BasUserSecurity> {

     public BasUserSecurity quertyBasUserSecurityByUserId(Integer userId);

}
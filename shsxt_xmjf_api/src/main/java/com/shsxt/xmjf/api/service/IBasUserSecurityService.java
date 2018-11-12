package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BasUserSecurity;

public interface IBasUserSecurityService {
    public BasUserSecurity quertyBasUserSecurityByUserId(Integer userId);

    /**
     * 实名认证
     * @param realName
     * @param idCard
     * @param busiPwd
     * @param userId
     * @param confirmPwd
     */
    public void updataBasUserSecurity(String realName,String idCard, String busiPwd, Integer userId, String confirmPwd);
}

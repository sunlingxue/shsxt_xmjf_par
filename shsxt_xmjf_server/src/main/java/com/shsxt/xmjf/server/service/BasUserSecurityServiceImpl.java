package com.shsxt.xmjf.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shsxt.xmjf.api.Utils.AsserUtil;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.server.Aliyun;
import com.shsxt.xmjf.server.db.dao.BasUserSecurityMapper;
import com.shsxt.xmjf.server.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service
public class BasUserSecurityServiceImpl implements IBasUserSecurityService {

    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;


    @Override
    public BasUserSecurity quertyBasUserSecurityByUserId(Integer userId) {

        return basUserSecurityMapper.quertyBasUserSecurityByUserId(userId);
    }

    @Override
    public void updataBasUserSecurity(String realName,String idCard, String busiPwd, Integer userId, String confirmPwd) {
        /**
         * 1、参数校验
         * 2、实名认证
         * 3、执行更新
         */
        checkParams(realName,idCard,busiPwd,confirmPwd);

        BasUserSecurity basUserSecurity = basUserSecurityMapper.quertyBasUserSecurityByUserId(userId);
        AsserUtil.isTrue(basUserSecurity.getRealnameStatus().equals(1),"该用户已实名认证！");

        String param = "realName="+realName+"&cardNo="+idCard;
        String result = Aliyun.APISTORE("http://1.api.apistore.cn/idcard3",param,"a386f386635c4dc89638459e09469710","POST");
        JSONObject jsonObject = (JSONObject) JSON.parse(result);
        AsserUtil.isTrue(jsonObject.getInteger("error_code")!=0,jsonObject.getString("reason"));

        basUserSecurity.setVerifyTime(new Date());
        basUserSecurity.setPaymentPassword(MD5.toMD5(busiPwd));
        basUserSecurity.setRealnameStatus(1);
        basUserSecurity.setIdentifyCard(idCard);
        basUserSecurity.setRealname(realName);
        AsserUtil.isTrue(basUserSecurityMapper.update(basUserSecurity)<1, XmjfConstant.OPS_FAILED_MSG);
        System.out.println("实名认证成功！");
    }

    /**
     * 参数校验
     * @param realName
     * @param idCard
     * @param busiPwd
     * @param confirmPwd
     */
    private void checkParams(String realName, String idCard, String busiPwd, String confirmPwd) {

        AsserUtil.isTrue(StringUtils.isBlank(realName),"真实姓名不能为空！");
        AsserUtil.isTrue(StringUtils.isBlank(idCard),"身份证号不能为空！");
        AsserUtil.isTrue(StringUtils.isBlank(busiPwd),"交易密码不能为空！");
        AsserUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空！");
        AsserUtil.isTrue(idCard.length()!= 18,"身份证号违法！");
        AsserUtil.isTrue(!busiPwd.equals(confirmPwd),"密码不一致！");

    }
}

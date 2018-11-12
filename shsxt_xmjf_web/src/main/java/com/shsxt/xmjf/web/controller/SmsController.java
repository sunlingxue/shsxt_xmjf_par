package com.shsxt.xmjf.web.controller;


import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.service.ISmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class SmsController {


    @Resource
    private ISmsService smsService;

    @RequestMapping("sendSms")
    @ResponseBody
    public ResultInfo sendSms(String phone, String imageCode, Integer type, HttpSession session){
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(imageCode)){
            resultInfo.setCode(300);
            resultInfo.setMsg("图片验证码不能为空！");
            return resultInfo;
        }

        // 获取session 中存储的验证码
        String sessionImageCode = (String) session.getAttribute(XmjfConstant.PICTURE_IMAGE);

        //session 失效
        if (StringUtils.isBlank(sessionImageCode)){
            resultInfo.setCode(300);
            resultInfo.setMsg("图片验证码已失效，请刷新页面！");
            return resultInfo;
        }

        if (!(imageCode.equals(sessionImageCode))){
            resultInfo.setCode(300);
            resultInfo.setMsg("图片验证码不正确！");
            return resultInfo;
        }

        //删除session
        session.removeAttribute(XmjfConstant.PICTURE_IMAGE);

        try {
            smsService.sendSms(phone,type);
        } catch (ParamsException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());

        }catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("短信发送失败!");
        }


        return resultInfo;
    }
}

package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("queryUserByUserId")
    @ResponseBody
    public User queryUserByUserId(Integer userId) {
        return userService.queryUserByUserId(userId);
    }


    @RequestMapping("register")
    @ResponseBody
    public ResultInfo register(String phone, String code, String password) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.saveUser(phone, code, password);
        }catch (ParamsException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("注册失败！");
        }

        return resultInfo;
    }


    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String phone, String password, HttpSession session){
        ResultInfo resultInfo = new ResultInfo();

        try {
            UserModel userModel = userService.userLogin(phone, password);
            session.setAttribute(XmjfConstant.SESSION_USER,userModel);
        }catch (ParamsException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("登录失败！");
        }

        return resultInfo;
    }


    @RequestMapping("quickLogin")
    @ResponseBody
    public ResultInfo quickLogin(String phone,String code){
        ResultInfo resultInfo = new ResultInfo();


        return resultInfo;
    }

}

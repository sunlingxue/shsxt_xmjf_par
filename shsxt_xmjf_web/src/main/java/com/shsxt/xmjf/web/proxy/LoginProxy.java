package com.shsxt.xmjf.web.proxy;


import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.LoginException;
import com.shsxt.xmjf.api.model.UserModel;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Component
@Aspect
public class LoginProxy {

    @Resource
    private HttpSession session;

    /**
     * 定义切入点匹配方法规则定义
     * 匹配规则表达式含义拦截带有com.shsxt.xmjf.web.annotation.RequireLogin注解的所有方法
     */
    @Pointcut("@annotation(com.shsxt.xmjf.web.annotation.RequireLogin)")
    public void cut() {

    }

    /**
     * 声明前置通知并将通知应用到定义的切入点上
     * 目标泪方法执行前执行该通知
     */
    @Before(value = "cut()")
    public void preMethod() {
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        if (null == userModel) {
            throw new LoginException();
        }
    }
}

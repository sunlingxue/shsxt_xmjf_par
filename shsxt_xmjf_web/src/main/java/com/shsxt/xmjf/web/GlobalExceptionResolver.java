package com.shsxt.xmjf.web;

import com.shsxt.xmjf.api.exceptions.LoginException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object handler, Exception ex) {

        ModelAndView mv = new ModelAndView();

        if (ex instanceof LoginException){
            mv.setViewName("redirect:/login");
        }

        return mv;
    }
}

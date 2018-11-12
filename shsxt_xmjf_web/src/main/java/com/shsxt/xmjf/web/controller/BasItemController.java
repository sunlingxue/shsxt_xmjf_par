package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.api.Utils.PageList;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.query.BasItemQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import com.shsxt.xmjf.web.annotation.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller

@RequestMapping("item")
public class BasItemController {

    @Resource
    private IBasItemService basItemService;

    @RequestMapping("index")
    public  String index(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "invest_list";
    }


    @RequestMapping("list")
    @ResponseBody
    public PageList queryBasItemByParams(BasItemQuery basItemQuery){
        return basItemService.queryBasItemByParams(basItemQuery);
    }


    @RequestMapping("updateBasItemStatusToOpen")
    @ResponseBody
    public ResultInfo updateBasItemStatusToOpen(Integer itemId){
        ResultInfo resultInfo = new ResultInfo();


        try {
            basItemService.updateBasItemStatusToOpen(itemId);
        } catch (ParamsException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("项目更新失败！");
        }

        return resultInfo;
    }
}

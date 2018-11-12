package com.shsxt.xmjf.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.shsxt.xmjf.api.Utils.PageList;
import com.shsxt.xmjf.api.constant.AlipayConfig;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.web.annotation.RequireLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.jgss.HttpCaller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("account")
public class AccountController {

    @Resource
    private HttpSession session;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    /**
     * 充值
     *
     * @param request
     * @return
     */
    @RequestMapping("recharge")
    @RequireLogin
    public String recharge(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "recharge";
    }

    @RequestMapping("queryRechargesByParams")
    @ResponseBody
    @RequireLogin
    public PageList queryRechargesByParams(BusAccountRechargeQuery busAccountRechargeQuery,HttpSession session){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        busAccountRechargeQuery.setUserId(userModel.getId());
        return busAccountRechargeService.queryRechageByParams(busAccountRechargeQuery);
    }

    @RequestMapping("setting")
    @RequireLogin
    public String setting(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "setting";
    }

    @RequestMapping("rechargeList")
    @RequireLogin
    public String rechargeList(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "recharge_record";
    }

    @RequestMapping("doRecharge")
    @RequireLogin
    public String doRecharge(BigDecimal amount, String imageCode, String busiPwd, HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());

        if (StringUtils.isBlank(imageCode)) {
            request.setAttribute("msg", "图片验证码不能为空！");
            return "recharge";
        }

        HttpSession session = request.getSession();

        String sessionImageCode = (String) session.getAttribute(XmjfConstant.PICTURE_IMAGE);
        //Session失效
        if (StringUtils.isBlank(sessionImageCode)) {
            request.setAttribute("msg", "图片验证码已失效，请重新刷新页面！");
            return "recharge";
        }

        if (!imageCode.equals(sessionImageCode)) {
            request.setAttribute("msg", "图片验证码输入错误！");
            return "recharge";
        }
        //删除图片验证码
        session.removeAttribute(XmjfConstant.PICTURE_IMAGE);

        UserModel userModel = (UserModel) request.getSession().getAttribute(XmjfConstant.SESSION_USER);

        String result = null;
        try {
            result = busAccountRechargeService.addBusAccountRecharge(amount, busiPwd, userModel.getId());

            request.setAttribute("result", result);
        } catch (ParamsException e) {
            e.printStackTrace();
            request.setAttribute("msg", e.getMsg());
            return "recharge";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", XmjfConstant.OPS_FAILED_MSG);
            return "recharge";
        }
        return "pay";
    }


    /**
     * 同步回调
     *
     * @param request
     * @return
     */
    @RequestMapping("returnUrl")
    @RequireLogin
    public String returnUrlCallback(@RequestParam(name = "out_trade_no") String orderNo,
                                    @RequestParam(name = "trade_no") String tradeNo,
                                    @RequestParam(name = "total_amount") String totalAmount,
                                    @RequestParam(name = "seller_id") String sellId,
                                    @RequestParam(name = "app_id") String appId,
                                    HttpServletRequest request) {

        Boolean signVerified = checkParams(request);

        try {

            //——请在这里编写您的程序（以下代码仅作参考）——
            if (signVerified) {
                /**
                 * 平台中充值业务处理
                 */
                busAccountRechargeService.updateBusAccountRecharge02(orderNo, totalAmount, tradeNo, appId, sellId);

            } else {
                request.setAttribute("msg", "账户充值异常,请联系客服!");
                System.out.println("账户充值异常,请联系客服!");

            }
        } catch (ParamsException e) {
            e.printStackTrace();
            request.setAttribute("msg", e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "账户充值异常,请联系客服!");
        }

        request.setAttribute("msg", "账户充值成功!!!");

        return "result";

    }


    /**
     * 异步回调
     *
     * @param request
     * @return
     */
    @RequestMapping("notifyUrl")
    @RequireLogin
    public String notifyUrlCallback(@RequestParam(name = "out_trade_no") String orderNo,
                                  @RequestParam(name = "trade_no") String tradeNo,
                                  @RequestParam(name = "total_amount") String totalAmount,
                                  @RequestParam(name = "seller_id") String sellId,
                                  @RequestParam(name = "app_id") String appId,
                                  @RequestParam(name = "trade_status") String tradeStatus,
                                  HttpServletRequest request) {

       Boolean signVerified = checkParams(request);
        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if(signVerified) {//验证成功

            if(tradeStatus.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                try {
                    busAccountRechargeService.updateBusAccountRecharge02(orderNo, totalAmount, tradeNo, appId, sellId);
                    request.setAttribute("result","success");
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("result","faild");
                }

            }else if (tradeStatus.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
                request.setAttribute("result","faild");
            }



        }else {//验证失败


            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
            System.out.println("验签失败!");
            request.setAttribute("result", "fail");
        }

        return "pay_result";
    }

    private Boolean checkParams(HttpServletRequest request) {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            params.put(name, valueStr);
        }

        boolean signVerified = false;

        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return signVerified;

    }


    @RequestMapping("index")
    @RequireLogin
    public String index() {
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        System.out.println(userModel);
        System.out.println("账户主页面...");
        return "account";
    }


    @RequestMapping("auth")
    @RequireLogin
    public String auth(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "auth";
    }


    @RequestMapping("doAuth")
    @RequireLogin
    @ResponseBody
    public ResultInfo doAuth(String realName, String idCard, String busiPwd,
                             String confirmPwd, HttpSession session) {
        ResultInfo resultInfo = new ResultInfo();

        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);

        Integer userId = userModel.getId();

        try {
            basUserSecurityService.updataBasUserSecurity(realName, idCard, busiPwd, userId, confirmPwd);
        } catch (ParamsException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());

        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("认证失败！");
        }

        return resultInfo;
    }

}

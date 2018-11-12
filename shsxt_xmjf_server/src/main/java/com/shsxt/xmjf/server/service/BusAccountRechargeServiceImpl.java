package com.shsxt.xmjf.server.service;

import com.alibaba.dubbo.container.page.PageHandler;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.PageHelper;
import com.shsxt.xmjf.api.Utils.AsserUtil;
import com.shsxt.xmjf.api.Utils.PageList;
import com.shsxt.xmjf.api.constant.AlipayConfig;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.enums.OrderStatus;
import com.shsxt.xmjf.api.enums.PayType;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.server.db.dao.*;
import com.shsxt.xmjf.server.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BusAccountRechargeServiceImpl implements IBusAccountRechargeService {

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BusAccountRechargeMapper busAccountRechargeMapper;

    @Resource
    private BasUserMapper basUserMapper;

    @Resource
    private ISmsService smsService;

    @Resource
    private BusAccountMapper busAccountMapper;

    @Resource
    private BusAccountLogMapper busAccountLogMapper;

    @Resource
    private BusUserStatMapper busUserStatMapper;

    @Override
    public String addBusAccountRecharge(BigDecimal amount, String busiPwd, Integer userId) {
        /**
         * 1.基本校验
         *     amount >0
         *     交易密码必须与数据库存储的校验密码一致
         * 2.添加充值记录  bus_account_recharge
         * 3.发起充值请求
         */
        checkParams(amount, busiPwd, userId);

        String orderNo = initBusAccountRecharge(amount, userId);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);


        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderNo;
        //付款金额，必填
        String total_amount = amount.toString();
        //订单名称，必填
        String subject = "PC端用户充值";
        //商品描述，可空
        String body = "PC端用户充值";


        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", out_trade_no);
        map.put("total_amount", total_amount);
        map.put("subject", subject);
        map.put("body", body);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");

        alipayRequest.setBizContent(JSON.toJSONString(map));


        String result = null;
        //请求
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新记录
     * @param orderNo
     * @param totalAmount
     * @param tradeNo
     */
    @Override
    public void updateBusAccountRecharge(String orderNo,String totalAmount,String tradeNo) {
        /**
         * 1.参数基本校验
         *   orderNo:非空  订单记录必须存在
         *   totalAmount:非空  >0  订单金额与充值金额相等
         *   tradeNo:交易流水号 非空
         *
         * 2.订单状态
         *     如果为支付完成状态  方法结束
         *     如果订单状态为审核中状态  执行更新操作
         * 3.执行更新
         *     a.充值记录表更新   bus_account_recharge
         *     b.账户记录更新     bus_account
         *     c.记录账户资金变动日志信息  bus_account_log
         *     d.更新用户统计信息
         *     e.用户积分表更新
         *     f.积分日志表记录添加
         * 4.发送通知短信   smsService   发邮件
         */

        AsserUtil.isTrue(StringUtils.isBlank(orderNo),"订单异常，请联系客服！");
        BusAccountRecharge busAccountRecharge = busAccountRechargeMapper.queryBusAccountRechargeByOrderNo(orderNo);
        AsserUtil.isTrue(null==busAccountRecharge,"订单异常，请联系客服！");
        AsserUtil.isTrue(StringUtils.isBlank(totalAmount),"订单异常，请联系客服！");
        AsserUtil.isTrue(BigDecimal.valueOf(Double.parseDouble(totalAmount)).compareTo(BigDecimal.ZERO)<=0,"订单金额异常，请联系客服！");
        AsserUtil.isTrue(busAccountRecharge.getRechargeAmount().compareTo(BigDecimal.valueOf(Double.parseDouble(totalAmount))) !=0,"订单金额异常，请联系客服！");
        AsserUtil.isTrue(StringUtils.isBlank(tradeNo),"订单异常,请联系客服!");
        if (busAccountRecharge.getStatus().equals(OrderStatus.PAY_SUCCESS)){
            return;
        }

        executeUpdate(orderNo,totalAmount,tradeNo);
        BasUser basUser = basUserMapper.queryById(busAccountRecharge.getUserId());
        //smsService.sendSms(basUser.getMobile(),XmjfConstant.SMS_RECHARGE_NOTIFY_TYPE);



    }

    @Override
    public void updateBusAccountRecharge02(String orderNo,String totalAmount,String tradeNo,String appId,String sellId) {
        AsserUtil.isTrue(StringUtils.isBlank(orderNo),"订单异常，请联系客服！");
        BusAccountRecharge busAccountRecharge = busAccountRechargeMapper.queryBusAccountRechargeByOrderNo(orderNo);
        AsserUtil.isTrue(null==busAccountRecharge,"订单异常，请联系客服！");
        AsserUtil.isTrue(StringUtils.isBlank(totalAmount),"订单异常，请联系客服！");
        AsserUtil.isTrue(BigDecimal.valueOf(Double.parseDouble(totalAmount)).compareTo(BigDecimal.ZERO)<=0,"订单金额异常，请联系客服！");
        AsserUtil.isTrue(busAccountRecharge.getRechargeAmount().compareTo(BigDecimal.valueOf(Double.parseDouble(totalAmount))) !=0,"订单金额异常，请联系客服！");
        AsserUtil.isTrue(StringUtils.isBlank(tradeNo),"订单异常,请联系客服!");
        AsserUtil.isTrue(StringUtils.isBlank(appId)||StringUtils.isBlank(sellId)||appId.equals(AlipayConfig.app_id),"订单异常，请联系客服！");
        if (busAccountRecharge.getStatus().equals(OrderStatus.PAY_SUCCESS)){
            return;
        }
        executeUpdate(orderNo,totalAmount,tradeNo);
        BasUser basUser = basUserMapper.queryById(busAccountRecharge.getUserId());

    }


    /**
     * 充值记录分页查询
     * @param busAccountRechargeQuery
     * @return
     */
    @Override
    public PageList queryRechageByParams(BusAccountRechargeQuery busAccountRechargeQuery) {
        PageHelper.startPage(busAccountRechargeQuery.getPageNum(),busAccountRechargeQuery.getPageSize());
        List<Map<String, Object>> maps = busAccountRechargeMapper.queryRechargesByParams(busAccountRechargeQuery);
        return new PageList(maps);
    }

    private void executeUpdate(String orderNo, String totalAmount, String tradeNo) {
        BusAccountRecharge busAccountRecharge = busAccountRechargeMapper.queryBusAccountRechargeByOrderNo(orderNo);
        busAccountRecharge.setStatus(OrderStatus.PAY_SUCCESS.getType());
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(totalAmount));
        busAccountRecharge.setActualAmount(amount);
        busAccountRecharge.setAuditTime(new Date());
        busAccountRecharge.setBusiNo(tradeNo);
        AsserUtil.isTrue(busAccountRechargeMapper.update(busAccountRecharge)<1,XmjfConstant.OPS_FAILED_MSG);

        BusAccount busAccount = busAccountMapper.queryBusAccountByUserId(busAccountRecharge.getUserId());
        busAccount.setTotal(busAccount.getTotal().add(amount));
        busAccount.setUsable(busAccount.getUsable().add(amount));
        busAccount.setCash(busAccount.getCash().add(amount));
        AsserUtil.isTrue(busAccountMapper.update(busAccount)<1,XmjfConstant.OPS_FAILED_MSG);

        BusAccountLog busAccountLog = new BusAccountLog();
        busAccountLog.setUsable(busAccount.getUsable());
        busAccountLog.setUserId(busAccountRecharge.getUserId());
        busAccountLog.setWait(busAccount.getWait());
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setOperType("用户充值");
        busAccountLog.setBudgetType(1);//收入
        busAccountLog.setRepay(busAccount.getRepay());
        busAccountLog.setRemark("用户充值操作");
        busAccountLog.setOperMoney(amount);
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setAddtime(new Date());
        busAccountLog.setCash(busAccount.getCash());
        AsserUtil.isTrue(busAccountLogMapper.save(busAccountLog)<1,XmjfConstant.OPS_FAILED_MSG);

        BusUserStat busUserStat = busUserStatMapper.queryBusUserStatByUserId(busAccountRecharge.getUserId());
        busUserStat.setRechargeCount(busUserStat.getRechargeCount()+1);
        busUserStat.setRechargeAmount(busUserStat.getRechargeAmount().add(amount));
        AsserUtil.isTrue(busUserStatMapper.update(busUserStat)<1,XmjfConstant.OPS_FAILED_MSG);


    }

    /**
     * 添加充值记录
     *
     * @param amount
     * @param userId
     */
    private String initBusAccountRecharge(BigDecimal amount, Integer userId) {

        BusAccountRecharge busAccountRecharge = new BusAccountRecharge();

        busAccountRecharge.setUserId(userId);

        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        busAccountRecharge.setOrderNo(orderNo);
        busAccountRecharge.setStatus(OrderStatus.PAY_CHECKING.getType());
        busAccountRecharge.setRechargeAmount(amount);
        busAccountRecharge.setResource("PC端用户充值操作");
        busAccountRecharge.setType(PayType.PC.getType());
        busAccountRecharge.setAddtime(new Date());
        AsserUtil.isTrue(busAccountRechargeMapper.save(busAccountRecharge) < 1, XmjfConstant.OPS_FAILED_MSG);

        return orderNo;

    }

    /**
     * 参数校验
     *
     * @param amount
     * @param buisPwd
     * @param userId
     */
    private void checkParams(BigDecimal amount, String buisPwd, Integer userId) {

        AsserUtil.isTrue(null == amount, "充值金额为空！");
        AsserUtil.isTrue(amount.compareTo(BigDecimal.ZERO) <= 0, "充值金额非法");
        AsserUtil.isTrue(StringUtils.isBlank(buisPwd), "交易密码为空");
        BasUserSecurity basUserSecurity = basUserSecurityService.quertyBasUserSecurityByUserId(userId);
        AsserUtil.isTrue(!basUserSecurity.getRealnameStatus().equals(1), "请先进行实名认证！");
        AsserUtil.isTrue(!basUserSecurity.getPaymentPassword().equals(MD5.toMD5(buisPwd)), "交易密码输入错误！");

    }

}

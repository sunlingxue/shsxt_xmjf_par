package com.shsxt.xmjf.server.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.shsxt.xmjf.api.Utils.AsserUtil;
import com.shsxt.xmjf.api.Utils.RandomCodesUtils;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.server.db.dao.BasUserMapper;
import com.shsxt.xmjf.server.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@Service
public class SmsServiceImpl implements ISmsService {

    @Resource
    private BasUserMapper basUserMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void sendSms(String phone, Integer type) {


        /**
         * 参数校验
         */
        checkParams(phone,type);

        String code = RandomCodesUtils.createRandom(true,4);

        if (type == XmjfConstant.SMS_REGISTER_TYPE){
            //校验手机号是否注册
            AsserUtil.isTrue(null!= basUserMapper.queryUserByPhone(phone),"该手机号已注册");
            doSms(phone,XmjfConstant.SMS_REGISTER_TEMPLATE_CODE,code);

            /**
             * 将code存入radis
             */

            String key = "phone::"+phone+"::templateCode::"+XmjfConstant.SMS_REGISTER_TEMPLATE_CODE+"::code::"+code;
            redisTemplate.opsForValue().set(key,code,180, TimeUnit.SECONDS);


        }else if(type == XmjfConstant.SMS_LOGIN_TYPE){

            doSms(phone,XmjfConstant.SMS_LOGIN_TEMPLATE_CODE,code);

            /**
             * 将code存入radis
             */

            String key = "phone::"+phone+"::templateCode::"+XmjfConstant.SMS_LOGIN_TEMPLATE_CODE+"::code::"+code;
            redisTemplate.opsForValue().set(key,code,180, TimeUnit.SECONDS);

        }else if(type == XmjfConstant.SMS_REGISTER_NOTIFY_TYPE){
            doSms(phone,XmjfConstant.SMS_REGISTER_NOTIFY_TEMPLATE_CODE,"");
        }else if(type == XmjfConstant.SMS_RECHARGE_NOTIFY_TYPE){
            doSms(phone,XmjfConstant.SMS_RECHARGE_NOTIFY_TEMPLATE_CODE,"");
        }else {
            System.out.println("类型错误");
        }



    }

    private void doSms(String phone, String templateCode, String code) {
        try {
            //设置超时时间-可自行调整
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//初始化ascClient需要的几个参数
            final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
            final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
//替换成你的AK
            final String accessKeyId =XmjfConstant.SMS_KEY_ID;//你的accessKeyId,参考本文档步骤2
            final String accessKeySecret = XmjfConstant.SMS_KEY_SECRET;//你的accessKeySecret，参考本文档步骤2
//初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(XmjfConstant.SMS_SIGN_NAME);
            //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            Map<String,String> map=new HashMap<String,String>();

            if (StringUtils.isNotBlank(code)){
                map.put("code",code);
            }
            request.setTemplateParam(JSON.toJSONString(map));
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //request.setOutId("yourOutId");
//请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            System.out.println(sendSmsResponse.getCode());

            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功

                System.out.println("发送成功！");
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 参数校验
     * @param phone
     * @param type
     */
    private void checkParams(String phone, Integer type) {

        /**
         *  1、参数非空
         *      phone type
         *  2、手机号是否合法
         *      长度11位 格式
         *  3、type 合法性校验
         *      1-注册
         *      2-登录
         *  4、手机号唯一
         */

        PhoneUtil.checkPhone(phone);
        AsserUtil.isTrue(!(type == XmjfConstant.SMS_REGISTER_TYPE || type == XmjfConstant.SMS_LOGIN_TYPE ||
                type==XmjfConstant.SMS_RECHARGE_NOTIFY_TYPE || type==XmjfConstant.SMS_REGISTER_NOTIFY_TYPE),"短信类型非法");

    }

    public static void main(String[] args) {
        System.out.println(isMobile("18844197942"));
    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches(XmjfConstant.REGEX_MOBILE, mobile);
    }
}

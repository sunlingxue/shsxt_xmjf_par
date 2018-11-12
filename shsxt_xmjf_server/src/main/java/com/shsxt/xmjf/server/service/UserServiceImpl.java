package com.shsxt.xmjf.server.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.shsxt.xmjf.api.Utils.AsserUtil;
import com.shsxt.xmjf.api.Utils.RandomCodesUtils;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.server.db.dao.*;
import com.shsxt.xmjf.server.utils.MD5;
import com.shsxt.xmjf.server.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private BasUserMapper basUserMapper;

    @Resource
    private BasUserInfoMapper basUserInfoMapper;

    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;

    @Resource
    private BasExperiencedGoldMapper basExperiencedGoldMapper;

    @Resource
    private ISmsService smsService;

    @Override
    public User queryUserByUserId(Integer userId) {
        return userDao.queryUserByUserId(userId);
    }

    @Override
    public void saveUser(String phone, String code, String password) {
        /**
         *  1.参数校验
         *     phone:非空 格式合法 手机号唯一
         *     password:非空  长度至少六位
         *     code:手机验证码非空  有效
         * 2.用户信息初始化
         *       bas_user	用户基本信息
         bas_user_info	用户信息扩展表记录添加
         bas_user_security	用户安全信息表
         bus_account	用户账户表记录信息
         bus_user_integral	用户积分记录
         bus_income_stat	用户收益表记录
         bus_user_stat	用户统计表
         bas_experienced_gold	注册体验金表
         sys_log     系统日志
         3.注册成功 给用户发送手机通知短信
         */

        checkParams(phone,code,password);

        AsserUtil.isTrue(null != basUserMapper.queryUserByPhone(phone),"该手机号已注册！");

        Integer userId = initBasUser(phone,password);

        initBasUserInfo(userId,phone);

        initBasUserSecurity(userId);

        initBasExperiencedGold(userId);


        /*
        发送短信通知注册成功
        */
        smsService.sendSms(phone,XmjfConstant.SMS_REGISTER_NOTIFY_TYPE);
    }

    @Override
    public UserModel userLogin(String phone, String password) {
        checkUserLoginParams(phone , password);
        BasUser basUser = basUserMapper.queryUserByPhone(phone);
        AsserUtil.isTrue(null == basUser,"该手机号还未注册，请先进行注册！");
        AsserUtil.isTrue(!(basUser.getPassword().equals(MD5.toMD5(password+basUser.getSalt()))),"密码输入错误！");
        return buildUserModelInfo(basUser);
    }

    @Override
    public UserModel quickLogin(String phone, String code) {

        checkQuickLoginParams(phone,code);

        BasUser basUser = basUserMapper.queryUserByPhone(phone);

        AsserUtil.isTrue(null == basUser,"该手机号还未注册！");

        UserModel userModel = buildUserModelInfo(basUser);

        return userModel;
    }

    /**
     * 快速登录参数校验
     * @param phone
     * @param code
     */
    private void checkQuickLoginParams(String phone, String code) {
        PhoneUtil.checkPhone(phone);
        String key = "phone::"+phone+"::templateCode::"+XmjfConstant.SMS_LOGIN_TEMPLATE_CODE+"::code::"+code;

        AsserUtil.isTrue(StringUtils.isBlank(code),"验证码为空");

        AsserUtil.isTrue(!redisTemplate.hasKey(key),"验证码已失效!");

        String redisCode = (String) redisTemplate.opsForValue().get(key);

        AsserUtil.isTrue(!redisCode.equals(code),"验证码输入错误！");

    }

    /**
     * UserModel创建
     * @param basUser
     * @return
     */
    private UserModel buildUserModelInfo(BasUser basUser) {
        UserModel userModel = new UserModel();
        userModel.setId(basUser.getId());
        userModel.setPhone(basUser.getMobile());
        userModel.setUserName(basUser.getUsername());
        return userModel;
    }

    /**
     * 登录参数校验
     * @param phone
     * @param password
     */
    private void checkUserLoginParams(String phone, String password) {
        PhoneUtil.checkPhone(phone);
        AsserUtil.isTrue(StringUtils.isBlank(password),"请输入密码！");
    }

    /**
     * 注册体验金
     * @param userId
     */
    private void initBasExperiencedGold(Integer userId) {
        BasExperiencedGold basExperiencedGold = new BasExperiencedGold();
        basExperiencedGold.setUserId(userId);
        basExperiencedGold.setAddtime(new Date());
        basExperiencedGold.setAmount(BigDecimal.valueOf(2888));
        basExperiencedGold.setExpiredTime(DateUtil.offsetDay(new Date(),15));//15天后过期
        basExperiencedGold.setGoldName("注册体验金");
        basExperiencedGold.setStatus(2);
        basExperiencedGold.setUsefulLife(15);
        basExperiencedGold.setWay("注册获取");
        basExperiencedGold.setInvestId(0);
        AsserUtil.isTrue(basExperiencedGoldMapper.save(basExperiencedGold)<1,XmjfConstant.OPS_FAILED_MSG);

    }

    private void initBasUserSecurity(Integer userId) {
        BasUserSecurity basUserSecurity = new BasUserSecurity();
        basUserSecurity.setUserId(userId);
        basUserSecurity.setPhoneStatus(0);
        basUserSecurity.setEmailStatus(0);
        basUserSecurity.setRealnameStatus(0);
        AsserUtil.isTrue(basUserSecurityMapper.save(basUserSecurity)<1,XmjfConstant.OPS_FAILED_MSG);
    }


    private void initBasUserInfo(Integer userId, String phone) {
        BasUserInfo basUserInfo = new BasUserInfo();

        basUserInfo.setUserId(userId);
        /**
         * 邀请码  不能重复
         *   手机号
         *   字母+数字组合  加密
         */
        basUserInfo.setInviteCode(phone);

        AsserUtil.isTrue(basUserInfoMapper.save(basUserInfo)<1,XmjfConstant.OPS_FAILED_MSG);

    }


    /**
     * 用户基本信息初始化
     * @param phone
     * @param password
     */
    private Integer initBasUser(String phone, String password) {
        BasUser basUser = new BasUser();

        basUser.setUsername(phone);
        String salt = RandomCodesUtils.createRandom(false,6);
        basUser.setPassword(MD5.toMD5(password+salt));
        basUser.setSalt(salt);
        basUser.setStatus(1);
        basUser.setType(1);
        basUser.setAddtime(new Date());
        basUser.setTime(new Date());
        basUser.setReferer("PC");
        basUser.setMobile(phone);
        AsserUtil.isTrue(basUserMapper.save(basUser)<1,XmjfConstant.OPS_FAILED_MSG);
        return basUser.getId();


    }

    /**
     * 参数校验
     * @param phone
     * @param code
     * @param password
     */
    private void checkParams(String phone, String code, String password) {

        PhoneUtil.checkPhone(phone);

        String key = "phone::"+phone+"::templateCode::"+ XmjfConstant.SMS_REGISTER_TEMPLATE_CODE+"::code::"+code;

        AsserUtil.isTrue(StringUtils.isBlank(code),"验证码为空");

        AsserUtil.isTrue(!redisTemplate.hasKey(key),"验证码已失效");redisTemplate.hasKey(key);

        String redisCode = (String) redisTemplate.opsForValue().get(key);

        AsserUtil.isTrue(!code.equals(redisCode),"验证码不正确");

        AsserUtil.isTrue(StringUtils.isBlank(password),"密码为空！");

        AsserUtil.isTrue(password.length()<6,"密码长度至少6位！");

    }


}

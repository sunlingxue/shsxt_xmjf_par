package com.shsxt.xmjf.server.utils;

import com.shsxt.xmjf.api.Utils.AsserUtil;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PhoneUtil {
    public static void checkPhone(String phone){
        AsserUtil.isTrue(StringUtils.isBlank(phone),"手机号为空！");
        AsserUtil.isTrue(phone.length()!=11,"手机号格式非法！");
        AsserUtil.isTrue(!(Pattern.matches(XmjfConstant.REGEX_MOBILE, phone)),"手机号格式非法！");
    }
}

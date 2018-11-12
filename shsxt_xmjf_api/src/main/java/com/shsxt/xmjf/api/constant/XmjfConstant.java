package com.shsxt.xmjf.api.constant;

public class XmjfConstant {

    public static final String PICTURE_IMAGE="image";

    public static final int SMS_REGISTER_TYPE=1;
    public static final int SMS_LOGIN_TYPE=2;
    public static final int SMS_REGISTER_NOTIFY_TYPE=3;
    public static final int SMS_RECHARGE_NOTIFY_TYPE=3;
    public static final String SMS_KEY_ID="LTAIftPLNzhgsHmx";
    public static final String SMS_KEY_SECRET="VgLO26MfrjUiEohAZVJ5YfLsjl8p3J";
    public static final String SMS_SIGN_NAME="小马金服";
    public static final String SMS_REGISTER_TEMPLATE_CODE="SMS_109450111";
    public static final String SMS_LOGIN_TEMPLATE_CODE="SMS_115100107";
    public static final String SMS_REGISTER_NOTIFY_TEMPLATE_CODE="SMS_139243932";
    public static final String SMS_RECHARGE_NOTIFY_TEMPLATE_CODE="SMS_139243932";

    public static final String REGEX_MOBILE="^((17[0-9])|(14[0-9])|(13[0-9])|(16[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";


    public static final Integer OPS_SUCCESS_CODE=200;
    public static final String OPS_SUCCESS_MSG="success";
    public static final Integer OPS_FAILED_CODE=300;
    public static final String OPS_FAILED_MSG="failed";
    public static final String SESSION_USER="userInfo";
}

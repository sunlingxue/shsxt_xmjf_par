package com.shsxt.xmjf.api.constant;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092200569516";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPWxyR5lB2QlbeLJpOb5Dwd9lhP1DEFhj+gwcgrse6bEZQfEsYXz4JZU7T7l4AySh/HQcPYORHePzKE89wD66apLFGUJHFLVMACDm9uWCCvj1ZHGnsfBMRzXjfZatkUukECVCNHS3Rt53X4YKiV2ZVXo/s70XQMKzVtHVP3JPNiQdLa2FeuSiOuqR3rJ5f5orOcVqFjb58vLAoV59TnF9bOH0PT1u3GrxNlr+EJl7u3WeZYr86Wqu/azCS5Z0+vlfRqiHEujbEatboOYEIjKFQi8/gemmzf2uttTZlnRWLHMUfrkm20qL4QCNRL7jXyvLT1pG8bDVeoTc+ce4zW8pnAgMBAAECggEBAI2m5AYu5FbWmmuS+y6CA1/5rSzwmWgIxc3HvyVGT0kgcCJbjwCYK/6wwkGcCfsQyhlAn5dMkIUnQfKTvKfodwmLrzfhqQiDDr67YgLcyEaooIoLD4eD2nv+4m8sUT0ix+kbIyMtL/dITong5WpwnkfA1sqya/OaBU5tsHWnaczvHIPXCPuXpLqgtygOUFA4tpuUsjOv7thUbFRWfZzM3sYxCvl0TXcc8rBueTvvkKAheVqYgbX4RevBVvmyuwSpkeo1rtyLFrjG+UyxZVP0bgFcMwrAv7WuUDY7btMpQNAbclrs8lspkANidttvoN8w7mVvkpKpoiHrQQLrkyJK6PECgYEAzJBdwF6MBvgd32Tz7F5pt1B2cQ4NrBU0TC9PBfQbpAyew+Qzbfv/xfGF/EsnrYK6HNTIiquMT5BbeYkum3jByorxMEdaL/WzCXA2veyJ0B1NAh5RkGWqsQI/Qsh6jSyJ3Su6UsvDBVccbNc/4IxKVDx5BNWIm3CnY5wNg5az2QUCgYEAs2bUHWOSk5C5bmSF0wlVpZgzSwed6pI21XUpAenq5mJ0PhiWXOWHelfKnFx7vudp1aqnfJ1Qd7KmXCBkN3O5+FpbAvQ5Q74JaWhAK7aOvHZuyS1InWFJ1q4KJOxlVWhWUVvqnThamFIO5v15GyHOGHVcPOeJXOTu9a2uIpfrgXsCgYAbSKIId1PL1in90rQp8Ym+FnBFse8m5YkdJC4xCBbrsXc3f2dQowzBNO5+8PMw1D/IeqToweqr7gAVXLOnrtqCDjgPDWU9AlNHzfyvJi5XYKEVyD9ut/ClPTx8xsp5q9gTdLKcOmsfejfjWG4/n9aGF664c1aODN92jKjMxTvllQKBgADHuLJsBW4o2fWV8FMLnli7AbYY0WH8wspI2SXN+5yG3xSKDNVYli89BH+Lk2owHXC1bkcHdBshJsINzgHDuhJPakfoAfu+btILhwPpplNBfJfZX5B3pNePKDIi6QnnC0ENUkbewN4atT/h6E3q5reKgx0ssJc706Iz4Cb8XCTTAoGAODZh+orahJAo4VmkDG7QB58qYf0YEKagz9TWiBRGPPwTtof+xokfwLlkeWTEjklxaYLHNIRqKqamoOTBeXm8yB+evRqh+WVu8QRA/cEOmWk2LOems84t2LClO+i4MJMbmScZv5hhnpGWmGm8wieLBV+ej8u93AMF2YNMjHteNjI=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw2axhJDFFxjPay2SgC4YwkQlUwgonfi4YnNL8FDYHkzUmmtCBOVisIsPnurUnoI7Vt/ygmzrJqKjIcv5+lWQnbFrKpNqCdEFSKGfBnt2e9vYD7ouTxDrBXKTPLDrNztZ6NrGpdj9Er77rrbFaWssr6QY6uYg74hoCMMzWtkNEVqvycwstzYci5Y1NQzdn/Xo5cr60J2gEx10fjmLiqUMX7DVz/yVh95nh+CmHKOkWHHEaCIpBhLT9sTBg+NjEyh56yco3PgufDZgeuI083OZwJT2Vd0cxUtw+ZY1JOyuGz8AOs3QBRmwfyEcGa2nvstZpUedjJPaCVa/uQH4nsGL2QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://127.0.0.1:5555/account/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://127.0.0.1:5555/account/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

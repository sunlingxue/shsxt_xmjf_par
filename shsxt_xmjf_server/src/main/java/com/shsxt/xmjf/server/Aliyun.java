package com.shsxt.xmjf.server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Aliyun {
	/**
	 * APISTORE_GET
	 * @param strUrl
	 * @param param
	 * @param appcode
	 * @return
	 */
	public static String APISTORE(String strUrl, String param, String appcode, String Method) {
		
		String returnStr = null; // 返回结果定义
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		try {
			url = new URL(strUrl + "?" + param);
			httpURLConnection = (HttpURLConnection) url.openConnection();			
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod(Method); 
			httpURLConnection.setUseCaches(false); // 不用缓存
			httpURLConnection.connect();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			reader.close();
			returnStr = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return returnStr;
	}
	
    
    // 发起请求,获取内容
    public static void main(String[] args) {
        	//请正确填写appcode,如果填写错误阿里云会返回 400错误或403错误
        //appcode查看地址 https://market.console.aliyun.com/imageconsole/
       /* String appcode = "a386f386635c4dc89638459e09469710";
        //请求地址
        String url="http://1.api.apistore.cn/idcard3";
        //请求参数
        String params = "realName=王大锤&cardNo=452617198501100543";*/
        
        //发送请求
       // String result = APISTORE(url, params, appcode,"POST");
        //输出结果
       // System.out.println(result);
        //JSON
        /*JSONObject object = JSONObject.fromObject(result);
        //输出状态码
	    System.out.println(object.getInt("error_code")) ;  
	    //输出返回结果
	    System.out.println(object.get("reason")) ;*/
        String  result="{\"result\":{\"realName\":\"王大锤\",\"cardNo\":\"452617198501100543\"},\"reason\":\"身份证号码格式不正确\",\"error_code\":80004,\"ordersign\":\"20181016110324073029951173\"}";
		JSONObject jsonObject= (JSONObject) JSON.parse(result);
		System.out.println(jsonObject.getInteger("error_code"));
		System.out.println(jsonObject.getString("reason"));

	}

}



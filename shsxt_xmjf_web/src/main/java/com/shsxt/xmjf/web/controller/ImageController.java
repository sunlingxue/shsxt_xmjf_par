package com.shsxt.xmjf.web.controller;

import com.google.code.kaptcha.Producer;
import com.shsxt.xmjf.api.constant.XmjfConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 *生成图片验证码
 */
@Controller
public class ImageController {

    @Resource
    private Producer producer;

    @RequestMapping("image")
    public void getImage(HttpServletRequest request, HttpServletResponse response){
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        String code = producer.createText();
        System.out.println("验证码:"+code);

        //将验证码存入session
        request.getSession().setAttribute(XmjfConstant.PICTURE_IMAGE,code);

        BufferedImage bi = producer.createImage(code);

        OutputStream os = null;

        try {
            os = response.getOutputStream();
            ImageIO.write(bi,"jpg",os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

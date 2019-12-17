package com.zhm.controller;

import com.zhm.util.websocketModel;
import com.zhm.websocket.MyWebSocket;
import com.zhm.websocket.QRCodeGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Created by 赵红明 on 2019/11/20.
 */
@Api(value = "扫码登录相关",description = "扫码登录相关")
@RestController
@RequestMapping(value = {"/api/scan"})
public class ScanController {

    private static Logger logger=LoggerFactory.getLogger(ScanController.class);
    @Autowired
    private MyWebSocket myWebSocket;
    //存储二维码维一标识
    public static Set<String> tokes = new HashSet<>();

    @Autowired
    private Environment environment;

    @ApiOperation(value = "生成的登录二维码",notes = "生成的登录二维码",httpMethod = "GET")
    @RequestMapping(value = "/proCode",method = RequestMethod.GET)
    public void  createQRCode(HttpServletResponse httpServletResponse)throws Exception{
        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        System.out.println("生成二维码!");
        //生成唯一ID
        String uuid = request.getParameter("uuid");
        tokes.add(uuid);
        logger.info("uuid=====>"+uuid);
        //二维码内容,在手机上登录的是一个是否在手机上确认登录的地址（在app内）。
        String url=environment.getProperty("mfw.authority.appAffirmurl");

        String encoderContent=url+uuid+"?date="+(new Date()).getTime();

        logger.info("perfactUrl====>"+encoderContent);
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        BufferedImage twoDimensionImg = new QRCodeGenerator().qRCodeCommon(encoderContent,350,350);
        ImageIO.write(twoDimensionImg, "jpg", jpegOutputStream);
        response.addHeader("Content-Disposition", "attachment;filename=" + new String((uuid+".jpg").getBytes()));
        byte[] bys = jpegOutputStream.toByteArray();
        response.addHeader("Content-Length", "" + bys.length);
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(bys);
        responseOutputStream.flush();
        responseOutputStream.close();
        websocketModel wm = new websocketModel();
        wm.setType("overdue");
        wm.setUid(uuid);
        myWebSocket.sendInfo(wm,uuid);//向前端发送消息
    }

}

package com.zhm.controller;

import com.zhm.dto.HttpDto;
import com.zhm.dto.server.Sys;
import com.zhm.entity.SysUser;
import com.zhm.http.OkhttpUtil;
import com.zhm.http.OriginalHttp;
import com.zhm.service.LoginService;
import com.zhm.service.SysUserService;
import com.zhm.util.*;
import com.zhm.websocket.MyWebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import okhttp3.internal.http2.Header;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/11/6.
 *
 */
@Api(value = "对外接口",description = "对外接口")
@RestController
@RequestMapping(value = "/openApi/interface")
public class OpenController {

    private static Logger logger= LoggerFactory.getLogger(OpenController.class);


    static final MD5Util md5Util = new MD5Util();

    @Autowired
    private MyWebSocket myWebSocket;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LoginService loginService;
    /**
     * 该接口主要用于给外部需要MFW系统信息的用户，提供用户接口。
     * @param username
     * @param password
     * @return
     */
    @CrossOrigin(origins = "*")
    @ApiOperation(value="获取acessToken")
    @RequestMapping(name = "获取acessToken", value = {"/getToken"}, method = RequestMethod.GET)
    public Result token(String username, String password){
        //校验用户是否存在，如果存在给其提供一个token,需要密码Md5加密
        password = md5Util.StringInMd5(password);
        SysUser sysUser=sysUserService.findUser(username,password);
        if(sysUser!=null){
            LoginUser loginUser=new LoginUser();
            loginUser.setUserId(sysUser.getUserId());
            loginUser.setUserName(username);
            String token= JwtUtil.sign(username,password,loginUser);
            return Result.sendSuccess("获取token成功",token);
        }else{
            return Result.sendFailure("用户名或者密码错误");
        }
    }

    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取菜单接口(view页面使用（接口使用）)")
    @RequestMapping(value = "/menu",method= RequestMethod.GET)
    public Result menu()throws Exception{
        LoginUser loginUser=AuthorityUtils.getInterfaceCurrentUser();
        return  sysUserService.queryUserMenuForView(loginUser.getUserId());

    }

    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取用户信息（接口使用）")
    @RequestMapping(value = "/userInfo",method= RequestMethod.GET)
    public Result userInfo(){
        LoginUser loginUser=AuthorityUtils.getInterfaceCurrentUser();
        Integer userId=loginUser.getUserId();
        SysUser sysUser=sysUserService.findOne(userId);
        if(sysUser!=null){
            return Result.sendSuccess("查询到用户信息！",sysUser);
        }else{
            return Result.sendFailure("没有查询到用户信息！");
        }
    }
    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取菜单接口(接口使用)")
    @RequestMapping(value = "/permission",method= RequestMethod.GET)
    public Result queryMenu(String menuCode){
        if(menuCode!=null&&!menuCode.trim().equals("")){
            logger.info("查询菜单："+menuCode+"的菜单列表");
        }else{
           return Result.sendFailure("请输入菜单编号（menuCode）");
        }
        LoginUser loginUser=AuthorityUtils.getInterfaceCurrentUser();
        Integer userId=loginUser.getUserId();
        return sysUserService.queryUserInfo(userId,menuCode);
    }

    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "扫码登录(用于二维码扫码登录)",notes = "用手机扫描后，手机上会有一个页面，然后调用扫描登录接口（会把用户token信息传递过来。）")
    @RequestMapping(value = "/qrcodelogin",method= RequestMethod.GET)
    public Result qrcodelogin (String uid)throws Exception{
        logger.info("扫码登录:"+uid);
        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        LoginUser loginUser=AuthorityUtils.getInterfaceCurrentUser();
        websocketModel wm = new websocketModel();
        wm.setType("loginOk");
        wm.setNickName (loginUser.getUserName()); //用户昵称
        wm.setUserId(loginUser.getUserId());
        wm.setUsername(loginUser.getUserName());
        wm.setAvatar(loginUser.getAvataUrl());
        wm.setUid(uid);
        SysUser sysUser=sysUserService.findOne(loginUser.getUserId());
        loginService.login(request,response,loginUser.getUserName(),sysUser.getPassWord());
        myWebSocket.sendInfo(wm,uid);//向前端发送登录成功的消息
        return Result.sendSuccess("扫描登录成功");
    }
    /**
     * 对外展示接口
     */
    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "Http接口模型")
    @RequestMapping(value = "/httpModel",method= RequestMethod.GET)
    public Result httpModel(HttpDto httpDto){
        String info= OriginalHttp.sendGet(httpDto);
       return Result.sendSuccess("success",info);
    }

}

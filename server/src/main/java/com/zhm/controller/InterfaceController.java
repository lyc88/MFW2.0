package com.zhm.controller;

import com.zhm.entity.SysUser;
import com.zhm.service.SysUserService;
import com.zhm.util.JsonUtil;
import com.zhm.util.Result;
import com.zhm.util.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/11/5.
 */
@Api(value = "菜单等接口",description = "菜单等接口")
@RestController
@RequestMapping(value = "/api/interface")
public class InterfaceController {
    private static Logger logger= LoggerFactory.getLogger(InterfaceController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private StringRedisTemplate rt=new StringRedisTemplate();

    @Autowired
    private SysUserService sysUserService;

    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取菜单接口(view页面使用（界面使用）)")
    @RequestMapping(value = "/menu",method= RequestMethod.GET)
    public Result menu(Integer userId, HttpServletRequest request, HttpServletResponse response)throws Exception{
        if(userId!=null){
            return  sysUserService.queryUserMenuForView(userId);
        }else{
            //获取cookie信息
            String cookieName=environment.getProperty("mfw.authority.default.cookieName");
            Cookie cookie = WebUtils.getCookie(request, cookieName);
            if(cookie!=null){
                String loginToken = cookie.getValue();
                logger.info("loginToken"+loginToken);
                String redisName=environment.getProperty("mfw.redis.user.info")+loginToken;
                Map<String, Object> userInfo = this.getUserInfo(redisName);
                userId=Integer.parseInt(userInfo.get("userId").toString());
                return  sysUserService.queryUserMenuForView(userId);
            }else{
                return Result.sendFailure("cookie失效请重新登录");
            }
        }

    }

    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取用户信息（界面使用）")
    @RequestMapping(value = "/userInfo",method= RequestMethod.GET)
    public Result userInfo(HttpServletRequest request){
        String cookieName=environment.getProperty("mfw.authority.default.cookieName");
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if(cookie!=null){
            String loginToken = cookie.getValue();
            logger.info("loginToken"+loginToken);
            String redisName=environment.getProperty("mfw.redis.user.info")+loginToken;
            Map<String, Object> userInfo = this.getUserInfo(redisName);
            Integer userId=Integer.parseInt(userInfo.get("userId").toString());
            SysUser sysUser=sysUserService.findOne(userId);
            if(sysUser!=null){
                return Result.sendSuccess("查询到用户信息！",sysUser);
            }else{
                return Result.sendFailure("没有查询到用户信息！");
            }
        }else{
            return Result.sendFailure("cookie失效请重新登！");
        }
    }
    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取菜单接口(其他系统使用（界面使用）)")
    @RequestMapping(value = "/permission",method= RequestMethod.GET)
    public Result queryMenu(String menuCode,Integer userId,HttpServletRequest request){
        if(menuCode!=null&&!menuCode.trim().equals("")){
            logger.info("查询菜单："+menuCode+"的菜单列表");
        }else{
            menuCode=environment.getProperty("mfw.authority.menuCode");

        }
        //获取cookie信息
        if(userId!=null){
            return sysUserService.queryUserInfo(userId,menuCode);
        }else{
            String cookieName=environment.getProperty("mfw.authority.default.cookieName");
            Cookie cookie = WebUtils.getCookie(request, cookieName);
            if(cookie!=null){
                logger.info("系统："+menuCode+"开始获取菜单");
                String loginToken = cookie.getValue();
                logger.info("loginToken"+loginToken);
                String redisName=environment.getProperty("mfw.redis.user.info")+loginToken;
                Map<String, Object> userInfo = this.getUserInfo(redisName);
                userId=Integer.parseInt(userInfo.get("userId").toString());
                return sysUserService.queryUserInfo(userId,menuCode);
            }else{
                return Result.sendFailure("cookie失效请重新登！");
            }
        }

    }


    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "获取系统信息")
    @RequestMapping(value = "/systemInfo",method= RequestMethod.GET)
    public Result systemInfo(HttpServletRequest request)throws Exception{
        Server server = new Server();
        server.copyTo();
        return Result.sendSuccess("查询系统信息成功",server);
    }
    public Map<String,Object> getUserInfo(String loginToken){
        String userInfoJson = rt.opsForValue().get(loginToken);
        logger.debug("redis.userInfoJson : " + userInfoJson);
        if(userInfoJson!=null){
            Map<String,Object> map= JsonUtil.fromJson(userInfoJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
            if(map!=null){
                return map;
            }
        }
        return null;
    }

}

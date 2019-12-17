package com.zhm.controller;

import com.zhm.annotation.SysLogEntity;
import com.zhm.dto.CheckUserDto;
import com.zhm.dto.SendEmailDto;
import com.zhm.dto.UpdatePassword;
import com.zhm.dto.UpdateUserDto;
import com.zhm.entity.SysUser;
import com.zhm.service.CommonService;
import com.zhm.service.LoginService;
import com.zhm.service.SysUserService;
import com.zhm.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 赵红明 on 2019/11/1.
 */
@Api(value = "登陆，注册接口",description = "登陆，注册接口")
@RestController
@RequestMapping(value = "/api/login")
public class LoginController {
    private static Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private Environment environment;

    @Autowired
    private StringRedisTemplate rt=new StringRedisTemplate();

    @Autowired
    private LoginService loginService;

    @Autowired
    private CommonService commonService;

    static final MD5Util md5Util=new MD5Util();


    @SysLogEntity(value = "系统登录日志")
    @ApiOperation(value = "登陆接口")
    @RequestMapping(value ="",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,Object> loginParam, HttpServletRequest request, HttpServletResponse response){
        logger.info("进入登录校验阶段");
        //用户名
        String userName=(String)loginParam.get("username");
        //密码
        String passWord=(String)loginParam.get("password");
        //验证码
        String parameter=(String)loginParam.get("vrifyCode");
        String captchaId = (String) request.getSession().getAttribute("vrifyCode");
        logger.info("Session  vrifyCode "+captchaId+" form vrifyCode "+parameter);
        if (!captchaId.equals(parameter)) {
            return Result.sendFailure("错误的验证码");
        }
        return loginService.login(request,response,userName,passWord);


    }

    @SysLogEntity("注册")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result register(@RequestBody Map<String,Object> loginParam, HttpServletRequest request, HttpServletResponse response){
        try{
            String parameter=(String)loginParam.get("vrifyCode");
            String captchaId = (String) request.getSession().getAttribute("vrifyCode");
            // String parameter = request.getParameter("vrifyCode");
            logger.info("Session  vrifyCode "+captchaId+" form vrifyCode "+parameter);
            if (!captchaId.equals(parameter)) {
                return Result.sendFailure("错误的验证码");
            }
            String userName=(String)loginParam.get("userName");
            String passWord=(String)loginParam.get("passWord");
            logger.info("md5=====>"+md5Util.StringInMd5(passWord));
            String email=(String)loginParam.get("email");
            String mobile=(String)loginParam.get("mobile");
            SysUser sysUser=new SysUser();
            sysUser.setUserName(userName);
            sysUser.setPassWord(passWord);
            sysUser.setEmail(email);
            sysUser.setMobile(mobile);
            sysUser.setStatus(0);
            sysUserService.save(sysUser);
            return Result.sendSuccess("用户注册成功！",null);
        }catch (Exception e){
            logger.error("用户注册异常，{}",e);
            return Result.sendFailure("用户注册失败！",null);
        }

    }

    /**
     * 修改密码
     * @param updateUserDto
     * @return
     */
    @SysLogEntity("修改密码")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Result updatePassword(@RequestBody UpdateUserDto updateUserDto)throws Exception{
        String oldPassword=md5Util.StringInMd5(updateUserDto.getPassWord());
        boolean flag=sysUserService.findUserAndPassword(updateUserDto.getUserName(),oldPassword);
        SysUser sysUser=sysUserService.findUser(updateUserDto.getUserName(),oldPassword);
        if(flag){
            if(updateUserDto.getConpassWord().equals(updateUserDto.getRepassWord())){
                String passWord=md5Util.StringInMd5(updateUserDto.getRepassWord());
                sysUser.setPassWord(passWord);
                sysUserService.save(sysUser);
                return Result.sendSuccess("修密码成功！",null);
            }else{
                return Result.sendFailure("输入的密码不相同！");
            }
        }else{
            return Result.sendFailure("输入的原密码错误！");
        }
    }

    /**
     * 发送邮件
     * @param sendEmailDto
     * @return
     */
    @RequestMapping(value = "/sendEmail",method= RequestMethod.POST)
    public Result sendEmail(@RequestBody SendEmailDto sendEmailDto){
        return commonService.sendSimpleMail(sendEmailDto.getToEmail(),sendEmailDto.getUserName(),"找回MyModel账号密码");
    }

    /**
     * 校验是否有验证码，如果成功登陆到填写新密码的页面
     * @return
     */
    @RequestMapping(value = "/searchPassword",method= RequestMethod.POST)
    public Result searchPassword(@RequestBody CheckUserDto checkUserDto){
        return sysUserService.checkRandomCode(checkUserDto);
    }

    /**
     * 忘记密码
     * @param updatePassword
     * @return
     */
    @RequestMapping(value = "/changePassword",method= RequestMethod.POST)
    public Result changePassword(@RequestBody UpdatePassword updatePassword){
        return sysUserService.updatePassword(updatePassword);
    }

    @CrossOrigin(origins = "*")
    @ApiModelProperty(value = "退出登陸")
    @RequestMapping(value = "/exitLogin",method= RequestMethod.GET)
    public Result exitLogin(HttpServletRequest request, HttpServletResponse response){
        String host = request.getHeader("host");// 域名+端口，例如sdisp.seedeer.com:6080
        String domain = StringUtils.getSecondLevelDomain(host);
        //获取cookie信息
        String cookieName=environment.getProperty("mfw.authority.default.cookieName");
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if(cookie!=null){
            //刪除redis
            String loginToken = cookie.getValue();
            logger.info("loginToken"+loginToken);
            String redisName=environment.getProperty("systemmodel.user.info")+loginToken;
            rt.delete(redisName);
            //清空cookie
            cookie.setDomain(domain);
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
            return Result.sendSuccess("退出成功",null);
        }else{
            //登录页面
            return Result.sendFailure("cookie失效请重新登录");
        }
    }



}

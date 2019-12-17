package com.zhm.service;

import com.alibaba.fastjson.JSON;
import com.zhm.controller.LoginController;
import com.zhm.entity.SysUser;
import com.zhm.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by 赵红明 on 2019/11/20.
 */
@Service
public class LoginService {

    private static Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private Environment environment;


    @Autowired
    private StringRedisTemplate rt=new StringRedisTemplate();

    static final MD5Util md5Util=new MD5Util();

    /**
     * 登录
     * @param request
     * @param response
     * @param userName
     * @param passWord
     * @return
     */
    public Result login(HttpServletRequest request, HttpServletResponse response,String userName, String passWord){
        //校验是否存在
        Result result=sysUserService.checkUser(userName,passWord);
        if(result.getCode().equals(Constant.SUCCESS)){
            SysUser sysUser=(SysUser) result.getData();
            String cookieName=environment.getProperty("mfw.authority.default.cookieName");
            //判断cookieName是否配置
            if(cookieName!=null){
                Cookie cookie= WebUtils.getCookie(request,cookieName);
                if(cookie!=null){
                    String redisName=environment.getProperty("mfw.redis.user.info")+cookie.getValue();
                    logger.info("redis中key值是："+redisName);
                    //获取Redis中用户信息
                    String userInfo=rt.opsForValue().get(redisName);
                    //如果Redis中用户信息是空的，将用户信息添加到Redis
                    if(userInfo==null){
                        this.saveCookieAndRedis(request,response,sysUser,cookieName);
                    }
                }else{
                    this.saveCookieAndRedis(request,response,sysUser,cookieName);
                }

            }else{
                logger.info("cookieName不存在！");
                return Result.sendFailure("请登录");
            }
            return Result.sendSuccess("查询到用户信息",sysUser.getUserId());
        }else{
            return result;
        }
    }


    /**
     * 将用户信息添加到Redis
     * @param request
     * @param response
     * @param sysUser
     * @param cookieName
     */
    public void saveCookieAndRedis(HttpServletRequest request,HttpServletResponse response,SysUser sysUser,String cookieName){
        try{
            String host = request.getHeader("host");// 域名+端口，例如www.zhm.com:8080
            String domain = StringUtils.getSecondLevelDomain(host);
            // 时间戳
            long timestamp = System.currentTimeMillis();
            // 用户信息 userCode + timestamp
            String userInfo = sysUser.getUserName() + timestamp;
            // 用户信息密文，也是缓存key
            String cookieInfo = md5Util.StringInMd5(userInfo);
            //设置失效时间
            String cookieExpireTimeStr = (String) request.getAttribute("loginCookieExpireTime");

            Integer cookieExpireTime=0;
            if(cookieExpireTimeStr!=null){
                cookieExpireTime = Integer.valueOf(cookieExpireTimeStr);
            }else{
                cookieExpireTime=2*60*60;//2个小时后才失效
            }

            /**
             * 登录信息存储到Redis中
             */
            //1:redis中用户名称
            String redisName = (String) request.getAttribute("redisName");
            if(redisName==null){
                redisName=environment.getProperty("mfw.redis.user.info")+cookieInfo;
            }
            //2:设置失效时间
            String img="";
            if(sysUser.getAvatarImg()!=null&&!sysUser.getAvatarImg().equals("")){
                img=sysUser.getAvatarImg();
            }else{
                img="https://img-blog.csdnimg.cn/20190829103618321.jpg";
            }
            LoginCache loginCache=new LoginCache(sysUser.getUserId(),sysUser.getUserName(),sysUser.getMobile(), cookieExpireTime+"",img);
            //3：用戶登录信息存储在Redis里
            String jsonInfo= JSON.toJSONString(loginCache);
            rt.opsForValue().set(redisName,jsonInfo);
            //4:在redis中设置失效时间
            rt.expire(redisName,cookieExpireTime , TimeUnit.SECONDS);//设置过期时间
            /**
             * 存在session里的数据用户名，
             */
            LoginUser loginUser=new LoginUser();
            loginUser.setUserId(sysUser.getUserId());
            loginUser.setUserName(sysUser.getUserName());
            AuthorityUtils.setCurrentUser(loginUser);
            /**
             * 用户登录信息token（也就是cookieInfo）存在Cookie里
             *
             */
            Cookie loginCookie = new Cookie(cookieName, cookieInfo);
            //1:跨域共享cookie的方法：设置cookie.setDomain(".jszx.com")
            logger.info("cookie共享的域名："+domain);
            loginCookie.setDomain(domain);
            //2:设置路径
            loginCookie.setPath("/");
            // cookie失效时间，单位为秒
            loginCookie.setMaxAge(cookieExpireTime);
            response.addCookie(loginCookie);
            //登录成功后，将菜单相应

            logger.info("用户："+sysUser.getUserName()+"在"+DateUtil.getNowStringDate()+"登录系统");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("存储到cookie出错",e);
        }

    }

}

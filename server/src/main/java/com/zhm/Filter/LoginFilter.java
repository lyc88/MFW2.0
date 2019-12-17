package com.zhm.Filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zhm.annotation.TokenFilter;
import com.zhm.util.AuthorityUtils;
import com.zhm.util.LoginUser;
import com.zhm.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/11/1.
 * filterName的首字母一定要小写！！！小写！！！小写！！！
 * 如果不小写，会导致配置的多个过滤器拦截url都失效了
 * urlPatterns的意思是过滤一些参数
 */
@WebFilter(urlPatterns = {"/api/*"}, filterName = "loginFilter")
public class LoginFilter implements Filter{

    private static Logger logger= LoggerFactory.getLogger(LoginFilter.class);

    private final Environment environment;

    private final StringRedisTemplate redisTemplate;

    public static final String KEY_USER = "user";


    @Autowired
    private StringRedisTemplate rt=new StringRedisTemplate();
    /**
     * 记录那些方法需要token
     */
    private Map<RequestMappingInfo, HandlerMethod> ignoredRequestMappingInfoMap;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public LoginFilter(Environment environment,StringRedisTemplate redisTemplate){
        this.environment=environment;
        this.redisTemplate=redisTemplate;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("进入LoginFilter拦截器初始化方法内");
        Map<RequestMappingInfo, HandlerMethod> allRequestMappingInfoMap = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : allRequestMappingInfoMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            boolean hasIgnoreFilter = handlerMethod.hasMethodAnnotation(TokenFilter.class);
            if (hasIgnoreFilter) {
                if (ignoredRequestMappingInfoMap == null) {
                    ignoredRequestMappingInfoMap = Maps.newConcurrentMap();
                }
                ignoredRequestMappingInfoMap.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("getFilterName:"+filterConfig.getFilterName());//返回<filter-name>元素的设置值。
        logger.info("getServletContext:"+filterConfig.getServletContext());//返回FilterConfig对象中所包装的ServletContext对象的引用。
        logger.info("getInitParameter:"+filterConfig.getInitParameter("cacheTimeout"));//用于返回在web.xml文件中为Filter所设置的某个名称的初始化的参数值
        logger.info("getInitParameterNames:"+filterConfig.getInitParameterNames());//返回一个Enum

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("进入LoginFilter拦截器");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String servletPath = httpServletRequest.getServletPath();
        logger.info("servletPath : " + servletPath);

        //如果是登录页面则不需要拦截
        if(servletPath.contains("/api/login")){
            chain.doFilter(request, response);
            return;
        }
        //验证码不需要拦截
        if(servletPath.contains("/api/defaultKaptcha")){
            chain.doFilter(request, response);
            return;
        }

        //对外接口
        if(servletPath.contains("/api/interface")){
            chain.doFilter(request, response);
            return;
        }
        //扫码登录
        if(servletPath.contains("/api/scan/proCode")){
            chain.doFilter(request, response);
            return;
        }
        //连接控制
        if(servletPath.contains("/api/websocket")){
            chain.doFilter(request, response);
            return;
        }

        //判断方法体(注解TokenFilter)是否需要过滤掉不执行拦截
        HandlerExecutionChain handlerExecutionChain = null;
        try {
            handlerExecutionChain = requestMappingHandlerMapping.getHandler(httpServletRequest);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
        boolean isIgnore = ignore(handlerExecutionChain);
        if (isIgnore) {
            logger.info(servletPath+"不需要拦截");
            chain.doFilter(request, response);
            return;
        }else{
            logger.info(servletPath+"需要拦截");
        }

        /**
         * 配置一些页面参数不需要过滤器处理
         */
        String excludedPatterns = environment.getProperty("mfw.authority.default.excluded_patterns");
        String[] excludedPatternsArray = StringUtils.split(excludedPatterns, ",");

        PathMatcher matcher = new AntPathMatcher();
        for (String excludedPattern : excludedPatternsArray) {
            logger.debug("servletPath : " + servletPath + ", excludedPattern : " + excludedPattern);
            if (matcher.match(excludedPattern, servletPath)) {
                chain.doFilter(request, response);
                return;
            }
        }


        String cookieName=environment.getProperty("mfw.authority.default.cookieName");

        //登录页面
        String loginUrl=environment.getProperty("mfw.authority.loginUrl");
        //判断cookieName是否配置
        if(cookieName!=null){
            Cookie cookie= WebUtils.getCookie(httpServletRequest,cookieName);
            if(cookie!=null){
                String redisName=environment.getProperty("mfw.redis.user.info")+cookie.getValue();
                logger.info("redis中key值是："+redisName);
                //获取Redis中用户信息
                String userInfo=rt.opsForValue().get(redisName);
                //如果Redis中用户信息是空的
                if(userInfo!=null){
                    JSONObject userObj=JSONObject.parseObject(userInfo);
                    LoginUser loginUser=new LoginUser();
                    loginUser.setUserId(Integer.parseInt(userObj.get("userId").toString()));
                    loginUser.setUserName(userObj.get("userName").toString());
                    if(userObj.get("avataUrl")!=null){
                        loginUser.setAvataUrl(userObj.get("avataUrl").toString());
                    }else{
                        loginUser.setAvataUrl("");
                    }
                    // 把用户信息放到log4j
                    MDC.put(KEY_USER, loginUser.getUserName());
                    loginUser.setExpires(userObj.get("expires").toString());
                    AuthorityUtils.setCurrentUser(loginUser);
                }else{
                    logger.info("redis中用户信息不存在！");
                    httpServletResponse.sendRedirect(loginUrl);
                    return;
                }
            }else{
                logger.info("cookie不存在！");
                httpServletResponse.sendRedirect(loginUrl);
                return;
            }
        }else{
            logger.info("cookieName不存在！");
            httpServletResponse.sendRedirect(loginUrl);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("销毁LoginFilter拦截器");
    }

    private boolean ignore(HandlerExecutionChain handlerExecutionChain) {
        HandlerMethod handler;
        try {
            if(handlerExecutionChain == null) {
                return false;
            }
            handler = (HandlerMethod) handlerExecutionChain.getHandler();
            if (handler == null) {
                return false;
            }
        } catch (Exception ignore) {
            return false;
        }
        if(ignoredRequestMappingInfoMap != null && !ignoredRequestMappingInfoMap.isEmpty()) {
            Collection<HandlerMethod> handlerMethods = ignoredRequestMappingInfoMap.values();
            for (HandlerMethod handlerMethod : handlerMethods) {
                if (handler.getBeanType().equals(handlerMethod.getBeanType()) && handler.getMethod().equals(handlerMethod.getMethod())) {
                    return true;
                }
            }
        }
        return false;
    }
}

package com.zhm.Filter;


import com.alibaba.fastjson.JSONObject;
import com.zhm.entity.SysUser;
import com.zhm.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 赵红明 on 2019/11/1.
 * filterName的首字母一定要小写！！！小写！！！小写！！！
 * 如果不小写，会导致配置的多个过滤器拦截url都失效了
 * urlPatterns的意思是过滤一些参数
 */
@WebFilter(urlPatterns = {"/openApi/*"}, filterName = "openFilter")
public class OpenFilter implements Filter{
    private static Logger logger= LoggerFactory.getLogger(OpenFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("进入OpenFilter拦截器初始化方法内");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        logger.info("进入OpenFilter拦截器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        logger.info("servletPath : " + servletPath);
        //获取accessToken不需要校验
        if(servletPath.equals("/openApi/interface/getToken")){
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        String accessToken = ((HttpServletRequest)servletRequest).getHeader("access_token");
        if(accessToken!=null&&!accessToken.equals("")){
            boolean flag = JwtUtil.verify(accessToken);
            if (flag) {
                //获取用户ID
                String userName= JwtUtil.getInfo(accessToken,"userInfo");
                LoginUser loginUser=JsonUtil.fromJson(userName, LoginUser.class);
                AuthorityUtils.setInterfaceCurrentUser(loginUser);
                logger.info("jwt token 校验成功！用户信息："+JsonUtil.toJson(loginUser));
            }else{
                logger.info("jwt token 校验成功！");
                this.sendTokenJson(response,JSONObject.parseObject(JSONObject.toJSONString(Result.sendFailure("access_token校验失败！"))));
                return;
            }
        }else{
            this.sendTokenJson(response,JSONObject.parseObject(JSONObject.toJSONString(Result.sendFailure("请输入acaccess_token！"))));
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("销毁OpenFilter拦截器");
    }

    private void sendTokenJson(ServletResponse response, JSONObject menuJson) throws IOException {
        PrintWriter out = setPrintWriter(response);
        out.print(menuJson);
        out.flush();
        out.close();
    }
    private PrintWriter setPrintWriter(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return response.getWriter();
    }
}

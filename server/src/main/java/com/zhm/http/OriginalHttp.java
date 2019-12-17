package com.zhm.http;

import com.zhm.dto.HttpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/11/11.
 */
public class OriginalHttp {

    private static Logger logger= LoggerFactory.getLogger(OriginalHttp.class);

    /**
     * 向指定URL发送GET方法的请求
     *
     *
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(HttpDto httpDto) {
        String result = "";
        BufferedReader in = null;
        try {
            String url=httpDto.getUrl();
            String param=httpDto.getData().toString();
            String accessToken=httpDto.getAccessToke();
            String urlNameString=null;
            if(param!=null&&!param.equals("")){
                urlNameString= url + "?" + param;
            }else{
                urlNameString= url;
            }

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("access_token", accessToken);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
              logger.info(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.info("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("关闭输入流，{}",e2);
            }
        }
        return result;
    }

    /**
     *
     *
     * 向指定 URL 发送POST方法的请求
     *
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     *            jsonFlag :是否是json格式发送
     *            token：
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(HttpDto httpDto) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            String url=httpDto.getUrl();
            String param=httpDto.getData().toString();
            String contentType=httpDto.getContentType();
            String token=httpDto.getAccessToke();
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("access_token", token);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            //  conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            if(param!=null){
                out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送 POST 请求出现异常！"+e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                logger.error("关闭输入流，{}",ex);
            }
        }
        return result;
    }
}

package com.zhm.util;

/**
 * 一些常量
 * Created by 赵红明 on 2019/10/31.
 */
public interface Constant {
    String SUCCESS="00000";
    String FAILURE="99999";

    /**
     * 配置文件前缀
     */
    interface PropertiesPrefix {

        /**
         * 服务contextPath
         */
        String SERVER_CONTEXTPATH = "server.contextPath";
    }

    /**
     * 父参数节点
     */
    String PARENT_PARAMS = "parent.params";

    /**
     * 子参数节点
     */
    String SON_PARAMS = "son.params";

    String Login_auth="com.zhm.myModel";

    String address_local="com.zhm.address";

    interface Rediskey {
        // redis前缀
        String PREKEY = "mfm.";
    }
    //导出
    public static final String EXCEL_TITLE_PREFIX = "USER";
}

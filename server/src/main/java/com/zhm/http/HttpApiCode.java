package com.zhm.http;

/**
 * http apiId定义
 *
 * @author bob
 * @date 2018/7/9 13:50
 */
public enum HttpApiCode {
    //-------------------------------------------------分隔符 以下为同步仓库接口-----------------------------------------------------------
    //主要菜单
    MAIN_MENU("beginInitialPush", "/api/apiService/receiveHeadOrder", "orderSystem", "beginSystem"),
    //详细菜单
    DETAIL_MENU("transferInitialPush", "/api/apiService/receiveHeadOrder", "orderSystem", "transferSystem"),
    //用户主要信息
    USER_INFO("purposeInitialPush", "/api/apiService/receiveHeadOrder", "orderSystem", "endSystem");


    private String apiId;

    //相对路径
    private String relativePath;

    //发件系统code
    private String sendSys;

    //收件系统code
    private String receiveSys;

    HttpApiCode(String apiId, String relativePath, String sendSys, String receiveSys) {
        this.apiId = apiId;
        this.relativePath = relativePath;
        this.sendSys = sendSys;
        this.receiveSys = receiveSys;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getApiId() {
        return apiId;
    }

    public String getSendSys() {
        return sendSys;
    }

    public String getReceiveSys() {
        return receiveSys;
    }


    /**
     * 根据apiId的值，返回类型的枚举实例。
     *
     * @param apiId
     */
    public static HttpApiCode httpApiCodeByApiId(String apiId) {
        for (HttpApiCode httpApiCode : HttpApiCode.values()) {
            if (httpApiCode.getApiId().equals(apiId)) {
                return httpApiCode;
            }
        }
        return null;
    }

}

package com.zhm.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Http请求Model
 * Created by 赵红明 on 2019/11/11.
 */
public class HttpDto {
    @ApiModelProperty(value = "接口URL")
    private String url;
    @ApiModelProperty(value = "接口token")
    private String accessToke;
    @ApiModelProperty(value = "请求类型contentType")
    private String contentType;
    @ApiModelProperty(value = "请求类型，post,get")
    private String type;
    @ApiModelProperty(value = "请求值")
    private Object data;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessToke() {
        return accessToke;
    }

    public void setAccessToke(String accessToke) {
        this.accessToke = accessToke;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

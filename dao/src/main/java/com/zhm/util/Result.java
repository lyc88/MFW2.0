package com.zhm.util;


import io.swagger.annotations.ApiModelProperty;

/**
 * Created by 赵红明 on 2019/10/31.
 */

public class Result {
    @ApiModelProperty(value = "返回码")
    private String code;
    @ApiModelProperty(value = "返回信息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private Object data;

    public static Result sendSuccess(String message,Object data){
        Result result=new Result();
        result.setCode(Constant.SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    public static Result sendSuccess(String message){
        Result result=new Result();
        result.setCode(Constant.SUCCESS);
        result.setMessage(message);
        return result;
    }
    public static Result sendFailure(String message,Object data){
        Result result=new Result();
        result.setCode(Constant.FAILURE);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    public static Result sendFailure(String message){
        Result result=new Result();
        result.setCode(Constant.FAILURE);
        result.setMessage(message);
        return result;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

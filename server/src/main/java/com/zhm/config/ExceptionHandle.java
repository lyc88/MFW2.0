package com.zhm.config;

import com.zhm.util.Constant;
import com.zhm.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 * Created by 赵红明 on 2019/11/18.
 */
@RestControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception exception){
      return Result.sendSuccess(Constant.FAILURE,exception.getMessage());
    }
}

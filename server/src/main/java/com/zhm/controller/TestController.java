package com.zhm.controller;

import com.zhm.annotation.TokenFilter;
import com.zhm.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 赵红明 on 2019/10/31.
 */
@Api(value = "网站测试接口",description = "网站测试接口")
@RestController
@RequestMapping(value = "/api")
public class TestController {
    @ApiOperation(value = "测试能否返回(不用拦截的)",notes = "这是备注",httpMethod = "GET")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Result test(){
        return Result.sendSuccess("测试不用拦截的成功");
    }
}

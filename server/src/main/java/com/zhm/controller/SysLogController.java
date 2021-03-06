package com.zhm.controller;

import com.zhm.entity.SysLog;
import com.zhm.essential.domain.PageableFactory;
import com.zhm.essential.jpa.search.DynamicSpecifications;
import com.zhm.essential.jpa.search.Operator;
import com.zhm.essential.jpa.search.SearchFilter;
import com.zhm.service.SysLogService;
import com.zhm.util.Result;
import com.zhm.util.Servlets;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 系统日志
 *
 * @author zhao.hongming
 * @email 1183483051@qq.com
 * @date 2019-09-11 09:52:06
 */
@Api(value = "系统日志接口",description = "系统日志接口")
@RestController
@RequestMapping("/api/syslog")
public class SysLogController {

    private static Logger logger= LoggerFactory.getLogger(SysLogService.class);

    @Autowired
    private SysLogService sysLogService;

    /**
    *
    *  分页查询
    * @param
    * @return
    */
    @RequestMapping(name = "分页查询", value = {""}, method = RequestMethod.GET)
    public Page<SysLog> pageForList(WebRequest request, Pageable pageable) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        SearchFilter.addFilter(filters, SearchFilter.build("deleteFlag", Operator.EQ, 0));
        Specification<SysLog> specification = DynamicSpecifications.bySearchFilter(filters);
        pageable = PageableFactory.create(pageable, "createTime", Sort.Direction.DESC);
        Page<SysLog> page = sysLogService.findAll(specification, pageable);
        return page;
    }

    /**
     *
     *  不分页查询
     * @param pageable
     * @return
     */
    @RequestMapping(name = "不分页查询", value = {"/queryList"}, method = RequestMethod.GET)
    public Result queryForList(WebRequest request, Pageable pageable) {
        try{
            Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
            Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
            SearchFilter.addFilter(filters, SearchFilter.build("deleteFlag", Operator.EQ, 0));
            Specification<SysLog> specification = DynamicSpecifications.bySearchFilter(filters);
            List<SysLog> plist = sysLogService.findAll(specification);
            return Result.sendSuccess("查询成功！",plist);
        }catch (Exception e){
            return Result.sendFailure("查询异常");
        }
    }

    /**
     * 新增
     *
     * @param sysLog
     * @return
     */
    @RequestMapping(name = "新增", value = {""}, method = RequestMethod.POST)
    public Result add(@RequestBody SysLog sysLog) throws Exception {
        try{
            sysLogService.save(sysLog);
            return  Result.sendSuccess("新增成功",sysLog);
        }catch (Exception e){
            logger.error("新增失败,{}",e);
            return Result.sendFailure("新增失败："+e.getMessage());
        }
    }
    /**
     * 修改
     *
     * @param sysLog
     * @return
     */
    @RequestMapping(name = "修改", value = {""}, method = RequestMethod.PUT)
    public Result update(@RequestBody SysLog sysLog) throws Exception {
        try{
            sysLogService.save(sysLog);
            return Result.sendSuccess("修改成功！",null);
        }catch (Exception e){
            logger.error("修改失败,{}",e);
            return Result.sendFailure("修改失败："+e.getMessage());
        }
    }

}

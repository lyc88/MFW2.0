package com.zhm.controller;

import com.google.common.collect.Maps;
import com.zhm.annotation.SysLogEntity;
import com.zhm.dto.ExportUser;
import com.zhm.entity.ParamDetail;
import com.zhm.entity.SysUser;
import com.zhm.essential.domain.PageableFactory;
import com.zhm.essential.jpa.search.DynamicSpecifications;
import com.zhm.essential.jpa.search.Operator;
import com.zhm.essential.jpa.search.SearchFilter;
import com.zhm.service.SysUserService;
import com.zhm.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by 赵红明 on 2019/6/26.
 */
@Api(value = "系统用户接口",description = "系统用户接口")
@RestController
@RequestMapping(value = "/api/sysUser")
public class SysUserController {
    private static Logger logger= LoggerFactory.getLogger(SysUserController.class);

    static final MD5Util md5Util=new MD5Util();
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ParamUtil paramUtil;

    @RequestMapping(name = "分页查询实例", value = {""}, method = RequestMethod.GET)
    public Page<SysUser> queryParamsInfo(WebRequest request, Pageable pageable) {
        //过滤掉一些参数
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        //默认值
        SearchFilter.addFilter(filters, SearchFilter.build("deleteFlag", Operator.EQ, 0));
        Specification<SysUser> specification = DynamicSpecifications.bySearchFilter(filters);
        //排序
        pageable = PageableFactory.create(pageable, "createTime", Sort.Direction.DESC);
        Page<SysUser> page=sysUserService.findAll(specification, pageable);
        return page;
    }

    /**
     * 新增
     *
     * @param sysUser
     * @return
     */
    @SysLogEntity("新增用户")
    @RequestMapping(name = "新增实例", value = {"/addSysUser"}, method = RequestMethod.POST)
    public Result addReason(@RequestBody SysUser sysUser) throws Exception {
        try{
            sysUser.setCreateTime(new Date());
            String password=sysUser.getPassWord();
            //生成盐值salt
            String salt=returnSalt();
            password=md5Util.StringInMd5(password);
            sysUser.setClientId(md5Util.StringInMd5(sysUser.getUserId()+","+salt));
            sysUser.setPassWord(password);
            sysUserService.save(sysUser);
            return  Result.sendSuccess("新增成功",sysUser);
        }catch (Exception e){
            logger.error("用户新增失败",e);
            return Result.sendFailure("新增失败："+e.getMessage());
        }
    }
    @SysLogEntity("修改用户")
    @RequestMapping(name = "修改实例", value = {"/updateSysUser"}, method = RequestMethod.PUT)
    public Result updateReason(@RequestBody SysUser sysUser) throws Exception {
        try{
            sysUser.setUpdateTime(new Date());
            sysUserService.save(sysUser);
            return  Result.sendSuccess("修改成功",sysUser);
        }catch (Exception e){
            return Result.sendFailure("修改失败："+e.getMessage());
        }
    }

    public String returnSalt(){
        String str = "MM-"+(char)(Math.random()*26+'A');
        Random random = new Random();
        int ends = random.nextInt(99);
        str=str+String.format("%02d",ends);//如果不足两位，前面补0
        //参数length，表示生成几位随机数
        for(int i = 0; i < 4; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                str += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;
    }

    @RequestMapping(value = "/initPassword/{userId}",method = RequestMethod.GET)
    public Result PasswordInit(@PathVariable Integer userId){
        try{
            SysUser sysUser=sysUserService.findOne(userId);
            if(sysUser!=null){
                ParamDetail initParam= paramUtil.getParamDetail("initParam","initPassword");
                String initPassword="123456";
                if(initParam!=null){
                   initPassword= initParam.getParamDetailCname();
                }
               String password=md5Util.StringInMd5(initPassword);
                sysUser.setPassWord(password);
                sysUserService.save(sysUser);
                return Result.sendSuccess("初始化密码成功",null);
            }else{
                return Result.sendFailure("没有查询到系统用户");
            }

        }catch (Exception e){
            logger.error("系统异常，",e);
            return Result.sendFailure("系统异常："+e.getCause());
        }
    }
   @RequestMapping(value = "/queryUserRoles/{userId}",method = RequestMethod.GET)
    public Result queryUserRoles(@PathVariable Integer userId){

        return sysUserService.queryUserRoles(userId);

    }

    @ApiOperation(value = "上传文件(头像)")
    @RequestMapping(name = "上传文件", value = {"/uploadCompress"}, method = RequestMethod.POST)
    public Result uploadCompress(@RequestParam(value = "file") MultipartFile file) throws Exception {
        return sysUserService.uploadCompress(file);
    }

    @ApiOperation(value = "查看注册数量")
    @RequestMapping(name = "查看注册数量", value = {"/registerNum"}, method = RequestMethod.GET)
    public Result registerNum(){
        List<Map<String,String>> list=sysUserService.registerNum();
        List<String> month=new ArrayList<>();
        List<String> nums=new ArrayList<>();
        for(Map<String,String> map:list){
            month.add(map.get("months"));
            nums.add(map.get("count"));
        }
        Map<String,Object> rmap=new HashMap<>();
        rmap.put("month",month);
        rmap.put("nums",nums);
        return Result.sendSuccess("查询成功！",rmap);
    }
    @ApiOperation(value = "查看操作数据")
    @RequestMapping(name = "查看操作数据", value = {"/findOperation"}, method = RequestMethod.GET)
    public Result findOperation(){
        List<Map<String,String>> list=sysUserService.findOperation();
        List<String> operation=new ArrayList<>();
        for(Map<String,String> map:list){
            operation.add(map.get("name"));
        }
        Map<String,Object> rmap=new HashMap<>();
        rmap.put("operation",operation);
        rmap.put("list",list);
        return Result.sendSuccess("查询成功！",rmap);
    }

    @ApiOperation(value = "导出用户详情")
    @RequestMapping(name = "导出用户详情", value = {"/download"}, method = RequestMethod.GET)
    public void download(WebRequest request, HttpServletResponse response) throws Exception {
        //过滤掉一些参数
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        //默认值
        SearchFilter.addFilter(filters, SearchFilter.build("deleteFlag", Operator.EQ, 0));
        Specification<SysUser> specification = DynamicSpecifications.bySearchFilter(filters);
        List<SysUser> userList = sysUserService.findAll(specification);
        /**
         * 原始导出的excel
         */
      /*  String fileName = Constant.EXCEL_TITLE_PREFIX + "_" + DateUtil.getNowShortStringDate() + ".xls";
        String[] headers = {"登录名", "性别", "邮箱", "手机号", "头像", "创建时间"};
        String[] fieldNames = {"userName", "sex", "email", "mobile", "avatarImg", "createTime"};
        new ExportExcel<SysUser>().exportExcel(fileName, headers, userList, fieldNames, response, null);*/
        /**
         * 复杂导出
         */
        new ExportExcel<SysUser>().exportUserList(response,userList);

    }
    @ApiOperation(value = "导出用户-角色-菜单详情")
    @RequestMapping(name = "导出用户-角色-菜单详情", value = {"/downloadAll"}, method = RequestMethod.GET)
    public void downloadAll(WebRequest request, HttpServletResponse response) throws Exception {
        //过滤掉一些参数
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        //默认值
        SearchFilter.addFilter(filters, SearchFilter.build("deleteFlag", Operator.EQ, 0));
        Specification<SysUser> specification = DynamicSpecifications.bySearchFilter(filters);
        List<SysUser> userList = sysUserService.findAll(specification);
        List<ExportUser> exportUsers=new ArrayList<>();
        if(userList!=null&&userList.size()>0){
            for(SysUser sysUser:userList){

                //查询出用于与角色，菜单关系。并且查出主要信息
                List<Map<String,String>> list= sysUserService.findUserAll(sysUser.getUserId());
                for(Map<String,String> map:list){
                    ExportUser exportUser=new ExportUser();
                    exportUser.setCreatTime(DateUtil.dateToString(sysUser.getCreateTime()));
                    exportUser.setUserName(sysUser.getUserName());
                    exportUser.setMobile(sysUser.getMobile());
                    exportUser.setMenuCode(map.get("menu_code"));
                    exportUser.setMenuName(map.get("menu_name"));
                    exportUser.setRoleCode(map.get("role_code"));
                    exportUser.setRoleName(map.get("role_name"));
                    exportUsers.add(exportUser);
                }
            }
        }

        new ExportExcel<ExportUser>().exportUseAll(response,exportUsers);

    }
    @ApiOperation(value = "查询所有用户")
    @RequestMapping(name = "查询所有用户", value = {"/users"}, method = RequestMethod.GET)
    public Result queryAllUsers(){
       List<SysUser> sysUsers=sysUserService.findUsers();
       return Result.sendSuccess("查询成功",sysUsers);

    }
}

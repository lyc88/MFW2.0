package com.zhm.service;

import com.google.common.collect.Maps;
import com.zhm.dao.RandomCodeDao;
import com.zhm.dao.SysMenuDao;
import com.zhm.dao.SysUserDao;
import com.zhm.dao.SysUserRoleDao;
import com.zhm.dto.CheckUserDto;
import com.zhm.dto.MenuDto;
import com.zhm.dto.UpdatePassword;
import com.zhm.entity.*;
import com.zhm.essential.jpa.search.SearchFilter;
import com.zhm.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by 赵红明 on 2019/11/1.
 */
@Service
public class SysUserService {
    private static Logger logger = LoggerFactory.getLogger(SysUserService.class);
    @Autowired
    private SysUserDao sysUserDao;

    static final MD5Util md5Util = new MD5Util();

    @Autowired
    private RandomCodeDao randomCodeDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysMenuDao sysMenuDao;
    /**
     * 分页查询列表
     *
     * @param specification
     * @param pageable
     * @return
     */
    public Page<SysUser> findAll(Specification<SysUser> specification, Pageable pageable) {
        return sysUserDao.findAll(specification, pageable);
    }

    /**
     * 查询列表根据某个排序
     *
     * @param specification
     * @param sort
     * @return
     */
    public List<SysUser> findAll(Specification<SysUser> specification, Sort sort) {
        return sysUserDao.findAll(specification, sort);
    }

    /**
     * 查询列表
     * @param specification
     * @return
     */
    public List<SysUser> findAll(Specification<SysUser> specification) {
        return sysUserDao.findAll(specification);
    }

    public SysUser findOne(Integer id) {
        return sysUserDao.findById(id).get();
    }

    /**
     * 新增与修改
     *
     * @param sysUser
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUser sysUser) {
        LoginUser loginUser = AuthorityUtils.getCurrentUser();
        Integer userId = 1;
        if (loginUser != null) {
            userId = loginUser.getUserId();
        }
        if (sysUser.getUserId() != null) {
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateUser(userId + "");
            //逻辑删除用户和角色关系
            sysUserRoleDao.updateUserRole(sysUser.getUserId());
        } else {
            sysUser.setCreateTime(new Date());
            sysUser.setCreateUser(userId + "");
        }
        addUserRole(sysUser);
        sysUserDao.save(sysUser);
    }

    /**
     * 添加用户和角色关系
     *
     * @param sysUser
     */
    public void addUserRole(SysUser sysUser) {
        if (sysUser.getRoleIds() != null && sysUser.getRoleIds().size() > 0) {
            for (Integer roleId : sysUser.getRoleIds()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getUserId());
                sysUserRole.setRoleId(roleId);
                sysUserRoleDao.save(sysUserRole);
            }
        }
    }

    /**
     * 根据用户名和密码查询用户信息
     *
     * @param userName
     * @param passWord
     * @return
     */
    public SysUser findUser(String userName, String passWord) {
        List<SysUser> sysUsers = sysUserDao.findByUserNameAndPassWordAndStatusAndDeleteFlag(userName, passWord, 0, 0);
        if (sysUsers != null && sysUsers.size() > 0) {
            return sysUsers.get(0);
        } else {
            return null;
        }
    }

    public Result checkUser(String userName, String passWord) {
        List<SysUser> sysUsers = sysUserDao.findByUserNameAndDeleteFlag(userName, 0);
        if (sysUsers != null && sysUsers.size() > 0) {
            SysUser users = findUser(userName, passWord);
            if (users != null) {
                return Result.sendSuccess("查询到用户", users);
            } else {
                return Result.sendFailure("用户" + userName + "的密码不存在！");
            }
        } else {
            return Result.sendFailure("用户" + userName + "不存在，请重新注册！");
        }

    }

    public Result checkRandomCode(CheckUserDto checkUserDto) {
        //1:校验用户是否存在
        List<SysUser> sysUsers = sysUserDao.findByUserNameAndDeleteFlag(checkUserDto.getUserName(), 0);
        SysUser sysUser = null;
        if (sysUsers != null && sysUsers.size() > 0) {
            sysUser = sysUsers.get(0);
            logger.info("用户存在");
        } else {
            return Result.sendFailure("登陆的用户：" + checkUserDto.getUserName() + "不存在");
        }
        //2:看Random是否存在，并且是否有效期内。

        List<RandomCode> randomCodes = randomCodeDao.findByUserIdAndRandomCodeAndDeleteFlag(sysUser.getUserId(), checkUserDto.getCode(), 0);

        Collections.sort(randomCodes, new Comparator<RandomCode>() {
            public int compare(RandomCode o1, RandomCode o2) {
                //按照RandomCode的createTime字段进行降序排列
                if (o1.getCreateTime().getTime() < o2.getCreateTime().getTime()) {
                    return 1;
                }
                if (o1.getCreateTime().getTime() == o2.getCreateTime().getTime()) {
                    return 0;
                }
                return -1;
            }
        });
        if (randomCodes != null && randomCodes.size() > 0) {
            RandomCode randomCode = randomCodes.get(0);
            long nTime = (new Date()).getTime();
            long cTime = randomCode.getCreateTime().getTime();
            if (nTime - cTime < randomCode.getPastTime()) {
                logger.info("验证码有效");
            } else {
                //查询出来的所有验证码失效
                for (RandomCode rdCode : randomCodes) {
                    rdCode.setUpdateUser(checkUserDto.getUserName());
                    rdCode.setUpdateTime(new Date());
                    rdCode.setDeleteFlag(0);
                    randomCodeDao.save(rdCode);
                }
                return Result.sendFailure("验证码：" + checkUserDto.getCode() + "已失效请重新发送！");
            }
        } else {
            return Result.sendFailure("验证码：" + checkUserDto.getCode() + "不存在！");
        }
        return Result.sendSuccess("成功", null);
    }

    public Result updatePassword(UpdatePassword updatePassword) {
        try {
            List<SysUser> sysUsers = sysUserDao.findByUserNameAndDeleteFlag(updatePassword.getUserName(), 0);
            SysUser sysUser = null;
            if (sysUsers != null && sysUsers.size() > 0) {
                sysUser = sysUsers.get(0);
                logger.info("用户存在");
            } else {
                return Result.sendFailure("登陆的用户：" + updatePassword.getUserName() + "不存在");
            }
            //修改密码，新密码MD5加密
            String passWord = md5Util.StringInMd5(updatePassword.getNewpassword());
            sysUser.setPassWord(passWord);
            sysUser.setUpdateUser(sysUser.getUserName());
            sysUser.setUpdateTime(new Date());
            sysUserDao.save(sysUser);
            return Result.sendSuccess("修改密码成功", null);
        } catch (Exception e) {
            logger.error("修改密码失败{}", e.getMessage());
            return Result.sendFailure("修改密码失败：原因" + e.getCause());
        }
    }

    public boolean findUserAndPassword(String userName, String passWord) {
        List<SysUser> sysUsers = sysUserDao.findByUserNameAndPassWordAndStatusAndDeleteFlag(userName, passWord, 0, 0);
        if (sysUsers != null && sysUsers.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * view界面的菜单
     *
     * @param userId
     * @return
     */
    public Result queryUserMenuForView(Integer userId) {
        //根据用户id查询角色信息，根据角色信息查询菜单信息
        List<SysMenu> sysMenus = sysUserDao.queryForUserMenuForView(userId);
        List<MenuDto> parentlist = this.combianMenuDto(sysMenus);
        return Result.sendSuccess("menuInfo", parentlist);
    }

    /**
     * 根据userId查询角色Id
     *
     * @return
     */
    public Result queryUserRoles(Integer userId) {
        List<Integer> users = sysUserRoleDao.queryChecKUserRole(userId);
        return Result.sendSuccess("查询用户和角色关系成功", users);
    }

    /**
     * 各种系统中的菜单
     *
     * @param userId
     * @return
     */
    public Result queryUserInfo(Integer userId, String menuCode) {
        //根据用户id查询角色信息，根据角色信息查询菜单信息
        List<SysMenu> sysMenus = sysUserDao.queryForUserMenu(userId, menuCode);
        List<MenuInfo> parentlist = this.queryMenuInfo(sysMenus,menuCode);
        return Result.sendSuccess("menuInfo", parentlist);
    }

    public List<MenuInfo> queryMenuInfo(List<SysMenu> sysMenus,String menuCode) {
        Map<Integer, MenuInfo> map = new HashMap<Integer, MenuInfo>();
        List<MenuInfo> parentlist = new ArrayList<MenuInfo>();
        try {
            boolean flag = true;
            if (sysMenus != null && sysMenus.size() > 0) {
                for (SysMenu pm : sysMenus) {
                    //查询其父亲节点
                    MenuInfo pmodel = map.get(pm.getParentId());
                    if (pmodel != null) {
                        //存到父亲里面
                        List<MenuInfo> pmmlist = pmodel.getChildren();
                        if (pmmlist == null) {
                            pmmlist = new ArrayList<>();
                        }
                        MenuInfo productModel = new MenuInfo();
                        productModel.setMenuCname(pm.getMenuName());
                        productModel.setMenuEname(pm.getMenuEname());
                        productModel.setMenuCode(pm.getMenuCode());
                        productModel.setMenuLevel(pm.getMenuLevel());
                        if (pm.getMenuType() == 0) {
                            productModel.setLeafFlag(0);
                        } else if (pm.getMenuType() == 1) {
                            productModel.setLeafFlag(0);
                        } else {
                            productModel.setLeafFlag(1);
                        }
                        productModel.setMenuLink(pm.getMenuUrl());
                        pmmlist.add(productModel);
                        pmodel.setChildren(pmmlist);
                        //存到map中
                        map.put(pm.getId(), productModel);
                    } else {
                        //根路径
                        List<MenuInfo> plist = new ArrayList<MenuInfo>();
                        MenuInfo pmmodel = new MenuInfo();
                        pmmodel.setMenuCname(pm.getMenuName());
                        pmmodel.setMenuEname(pm.getMenuEname());
                        pmmodel.setMenuCode(pm.getMenuCode());
                        if(pm.getMenuLevel()<=1){
                            flag=false;
                        }
                        pmmodel.setLeafFlag(0);
                        pmmodel.setMenuLevel(pm.getMenuLevel());
                        pmmodel.setMenuLink(pm.getMenuUrl());
                        pmmodel.setChildren(plist);
                        map.put(pm.getId(), pmmodel);
                        parentlist.add(pmmodel);
                    }
                }
                //没有系统级别的菜单
                if(flag){
                    List<SysMenu> menus=sysMenuDao.findByMenuCodeAndDeleteFlag(menuCode,0);
                    if(menus!=null&&menus.size()>0){
                        SysMenu menu=menus.get(0);
                        List<MenuInfo> plist = new ArrayList<MenuInfo>();
                        MenuInfo pmmodel = new MenuInfo();
                        pmmodel.setMenuCname(menu.getMenuName());
                        pmmodel.setMenuEname(menu.getMenuEname());
                        pmmodel.setMenuCode(menu.getMenuCode());
                        pmmodel.setLeafFlag(0);
                        pmmodel.setMenuLevel(menu.getMenuLevel());
                        pmmodel.setMenuLink(menu.getMenuUrl());
                        pmmodel.setChildren(parentlist);
                        plist.add(pmmodel);
                        return plist;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return parentlist;
    }

    /**
     * 组合menuList
     *
     * @param sysMenus
     * @return
     */
    public List<MenuDto> combianMenuDto(List<SysMenu> sysMenus) {
        //封装Map
        Map<Integer, MenuDto> map = new HashMap<Integer, MenuDto>();
        List<MenuDto> parentlist = new ArrayList<MenuDto>();
        try {
            if (sysMenus != null && sysMenus.size() > 0) {
                for (SysMenu pm : sysMenus) {
                    if (pm.getMenuLevel() == 1) {
                        //根路径
                        List<MenuDto> plist = new ArrayList<MenuDto>();
                        MenuDto pmmodel = new MenuDto();
                        pmmodel.setId(pm.getId());
                        pmmodel.setMenuName(pm.getMenuName());
                        pmmodel.setMenuEname(pm.getMenuEname());
                        pmmodel.setMenuCode(pm.getMenuCode());
                        pmmodel.setMenuType(pm.getMenuType());
                        pmmodel.setMenuLevel(pm.getMenuLevel());
                        pmmodel.setMenuUrl(pm.getMenuUrl());
                        pmmodel.setParentName("无");
                        pmmodel.setChildren(plist);
                        map.put(pm.getId(), pmmodel);
                        parentlist.add(pmmodel);
                    } else {
                        //查询其父亲节点
                        MenuDto pmodel = map.get(pm.getParentId());
                        if (pmodel != null) {
                            //存到父亲里面

                            List<MenuDto> pmmlist = pmodel.getChildren();
                            if (pmmlist == null) {
                                pmmlist = new ArrayList<>();
                            }
                            MenuDto productModel = new MenuDto();
                            productModel.setMenuName(pm.getMenuName());
                            productModel.setMenuEname(pm.getMenuEname());
                            productModel.setMenuCode(pm.getMenuCode());
                            productModel.setMenuLevel(pm.getMenuLevel());
                            productModel.setId(pm.getId());
                            productModel.setMenuType(pm.getMenuType());
                            productModel.setMenuUrl(pm.getMenuUrl());
                            productModel.setParentName(pmodel.getMenuName());
                            pmmlist.add(productModel);
                            pmodel.setChildren(pmmlist);
                            //存到map中
                            map.put(pm.getId(), productModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentlist;
    }

    /**
     * 上传用户头像
     * @param multipartFile
     * @return
     */
    public Result uploadCompress(MultipartFile multipartFile) {
        Result result = new Result();
        try {
            String fileName = multipartFile.getOriginalFilename();
            List<ParamDetail> paramDetails= ParamUtil.getParamDetailList("loadParam");
            String localUrl="";
            String exitUrl="";
            if(paramDetails!=null&&paramDetails.size()>0){
                for(ParamDetail paramDetail:paramDetails){
                    if(paramDetail.getParamDetailCode().equals("localUrl")){
                        localUrl=paramDetail.getParamDetailCname();
                    }else if(paramDetail.getParamDetailCode().equals("exitUrl")){
                        exitUrl=paramDetail.getParamDetailCname();
                    }
                }
            }else{
                return Result.sendFailure("请配置系统参数");
            }

            String pname = (new Date()).getTime()+ fileName;
            String waterPname= "water"+(new Date()).getTime()+ fileName;
            String filePath = localUrl + pname;
            File desFile = new File(filePath);
            if (!desFile.getParentFile().exists()) {
                desFile.mkdirs();
            }
            multipartFile.transferTo(desFile);
            String endInfo = localUrl + pname;
            logger.info("图片物理地址=" + endInfo);
            //水印操作
            String waterPic= localUrl+waterPname;
            (new WaterMarkUtils()).mark(endInfo,waterPic,"来源：MFW");
            String compressPath = exitUrl + waterPname;
            if (compressPath != null && compressPath != "") {
                result.setCode("00000");
                result.setMessage("图片上传成功");
                result.setData(compressPath);
            } else {
                result.setCode("99999");
                result.setMessage("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getCause()+","+e.getMessage());
            result.setCode("99999");
            result.setMessage("上传失败");
            logger.error("上传失败", e);
        }
        return result;
    }

    public List<Map<String,String>> findUserAll(Integer id){
        return sysUserDao.findUserAll(id);
    }
    public List<Map<String, String>> registerNum() {
        return sysUserDao.findRegister();
    }
    public List<Map<String, String>> findOperation() {
        return sysUserDao.findOperation();
    }

    public List<SysUser> findUsers(){
        return sysUserDao.findByStatusAndDeleteFlag(0,0);
    }
}

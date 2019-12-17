package com.zhm.dao;

import com.zhm.entity.SysMenu;
import com.zhm.entity.SysUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/11/1.
 */
public interface SysUserDao extends PagingAndSortingRepository<SysUser,Integer>,JpaSpecificationExecutor<SysUser>{
    List<SysUser> findByUserNameAndPassWordAndStatusAndDeleteFlag(String userName, String passWord, Integer status, Integer deleteFlag);
    List<SysUser> findByUserNameAndDeleteFlag(String userName,Integer deleteFlag);
    List<SysMenu> queryForUserMenu(Integer userId, String menuCode);
    List<SysMenu> queryForUserMenuForView(Integer userId);

    @Query(value = "select DATE_FORMAT(create_time,'%Y-%m') months,count(user_id) count from sys_user group by months", nativeQuery = true)
    List<Map<String, String>> findRegister();

    @Query(value = "select operation as name,count(username) as value from sys_log group by operation", nativeQuery = true)
    List<Map<String, String>> findOperation();

    @Query(value = "SELECT sm.menu_code,sm.menu_name,sr.role_code,sr.role_name FROM\tsys_user_role sur Left join sys_role sr on sur.role_id=sr.id LEFT JOIN sys_role_menu srm ON sur.role_id = srm.role_id LEFT JOIN sys_menu sm ON sm.id = srm.menu_id WHERE\tsur.delete_flag = 0 AND srm.delete_flag = 0 AND sur.delete_flag = 0 and sr.delete_flag=0 AND sm.delete_flag = 0 AND sm. STATUS = 0 AND sur.user_id=:userId", nativeQuery = true)
    List<Map<String, String>> findUserAll(@Param("userId") Integer userId);


    List<SysUser> findByStatusAndDeleteFlag(Integer status,Integer deleteFlag);
}

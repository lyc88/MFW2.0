package com.zhm.dao.impl;

import com.zhm.entity.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/8/5.
 */
public class SysUserDaoImpl {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<SysMenu> queryForUserMenu(Integer userId, String menuCode){
        //查询字段
        StringBuilder contentBuilder = new StringBuilder("select sm.* from sys_user_role sur left join sys_role_menu srm on sur.role_id=srm.role_id " +
                "left join sys_menu sm on sm.id=srm.menu_id where sur.delete_flag=0 and srm.delete_flag=0 and " +
                "sur.delete_flag=0 and sm.delete_flag=0 and sm.status=0");
        if(userId!=0){
            contentBuilder.append(" and sur.user_id="+userId);
        }
        if(userId!=0){
            contentBuilder.append(" and sm.menu_code like '"+menuCode.trim()+"%'");
        }
        contentBuilder.append(" ORDER BY sm.menu_level asc,sm.menu_type asc,sm.menu_order asc");
        List<SysMenu> list = jdbcTemplate.query(contentBuilder.toString(), (rs, rowNum) -> {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setId(rs.getInt("id"));
            sysMenu.setMenuName(rs.getString("menu_name"));
            sysMenu.setMenuEname(rs.getString("menu_ename"));
            sysMenu.setMenuCode(rs.getString("menu_code"));
            sysMenu.setMenuType(rs.getInt("menu_type"));
            sysMenu.setMenuLevel(rs.getInt("menu_level"));
            sysMenu.setParentId(rs.getInt("parent_id"));
            sysMenu.setMenuUrl(rs.getString("menu_url"));
            return sysMenu;
        });
        return list;
    }

    public List<SysMenu> queryForUserMenuForView(Integer userId){
        //查询字段
        StringBuilder contentBuilder = new StringBuilder("select * from sys_menu where menu_code in(select distinct(substring_index(menu_code,0,5)) from sys_user_role sur left join sys_role_menu srm on sur.role_id=srm.role_id " +
                "left join sys_menu sm on sm.id=srm.menu_id where sur.delete_flag=0 and srm.delete_flag=0 and " +
                "sur.delete_flag=0 and sm.delete_flag=0 ");
        if(userId!=0){
            contentBuilder.append("and sur.user_id="+userId+")");
        }
        List<SysMenu> list = jdbcTemplate.query(contentBuilder.toString(), (rs, rowNum) -> {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setId(rs.getInt("id"));
            sysMenu.setMenuEname(rs.getString("menu_ename"));
            sysMenu.setMenuName(rs.getString("menu_name"));
            sysMenu.setMenuCode(rs.getString("menu_code"));
            sysMenu.setMenuType(rs.getInt("menu_type"));
            sysMenu.setMenuLevel(rs.getInt("menu_level"));
            sysMenu.setParentId(rs.getInt("parent_id"));
            sysMenu.setMenuUrl(rs.getString("menu_url"));
            return sysMenu;
        });
        return list;
    }



}

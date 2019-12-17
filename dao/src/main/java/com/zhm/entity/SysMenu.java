package com.zhm.entity;

import com.zhm.ListenerEntity.ListenerEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by 赵红明 on 2019/8/1.
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu extends ListenerEntity {
    @ApiModelProperty(value = "系统菜单主键")
    private Integer id;
    @ApiModelProperty(value = "父菜单主键")

    private Integer parentId;
    @ApiModelProperty(value = "系统菜单编号")

    private String menuCode;
    @ApiModelProperty(value = "系统菜单级别")

    private Integer menuLevel;
    @ApiModelProperty(value = "系统菜单中文名称")

    private String menuName;
    @ApiModelProperty(value = "系统菜单英文名称")

    private String menuEname;
    @ApiModelProperty(value = "系统菜单链接")

    private String menuUrl;
    @ApiModelProperty(value = "系统菜单类型0：系统，1：菜单2：按钮")

    private Integer menuType;
    @ApiModelProperty(value = "系统菜单顺序")

    private Integer menuOrder;
    @ApiModelProperty(value = "系统菜单状态")

    private Integer status;
    private String createUser;
    private String updateUser;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "parent_id", nullable = true)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "menu_code", nullable = true, length = 50)
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Basic
    @Column(name = "menu_level", nullable = true)
    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

    @Basic
    @Column(name = "menu_name", nullable = true, length = 100)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "menu_ename", nullable = true, length = 100)
    public String getMenuEname() {
        return menuEname;
    }

    public void setMenuEname(String menuEname) {
        this.menuEname = menuEname;
    }

    @Basic
    @Column(name = "menu_url", nullable = true, length = 255)
    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @Basic
    @Column(name = "menu_order", nullable = true)
    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    @Basic
    @Column(name = "menu_type", nullable = true)
    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_user", nullable = true, length = 50)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }


    @Basic
    @Column(name = "update_user", nullable = true, length = 50)
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    private String parentName;
    @Transient
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}

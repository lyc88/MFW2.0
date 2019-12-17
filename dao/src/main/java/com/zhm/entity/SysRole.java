package com.zhm.entity;

import com.zhm.ListenerEntity.ListenerEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 赵红明 on 2019/8/1.
 */
@Entity
@Table(name = "sys_role")
public class SysRole extends ListenerEntity {
    @ApiModelProperty(value = "系统角色主键")
    private Integer id;
    @ApiModelProperty(value = "系统角色编号")

    private String roleCode;
    @ApiModelProperty(value = "系统角色名称")

    private String roleName;
    @ApiModelProperty(value = "系统角色状态（0 禁用 1 启用）")

    private Integer status;
    @ApiModelProperty(value = "系统角色备注")

    private String comment;

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
    @Column(name = "role_code", nullable = true, length = 30)
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Basic
    @Column(name = "role_name", nullable = true, length = 30)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
    @Column(name = "comment", nullable = true, length = 255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "create_user", nullable = true, length = 30)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }



    @Basic
    @Column(name = "update_user", nullable = true, length = 30)
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    private List<Integer> menuList;
    @Transient
    public List<Integer> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Integer> menuList) {
        this.menuList = menuList;
    }

    private List<Integer> dataList;
    @Transient
    public List<Integer> getDataList() {
        return dataList;
    }

    public void setDataList(List<Integer> dataList) {
        this.dataList = dataList;
    }
}

package com.zhm.entity;

import com.zhm.ListenerEntity.ListenerEntity;

import javax.persistence.*;

/**
 * Created by 赵红明 on 2019/8/1.
 */
@Entity
@Table(name = "sys_dept")
public class SysDept extends ListenerEntity {
    private Integer deptId;
    private Integer parentId;
    private String name;
    private Integer level;
    private Integer orderNum;
    private Integer status;
    private String createUser;
    private String updateUser;

    @Id
    @Column(name = "dept_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
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
    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "level", nullable = true, length = 2)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "order_num", nullable = true)
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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
    @Column(name = "create_user", nullable = true, length = 40)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }


    @Basic
    @Column(name = "update_user", nullable = true, length = 40)
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

    @Override
    public String toString() {
        return "SysDept{" +
                "deptId=" + deptId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", orderNum=" + orderNum +
                ", status=" + status +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysDept sysDept = (SysDept) o;

        if (deptId != null ? !deptId.equals(sysDept.deptId) : sysDept.deptId != null) return false;
        if (parentId != null ? !parentId.equals(sysDept.parentId) : sysDept.parentId != null) return false;
        if (name != null ? !name.equals(sysDept.name) : sysDept.name != null) return false;
        if (level != null ? !level.equals(sysDept.level) : sysDept.level != null) return false;
        if (orderNum != null ? !orderNum.equals(sysDept.orderNum) : sysDept.orderNum != null) return false;
        if (status != null ? !status.equals(sysDept.status) : sysDept.status != null) return false;
        if (createUser != null ? !createUser.equals(sysDept.createUser) : sysDept.createUser != null) return false;
        if (updateUser != null ? !updateUser.equals(sysDept.updateUser) : sysDept.updateUser != null) return false;
        return parentName != null ? parentName.equals(sysDept.parentName) : sysDept.parentName == null;
    }

    @Override
    public int hashCode() {
        int result = deptId != null ? deptId.hashCode() : 0;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (orderNum != null ? orderNum.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createUser != null ? createUser.hashCode() : 0);
        result = 31 * result + (updateUser != null ? updateUser.hashCode() : 0);
        result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
        return result;
    }
}

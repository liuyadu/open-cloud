package com.github.lyd.base.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源-菜单信息
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Table(name = "base_resource_menu")
public class BaseResourceMenu implements Serializable {
    private static final long serialVersionUID = -4414780909980518788L;
    /**
     * 菜单Id
     */
    @Id
    @Column(name = "menu_id")
    @KeySql(genId = SnowflakeId.class)
    private Long menuId;

    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 父级菜单
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 路径前缀:/,http://,https://
     */
    private String prefix;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 打开方式:_self窗口内,_blank新窗口
     */
    private String target;

    /**
     * 优先级 越小越靠前
     */
    private Integer priority;

    /**
     * 描述
     */
    @Column(name = "menu_desc")
    private String menuDesc;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @Column(name = "is_persist")
    private Integer isPersist;

    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 获取菜单Id
     *
     * @return menu_id - 菜单Id
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单Id
     *
     * @param menuId 菜单Id
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取菜单编码
     *
     * @return menu_code - 菜单编码
     */
    public String getMenuCode() {
        return menuCode;
    }

    /**
     * 设置菜单编码
     *
     * @param menuCode 菜单编码
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    /**
     * 获取菜单名称
     *
     * @return menu_name - 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置菜单名称
     *
     * @param menuName 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取父级菜单
     *
     * @return parent_id - 父级菜单
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父级菜单
     *
     * @param parentId 父级菜单
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * 获取优先级 越小越靠前
     *
     * @return priority - 优先级 越小越靠前
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级 越小越靠前
     *
     * @param priority 优先级 越小越靠前
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}

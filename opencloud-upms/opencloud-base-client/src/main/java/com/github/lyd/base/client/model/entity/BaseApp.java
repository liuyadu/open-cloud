package com.github.lyd.base.client.model.entity;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统应用-基础信息
 * @author liuyadu
 */
@Table(name = "base_app")
public class BaseApp implements Serializable {
    private static final long serialVersionUID = -4606067795040222681L;
    @Id
    @Column(name = "app_id")
    private String appId;

    /**
     * 应用秘钥
     */
    @Column(name = "app_secret")
    private String appSecret;

    /**
     * app类型：server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用
     */
    @Column(name = "app_type")
    private String appType;

    /**
     * 应用图标
     */
    @Column(name = "app_icon")
    private String appIcon;

    /**
     * app名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * app英文名称
     */
    @Column(name = "app_name_en")
    private String appNameEn;
    /**
     * 移动应用操作系统：ios-苹果 android-安卓
     */
    @Column(name = "app_os")
    private String appOs;


    /**
     * 用户ID:默认为0
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户类型:platform-平台 isp-服务提供商 dev-自研开发者
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * app描述
     */
    @Column(name = "app_desc")
    private String appDesc;

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
     * 官方网址
     */
    private String website;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @Column(name = "is_persist")
    private Integer isPersist;

    /**
     * @return app_id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取app名称
     *
     * @return app_name - app名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置app名称
     *
     * @param appName app名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取app英文名称
     *
     * @return app_name_en - app英文名称
     */
    public String getAppNameEn() {
        return appNameEn;
    }

    /**
     * 设置app英文名称
     *
     * @param appNameEn app英文名称
     */
    public void setAppNameEn(String appNameEn) {
        this.appNameEn = appNameEn;
    }

    /**
     * 获取客户端秘钥
     *
     * @return app_secret - 客户端秘钥
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * 设置客户端秘钥
     *
     * @param appSecret 客户端秘钥
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * @return app_type
     */
    public String getAppType() {
        return appType;
    }

    /**
     * @param appType
     */
    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppOs() {
        return appOs;
    }

    public void setAppOs(String appOs) {
        this.appOs = appOs;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
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

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }
}

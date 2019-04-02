package com.github.lyd.msg.client.model;

/**
 * @author woodev
 */
public class SmsNotify extends Notify {

    private static final long serialVersionUID = -8924332753124953766L;
    private String phoneNumber;

    private String templateCode;

    private String params;

    private String signName;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}

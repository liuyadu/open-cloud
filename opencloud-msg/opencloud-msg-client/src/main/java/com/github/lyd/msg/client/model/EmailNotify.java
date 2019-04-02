package com.github.lyd.msg.client.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author woodev
 */
public class EmailNotify extends Notify {

    /**
     * 收件人
     */
    private String to;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 附件
     */
    private List<File> attachments = new ArrayList<>();

    /**
     * 模板文件
     */
    private String templateFile;

    private String template;

    /**
     * 模板的参数集
     */
    private Map<String, Object> templateModels;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public Map<String, Object> getTemplateModels() {
        return templateModels;
    }

    public void setTemplateModels(Map<String, Object> templateModels) {
        this.templateModels = templateModels;
    }
}

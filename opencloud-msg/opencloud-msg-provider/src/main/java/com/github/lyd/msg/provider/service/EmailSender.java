package com.github.lyd.msg.provider.service;

import com.github.lyd.msg.client.model.EmailNotify;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @author woodev
 */
@Slf4j
public class EmailSender {

	private JavaMailSenderImpl javaMailSender;

	private FreeMarkerConfigurer freeMarkerConfigurer;


    /**
     * 发送邮件
     */
    public void sendSimpleMail(EmailNotify params)  {
    	 MimeMessage message = null;
         try {
             message = javaMailSender.createMimeMessage();
             MimeMessageHelper helper = new MimeMessageHelper(message, true);
             helper.setTo(params.getTo());
             helper.setFrom(javaMailSender.getUsername());
             helper.setSubject(params.getTitle());
             helper.setText(params.getContent(), true);
             this.addAttachment(helper,params);
             javaMailSender.send(message);
         } catch (Exception e) {
             log.error("发送邮件异常:",e);
         }
    }

    /**
     * 发送html邮件
     */
    public void sendHtmlMail(EmailNotify params){
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(javaMailSender.getUsername());
            helper.setTo(params.getTo());
            helper.setSubject(params.getTitle());
            helper.setText(params.getContent(), true);
            this.addAttachment(helper,params);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("发送邮件异常:",e);
        }
    }

    /**
     * 发送带附件的邮件
     */
    public void sendAttachmentMail(EmailNotify params) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(javaMailSender.getUsername());
            helper.setTo(params.getTo());
            helper.setSubject(params.getTitle());
            helper.setText(params.getContent(), true);
            this.addAttachment(helper,params);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("发送邮件异常:",e);
        }
    }

    /**
     * 发送模板邮件
     */
    public void sendTemplateMail(EmailNotify params) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(javaMailSender.getUsername());
            helper.setTo(params.getTo());
            helper.setSubject(params.getTitle());
            this.addAttachment(helper,params);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(params.getTemplateFile());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, params.getTemplateModels());
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("发送邮件异常:",e);
        }
    }

    /**
     * 添加附件
     * @param helper
     * @param params
     * @throws MessagingException
     */
    private void addAttachment(MimeMessageHelper helper, EmailNotify params) throws MessagingException {
        if(params.getAttachments() != null){
            List<File> attachments = params.getAttachments();
            for (File file:attachments){
                FileSystemResource attachment = new FileSystemResource(file);
                helper.addAttachment(file.getName(), file);
            }
        }
    }

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public FreeMarkerConfigurer getFreeMarkerConfigurer() {
		return freeMarkerConfigurer;
	}

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}


}

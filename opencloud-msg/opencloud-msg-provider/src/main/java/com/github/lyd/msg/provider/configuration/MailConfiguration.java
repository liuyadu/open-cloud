package com.github.lyd.msg.provider.configuration;

import com.github.lyd.msg.provider.service.EmailSender;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Properties;

/**
 * @author liuyadu
 */
@Configuration
public class MailConfiguration {


    @Bean
    public EmailSender emailSender(FreeMarkerConfigurer freeMarkerConfigurer, MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort().intValue());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        if (!properties.getProperties().isEmpty()) {
            Properties props = new Properties();
            props.putAll(properties.getProperties());
            sender.setJavaMailProperties(props);
        }
        EmailSender mailSender = new EmailSender();
        mailSender.setJavaMailSender(sender);
        mailSender.setFreeMarkerConfigurer(freeMarkerConfigurer);
        return mailSender;
    }
}

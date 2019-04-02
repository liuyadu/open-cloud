package com.github.saas.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 平台基础服务
 * 提供系统用户、权限分配、资源、客户端管理
 *
 * @author liuyadu
 */
@SpringBootApplication
public class TenantDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantDemoApplication.class, args);
    }
}

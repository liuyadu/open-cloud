package com.opencloud.autoconfigure;

import com.google.common.collect.Lists;
import com.opencloud.common.swagger.OpenSwaggerProperties;
import com.opencloud.common.utils.DateUtils;
import com.opencloud.common.utils.RandomValueUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Swagger文档生成配置
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({OpenSwaggerProperties.class})
@ConditionalOnProperty(prefix = "opencloud.swagger2", name = "enabled", havingValue = "true")
@Import({Swagger2DocumentationConfiguration.class})
public class SwaggerAutoConfiguration {
    private OpenSwaggerProperties openSwaggerProperties;
    private static final String SCOPE_PREFIX = "scope.";
    private Locale locale = LocaleContextHolder.getLocale();
    private MessageSource messageSource;

    public SwaggerAutoConfiguration(OpenSwaggerProperties openSwaggerProperties, MessageSource messageSource) {
        this.openSwaggerProperties = openSwaggerProperties;
        this.messageSource = messageSource;
        log.info("SwaggerProperties [{}]", openSwaggerProperties);
    }


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }

    /**
     * 构建全局参数
     * 这里主要针对网关服务外部访问数字验签所需参数
     * 只在网关服务开启{opencloud.resource-server.enabled-validate-sign=true}时生效.
     * 未开启,可以不填写
     *
     * @return
     */
    private List<Parameter> parameters() {
        ParameterBuilder builder = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        builder.name("Authorization").description("公共参数:认证token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false);
        pars.add(builder.build());
        builder.name("clientId").description("公共参数:客户端Id")
                .modelRef(new ModelRef("string")).parameterType("form")
                .required(false);
        pars.add(builder.build());
        builder.name("nonce").description("公共参数:随机字符串")
                .defaultValue(RandomValueUtils.uuid())
                .modelRef(new ModelRef("string")).parameterType("form")
                .required(false);
        pars.add(builder.build());
        builder.name("timestamp").description("公共参数:请求的时间,格式:yyyyMMddHHmmss")
                .defaultValue(DateUtils.getCurrentTimestampStr())
                .modelRef(new ModelRef("string")).parameterType("form")
                .required(false);
        pars.add(builder.build());
        builder.name("signType").description("公共参数:签名方式:MD5(默认)、SHA256.")
                .modelRef(new ModelRef("string")).parameterType("form")
                .allowableValues(new AllowableListValues(Lists.newArrayList(new String[]{"MD5", "SHA256"}), "string"))
                .required(true);
        pars.add(builder.build());
        builder = new ParameterBuilder();
        builder.name("sign").description("公共参数:数字签名")
                .modelRef(new ModelRef("string")).parameterType("form")
                .defaultValue("")
                .required(false);
        pars.add(builder.build());
        return pars;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(openSwaggerProperties.getTitle())
                .description(openSwaggerProperties.getDescription())
                .version("1.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(null, "list", "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }


    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
     * @return
     */
    @Bean
    SecurityScheme securityScheme() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    private List<AuthorizationScope> scopes() {
        List<String> scopes = Lists.newArrayList();
        List list = Lists.newArrayList();
        if (openSwaggerProperties.getScope() != null) {
            scopes.addAll(Lists.newArrayList(openSwaggerProperties.getScope().split(",")));
        }
        scopes.forEach(s -> {
            list.add(new AuthorizationScope(s, messageSource.getMessage(SCOPE_PREFIX + s, null, s, locale)));
        });
        return list;
    }

    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration(openSwaggerProperties.getClientId(),
                openSwaggerProperties.getClientSecret(),
                "realm", openSwaggerProperties.getClientId(),
                "", ApiKeyVehicle.HEADER, "", ",");
    }

    @Bean
    List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
                openSwaggerProperties.getUserAuthorizationUri(),
                openSwaggerProperties.getClientId(), openSwaggerProperties.getClientSecret());
        TokenEndpoint tokenEndpoint = new TokenEndpoint(openSwaggerProperties.getAccessTokenUri(), "access_token");
        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return grantTypes;
    }


}

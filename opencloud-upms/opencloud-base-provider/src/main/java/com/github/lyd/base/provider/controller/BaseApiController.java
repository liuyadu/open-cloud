package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.model.entity.BaseResourceApi;
import com.github.lyd.base.provider.service.BaseResourceApiService;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "系统接口资源管理")
@RestController
public class BaseApiController {
    @Autowired
    private BaseResourceApiService apiService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping(value = "/api")
    public ResultBody<PageList<BaseResourceApi>> getApiList(@RequestParam Map map) {
        BaseResourceApi query = new BaseResourceApi();
        query.setIsOpen(1);
        int openApiCount = apiService.getCount(query);
        return ResultBody.success(apiService.findListPage(new PageParams(map))).putExtra("openApiCount", openApiCount);
    }


    /**
     * 获取所有接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有接口列表", notes = "获取所有接口列表")
    @GetMapping("/api/all")
    public ResultBody<List<BaseResourceApi>> getApiAllList(String serviceId) {
        return ResultBody.success(apiService.findAllList(serviceId));
    }

    /**
     * 获取接口资源
     *
     * @param apiId
     * @return
     */
    @ApiOperation(value = "获取接口资源", notes = "获取接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", required = true, value = "ApiId", paramType = "path"),
    })
    @GetMapping("/api/{apiId}/info")
    public ResultBody<BaseResourceApi> getApi(@PathVariable("apiId") Long apiId) {
        return ResultBody.success(apiService.getApi(apiId));
    }

    /**
     * 添加接口资源
     *
     * @param apiCode   接口编码
     * @param apiName   接口名称
     * @param serviceId 服务ID
     * @param path      请求路径
     * @param status    是否启用
     * @param priority  优先级越小越靠前
     * @param apiDesc   描述
     * @return
     */
    @ApiOperation(value = "添加接口资源", notes = "添加接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiCode", required = true, value = "接口编码", paramType = "form"),
            @ApiImplicitParam(name = "apiName", required = true, value = "接口名称", paramType = "form"),
            @ApiImplicitParam(name = "apiCategory", required = true, value = "接口分类", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", required = true, value = "服务ID", paramType = "form"),
            @ApiImplicitParam(name = "path", required = false, value = "请求路径", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "apiDesc", required = false, value = "描述", paramType = "form"),
            @ApiImplicitParam(name = "isOpen", required = false, defaultValue = "0", allowableValues = "0,1", value = "是否开放接口", paramType = "form"),
            @ApiImplicitParam(name = "isAuth", required = false, defaultValue = "0", allowableValues = "0,1", value = "是否身份认证", paramType = "form")
    })
    @PostMapping("/api/add")
    public ResultBody<Long> addApi(
            @RequestParam(value = "apiCode") String apiCode,
            @RequestParam(value = "apiName") String apiName,
            @RequestParam(value = "apiCategory") String apiCategory,
            @RequestParam(value = "serviceId") String serviceId,
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "apiDesc", required = false, defaultValue = "") String apiDesc,
            @RequestParam(value = "isOpen", required = false, defaultValue = "0") Integer isOpen,
            @RequestParam(value = "isAuth", required = false, defaultValue = "1") Integer isAuth
    ) {
        BaseResourceApi api = new BaseResourceApi();
        api.setApiCode(apiCode);
        api.setApiName(apiName);
        api.setApiCategory(apiCategory);
        api.setServiceId(serviceId);
        api.setPath(path);
        api.setStatus(status);
        api.setPriority(priority);
        api.setApiDesc(apiDesc);
        api.setIsOpen(isOpen);
        api.setIsAuth(isAuth);
        Long apiId = null;
        BaseResourceApi result = apiService.addApi(api);
        if (result != null) {
            apiId = result.getApiId();
            // 刷新网关
            openRestTemplate.refreshGateway();
        }
        return ResultBody.success(apiId);
    }

    /**
     * 编辑接口资源
     *
     * @param apiId     接口ID
     * @param apiCode   接口编码
     * @param apiName   接口名称
     * @param serviceId 服务ID
     * @param path      请求路径
     * @param status    是否启用
     * @param priority  优先级越小越靠前
     * @param apiDesc   描述
     * @return
     */
    @ApiOperation(value = "编辑接口资源", notes = "编辑接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", required = true, value = "接口Id", paramType = "form"),
            @ApiImplicitParam(name = "apiCode", required = true, value = "接口编码", paramType = "form"),
            @ApiImplicitParam(name = "apiName", required = true, value = "接口名称", paramType = "form"),
            @ApiImplicitParam(name = "apiCategory", required = true, value = "接口分类", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", required = true, value = "服务ID", paramType = "form"),
            @ApiImplicitParam(name = "path", required = false, value = "请求路径", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "apiDesc", required = false, value = "描述", paramType = "form"),
            @ApiImplicitParam(name = "isOpen", required = false, defaultValue = "0", allowableValues = "0,1", value = "是否开放接口", paramType = "form"),
            @ApiImplicitParam(name = "isAuth", required = false, defaultValue = "0", allowableValues = "0,1", value = "是否身份认证", paramType = "form")
    })
    @PostMapping("/api/update")
    public ResultBody updateApi(
            @RequestParam("apiId") Long apiId,
            @RequestParam(value = "apiCode") String apiCode,
            @RequestParam(value = "apiName") String apiName,
            @RequestParam(value = "apiCategory") String apiCategory,
            @RequestParam(value = "serviceId") String serviceId,
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "apiDesc", required = false, defaultValue = "") String apiDesc,
            @RequestParam(value = "isOpen", required = false, defaultValue = "0") Integer isOpen,
            @RequestParam(value = "isAuth", required = false, defaultValue = "1") Integer isAuth
    ) {
        BaseResourceApi api = new BaseResourceApi();
        api.setApiId(apiId);
        api.setApiCode(apiCode);
        api.setApiName(apiName);
        api.setApiCategory(apiCategory);
        api.setServiceId(serviceId);
        api.setPath(path);
        api.setStatus(status);
        api.setPriority(priority);
        api.setApiDesc(apiDesc);
        api.setIsOpen(isOpen);
        api.setIsAuth(isAuth);
        apiService.updateApi(api);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 移除接口资源
     *
     * @param apiId
     * @return
     */
    @ApiOperation(value = "移除接口资源", notes = "移除接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", required = true, value = "ApiId", paramType = "form"),
    })
    @PostMapping("/api/remove")
    public ResultBody removeApi(
            @RequestParam("apiId") Long apiId
    ) {
        apiService.removeApi(apiId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }
}

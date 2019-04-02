package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.model.BaseOperationAuthority;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.client.model.entity.BaseResourceOperationApi;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "系统操作资源管理")
@RestController
public class BaseOperationController {
    @Autowired
    private BaseResourceOperationService baseResourceOperationService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页操作列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页操作列表", notes = "获取分页操作列表")
    @GetMapping("/operation")
    public ResultBody<PageList<BaseOperationAuthority>> getOperationListPage(@RequestParam Map map) {
        return ResultBody.success(baseResourceOperationService.findListPage(new PageParams(map)));
    }


    /**
     * 获取菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "获取菜单下所有操作", notes = "获取菜单下所有操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段", paramType = "form"),
    })
    @GetMapping("/operation/menu")
    public ResultBody<List<BaseOperationAuthority>> getOperationAllList(Long menuId) {
        return ResultBody.success(baseResourceOperationService.findListByMenuId(menuId));
    }

    /**
     * 获取操作资源详情
     *
     * @param operationId
     * @return
     */
    @ApiOperation(value = "获取操作资源详情", notes = "获取操作资源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作Id", paramType = "path"),
    })
    @GetMapping("/operation/{operationId}/info")
    public ResultBody<BaseOperationAuthority> getOperation(@PathVariable("operationId") Long operationId) {
        return ResultBody.success(baseResourceOperationService.getOperation(operationId));
    }

    /**
     * 添加操作资源
     *
     * @param operationCode 操作编码
     * @param operationName 操作名称
     * @param menuId        上级菜单
     * @param status        是否启用
     * @param priority      优先级越小越靠前
     * @param operationDesc 描述
     * @return
     */
    @ApiOperation(value = "添加操作资源", notes = "添加操作资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationCode", required = true, value = "操作编码", paramType = "form"),
            @ApiImplicitParam(name = "operationName", required = true, value = "操作名称", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "operationDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/operation/add")
    public ResultBody<Long> addOperation(
            @RequestParam(value = "operationCode") String operationCode,
            @RequestParam(value = "operationName") String operationName,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "operationDesc", required = false, defaultValue = "") String operationDesc
    ) {
        BaseResourceOperation operation = new BaseResourceOperation();
        operation.setOperationCode(operationCode);
        operation.setOperationName(operationName);
        operation.setMenuId(menuId);
        operation.setStatus(status);
        operation.setPriority(priority);
        operation.setOperationDesc(operationDesc);
        Long operationId = null;
        BaseResourceOperation result = baseResourceOperationService.addOperation(operation);
        if (result != null) {
            operationId = result.getOperationId();
            openRestTemplate.refreshGateway();
        }
        return ResultBody.success(operationId);
    }

    /**
     * 操作资源绑定API
     * @param operationId
     * @param apiIds
     * @return
     */
    @ApiOperation(value = "操作资源绑定API", notes = "操作资源绑定API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作ID", paramType = "form"),
            @ApiImplicitParam(name = "apiIds", required = false, value = "绑定的API资源:多个用,号隔开", paramType = "form"),
    })
    @PostMapping("/operation/api/add")
    public ResultBody addOperationApi(
            @RequestParam(value = "operationId") Long operationId,
            @RequestParam(value = "apiIds", required = false) String apiIds
    ) {
        baseResourceOperationService.addOperationApi(operationId, StringUtils.isNotBlank(apiIds) ? apiIds.split(",") : new String[]{});
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }

    /**
     * 获取操作资源已绑定API
     * @param operationId
     * @return
     */
    @ApiOperation(value = "获取操作资源已绑定API", notes = "获取操作资源已绑定API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作ID", paramType = "form")
    })
    @GetMapping("/operation/api")
    public ResultBody<BaseResourceOperationApi> getOperationApi(
            @RequestParam(value = "operationId") Long operationId
    ) {
       List<BaseResourceOperationApi> list =  baseResourceOperationService.findOperationApi(operationId);
        return ResultBody.success(list);
    }

    /**
     * 编辑操作资源
     *
     * @param operationId   操作ID
     * @param operationCode 操作编码
     * @param operationName 操作名称
     * @param menuId        上级菜单
     * @param status        是否启用
     * @param priority      优先级越小越靠前
     * @param operationDesc 描述
     * @return
     */
    @ApiOperation(value = "编辑操作资源", notes = "添加操作资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作ID", paramType = "form"),
            @ApiImplicitParam(name = "operationCode", required = true, value = "操作编码", paramType = "form"),
            @ApiImplicitParam(name = "operationName", required = true, value = "操作名称", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "operationDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/operation/update")
    public ResultBody updateOperation(
            @RequestParam("operationId") Long operationId,
            @RequestParam(value = "operationCode") String operationCode,
            @RequestParam(value = "operationName") String operationName,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "operationDesc", required = false, defaultValue = "") String operationDesc
    ) {
        BaseResourceOperation operation = new BaseResourceOperation();
        operation.setOperationId(operationId);
        operation.setOperationCode(operationCode);
        operation.setOperationName(operationName);
        operation.setMenuId(menuId);
        operation.setStatus(status);
        operation.setPriority(priority);
        operation.setOperationDesc(operationDesc);
        baseResourceOperationService.updateOperation(operation);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 移除操作资源
     *
     * @param operationId
     * @return
     */
    @ApiOperation(value = "移除操作资源", notes = "移除操作资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作ID", paramType = "form")
    })
    @PostMapping("/operation/remove")
    public ResultBody removeOperation(
            @RequestParam("operationId") Long operationId
    ) {
        baseResourceOperationService.removeOperation(operationId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }
}

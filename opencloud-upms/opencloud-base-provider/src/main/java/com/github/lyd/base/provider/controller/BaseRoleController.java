package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.model.entity.BaseRole;
import com.github.lyd.base.client.model.entity.BaseRoleUser;
import com.github.lyd.base.provider.service.BaseRoleService;
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
@Api(tags = "系统角色管理")
@RestController
public class BaseRoleController {
    @Autowired
    private BaseRoleService baseRoleService;

    /**
     * 获取分页角色列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页角色列表", notes = "获取分页角色列表")
    @GetMapping("/role")
    public ResultBody<PageList<BaseRole>> getRoleListPage(@RequestParam Map map) {
        return ResultBody.success(baseRoleService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有角色列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/role/all")
    public ResultBody<List<BaseRole>> getRoleAllList() {
        return ResultBody.success(baseRoleService.findList());
    }

    /**
     * 获取角色详情
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "path")
    })
    @GetMapping("/role/{roleId}/info")
    public ResultBody<BaseRole> getRole(@PathVariable(value = "roleId") Long roleId) {
        BaseRole result = baseRoleService.getRole(roleId);
        return ResultBody.success(result);
    }

    /**
     * 添加角色
     *
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     * @return
     */
    @ApiOperation(value = "添加角色", notes = "添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/add")
    public ResultBody<Long> addRole(
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        BaseRole role = new BaseRole();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        Long roleId = null;
        BaseRole result = baseRoleService.addRole(role);
        if (result != null) {
            roleId = result.getRoleId();
        }
        return ResultBody.success(roleId);
    }

    /**
     * 编辑角色
     *
     * @param roleId   角色ID
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     * @return
     */
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/update")
    public ResultBody updateRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        BaseRole role = new BaseRole();
        role.setRoleId(roleId);
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        baseRoleService.updateRole(role);
        return ResultBody.success();
    }


    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @PostMapping("/role/remove")
    public ResultBody removeRole(
            @RequestParam(value = "roleId") Long roleId
    ) {
        baseRoleService.removeRole(roleId);
        return ResultBody.success();
    }

    /**
     * 角色添加成员
     * @param roleId
     * @param userIds
     * @return
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/add")
    public ResultBody addUserRoles(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "userIds", required = false) String userIds
    ) {
        baseRoleService.saveRoleUsers(roleId, StringUtils.isNotBlank(userIds) ? userIds.split(",") : new String[]{});
        return ResultBody.success();
    }

    /**
     * 查询角色成员
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "查询角色成员", notes = "查询角色成员")
    @GetMapping("/role/users")
    public ResultBody<List<BaseRoleUser>> getRoleUsers(
            @RequestParam(value = "roleId") Long roleId
    ) {
        return ResultBody.success(baseRoleService.findRoleUsers(roleId));
    }

}

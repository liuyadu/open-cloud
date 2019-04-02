package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.model.BaseMenuAuthority;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceMenuService;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.test.BaseTest;
import com.github.lyd.common.utils.RandomValueUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class BaseAuthorityServiceImplTest extends BaseTest {

    @Autowired
    private BaseResourceOperationService baseResourceOperationService;
    @Autowired
    private BaseResourceMenuService baseResourceMenuService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Test
    public void  findAuthorityDetail(){
        List<BaseMenuAuthority> list = baseAuthorityService.findMenuAuthority(null);
    }

    @Test
    public void insertAuthority() {
        List<BaseResourceMenu> list = baseResourceMenuService.findAllList();
        for (Object object : list) {
            BaseResourceMenu menu = (BaseResourceMenu) object;
            BaseResourceOperation browse = new BaseResourceOperation();
            browse.setMenuId(menu.getMenuId());
            browse.setOperationCode(menu.getMenuCode() + "Browse");
            browse.setOperationName("浏览");
            browse.setOperationDesc("查看列表");
            browse.setIsPersist(1);
            browse.setStatus(1);
            BaseResourceOperation create = new BaseResourceOperation();
            create.setMenuId(menu.getMenuId());
            create.setOperationCode(menu.getMenuCode() + "Create");
            create.setOperationName("创建");
            create.setOperationDesc("新增数据");
            create.setIsPersist(1);
            create.setStatus(1);
            BaseResourceOperation edit = new BaseResourceOperation();
            edit.setMenuId(menu.getMenuId());
            edit.setOperationCode(menu.getMenuCode() + "Edit");
            edit.setOperationName("编辑");
            edit.setOperationDesc("编辑数据");
            edit.setIsPersist(1);
            edit.setStatus(1);
            BaseResourceOperation remove = new BaseResourceOperation();
            remove.setMenuId(menu.getMenuId());
            remove.setOperationName("删除");
            remove.setOperationDesc("删除数据");
            remove.setOperationCode(menu.getMenuCode() + "Remove");
            remove.setIsPersist(1);
            remove.setStatus(1);
            BaseResourceOperation detail = new BaseResourceOperation();
            detail.setMenuId(menu.getMenuId());
            detail.setOperationCode(menu.getMenuCode() + "Detail");
            detail.setOperationName("详情");
            detail.setOperationDesc("查看详情");
            detail.setIsPersist(1);
            detail.setStatus(1);
            try {
                baseResourceOperationService.addOperation(browse);
                baseResourceOperationService.addOperation(create);
                baseResourceOperationService.addOperation(edit);
                baseResourceOperationService.addOperation(remove);
                baseResourceOperationService.addOperation(detail);
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        System.out.println(clientId);
        System.out.println(clientSecret);
        System.out.println(encoder.encode(clientSecret));
    }
}

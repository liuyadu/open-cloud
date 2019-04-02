package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.provider.mapper.BaseResourceMenuMapper;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceMenuService;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseResourceMenuServiceImpl implements BaseResourceMenuService {
    @Autowired
    private BaseResourceMenuMapper baseResourceMenuMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseResourceOperationService baseResourceOperationService;
    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseResourceMenu> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseResourceMenu query =  pageParams.mapToObject(BaseResourceMenu.class);
        ExampleBuilder builder = new ExampleBuilder(BaseResourceMenu.class);
        Example example = builder.criteria()
                .andLikeRight("menuCode", query.getMenuCode())
                .andLikeRight("menuName", query.getMenuName()).end().build();
        example.orderBy("menuId").asc().orderBy("priority").asc();
        List<BaseResourceMenu> list = baseResourceMenuMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseResourceMenu> findAllList() {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceMenu.class);
        Example example = builder.criteria().end().build();
        example.orderBy("menuId").asc().orderBy("priority").asc();
        List<BaseResourceMenu> list = baseResourceMenuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public BaseResourceMenu getMenu(Long menuId) {
        return baseResourceMenuMapper.selectByPrimaryKey(menuId);
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    @Override
    public Boolean isExist(String menuCode) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceMenu.class);
        Example example = builder.criteria()
                .andEqualTo("menuCode", menuCode)
                .end().build();
        int count = baseResourceMenuMapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public BaseResourceMenu addMenu(BaseResourceMenu menu) {
        if (isExist(menu.getMenuCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", menu.getMenuCode()));
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getIsPersist() == null) {
            menu.setIsPersist(0);
        }
        menu.setServiceId(DEFAULT_SERVICE_ID);
        menu.setCreateTime(new Date());
        menu.setUpdateTime(menu.getCreateTime());
        baseResourceMenuMapper.insertSelective(menu);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);

        BaseResourceOperation browse = new BaseResourceOperation();
        browse.setMenuId(menu.getMenuId());
        browse.setOperationCode(menu.getMenuCode()+"Browse");
        browse.setOperationName("浏览");
        browse.setOperationDesc("查看列表");
        browse.setIsPersist(1);
        browse.setStatus(1);
        BaseResourceOperation create = new BaseResourceOperation();
        create.setMenuId(menu.getMenuId());
        create.setOperationCode(menu.getMenuCode()+"Create");
        create.setOperationName("创建");
        create.setOperationDesc("新增数据");
        create.setIsPersist(1);
        create.setStatus(1);
        BaseResourceOperation edit = new BaseResourceOperation();
        edit.setMenuId(menu.getMenuId());
        edit.setOperationCode(menu.getMenuCode()+"Edit");
        edit.setOperationName("编辑");
        edit.setOperationDesc("编辑数据");
        edit.setIsPersist(1);
        edit.setStatus(1);
        BaseResourceOperation remove = new BaseResourceOperation();
        remove.setMenuId(menu.getMenuId());
        remove.setOperationName("删除");
        remove.setOperationDesc("删除数据");
        remove.setOperationCode(menu.getMenuCode()+"Remove");
        remove.setIsPersist(1);
        remove.setStatus(1);
        BaseResourceOperation detail = new BaseResourceOperation();
        detail.setMenuId(menu.getMenuId());
        detail.setOperationCode(menu.getMenuCode()+"Detail");
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
       }catch (Exception e){
           log.error("");
       }
        return menu;
    }

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public BaseResourceMenu updateMenu(BaseResourceMenu menu) {
        BaseResourceMenu saved = getMenu(menu.getMenuId());
        if (saved == null) {
            throw new OpenAlertException(String.format("%s信息不存在!", menu.getMenuId()));
        }
        if (!saved.getMenuCode().equals(menu.getMenuCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(menu.getMenuCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", menu.getMenuCode()));
            }
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0l);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        menu.setUpdateTime(new Date());
        baseResourceMenuMapper.updateByPrimaryKeySelective(menu);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return menu;
    }


    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public void removeMenu(Long menuId) {
        BaseResourceMenu menu = getMenu(menuId);
        if (menu != null && menu.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除!"));
        }
        baseAuthorityService.removeAuthority(menuId,ResourceType.menu);
        baseResourceMenuMapper.deleteByPrimaryKey(menuId);
    }


}

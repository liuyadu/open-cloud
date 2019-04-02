package com.github.lyd.common.model;

import com.github.lyd.common.constants.CommonConstants;
import com.github.lyd.common.utils.BeanConvertUtils;
import com.github.lyd.common.utils.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页参数
 *
 * @author liuyau
 * @date 2018/07/10
 */
@ApiModel(value = "分页参数")
public class PageParams implements Serializable {
    private static final long serialVersionUID = -1710273706052960025L;
    private int page = CommonConstants.DEFAULT_PAGE;
    private int limit = CommonConstants.DEFAULT_LIMIT;
    private String sort;
    private String order;
    private Map<String, Object> requestMap = Maps.newHashMap();
    /**
     * 排序
     */
    private String orderBy;

    public static void main(String[] args) {
        System.out.println((String) "");
    }

    public PageParams() {
    }

    public PageParams(Map map) {
        if (map == null) {
            map = Maps.newHashMap();
        }
        this.page = Integer.parseInt(map.getOrDefault(CommonConstants.PAGE_KEY, CommonConstants.DEFAULT_PAGE).toString());
        this.limit = Integer.parseInt(map.getOrDefault(CommonConstants.PAGE_LIMIT_KEY, CommonConstants.DEFAULT_LIMIT).toString());
        this.sort = (String) map.getOrDefault(CommonConstants.PAGE_SORT_KEY, "");
        this.order = (String) map.getOrDefault(CommonConstants.PAGE_ORDER_KEY, "");
        map.remove(CommonConstants.PAGE_KEY);
        map.remove(CommonConstants.PAGE_LIMIT_KEY);
        map.remove(CommonConstants.PAGE_SORT_KEY);
        map.remove(CommonConstants.PAGE_ORDER_KEY);
        requestMap.putAll(map);
    }

    public PageParams(int page, int limit) {
        this(page, limit, "", "");
    }

    public PageParams(int page, int limit, String sort, String order) {
        this.page = page;
        this.limit = limit;
        this.sort = sort;
        this.order = order;
    }

    public int getPage() {
        if (page <= CommonConstants.MIN_PAGE) {
            page = 1;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        if (limit > CommonConstants.MAX_LIMIT) {
            limit = CommonConstants.MAX_LIMIT;
        }
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        if (StringUtils.isBlank(order)) {
            order = "asc";
        }
        if (StringUtils.isNotBlank(sort)) {
            this.setOrderBy(String.format("%s %s", StringUtils.camelToUnderline(sort), order));
        }
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public <T> T mapToObject(Class<T> t) {
        return BeanConvertUtils.mapToObject(this.requestMap, t);
    }
}

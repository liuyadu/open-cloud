package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseOperationAuthority;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.common.mapper.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface BaseResourceOperationMapper extends CrudMapper<BaseResourceOperation> {

}

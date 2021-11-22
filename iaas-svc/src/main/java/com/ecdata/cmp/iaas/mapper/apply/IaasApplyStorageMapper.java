package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyStorage;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyStorageVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 存储信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-03-10 13:50:16
 */
@Mapper
@Repository
public interface IaasApplyStorageMapper extends BaseMapper<IaasApplyStorage> {

    List<IaasApplyStorageVO> queryApplyStorage(Long applyId);

    List<IaasApplyStorageVO> queryBatchApplyStorage(List<Long> list);
}

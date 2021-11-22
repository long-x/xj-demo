package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyCalculate;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyCalculateVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 计算资源表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-03-10 13:50:16
 */
@Mapper
@Repository
public interface IaasApplyCalculateMapper extends BaseMapper<IaasApplyCalculate> {
    int deleteCalculate(Long id);

    List<IaasApplyCalculateVO> queryApplyCalculate(Long applyId);

    List<IaasApplyCalculateVO> queryApplyBatchCalculate(List<Long> list);
}

package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyRelationInfo;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyRelationInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface IaasApplyRelationInfoMapper extends BaseMapper<IaasApplyRelationInfo> {
    List<IaasApplyRelationInfoVO> queryRelationInfoList(@Param("applyId") Long applyId, @Param("userId") Long userId);

    List<IaasApplyRelationInfoVO> queryRelationInfoList2(@Param("applyId") Long applyId, @Param("userId") Long userId);

    List<IaasApplyRelationInfoVO> queryRelationInfoList3(@Param("applyId") Long applyId, @Param("userId") Long userId);
}

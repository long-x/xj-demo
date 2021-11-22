package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyConfigInfo;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasApplyConfigInfoMapper extends BaseMapper<IaasApplyConfigInfo> {

    List<IaasApplyConfigInfoVO> queryApplyConfigInfoList(@Param("applyId") Long applyId, @Param("configId") Long configId);

    List<IaasApplyConfigInfoVO> queryApplyConfigInfoListByApplyId(List<Long> list);

    int queryApplyServerNamePrefix(@Param("serverNamePrefix")  String serverNamePrefix);
}

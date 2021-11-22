package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyNetworkPolicy;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyNetworkPolicyVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网络策略信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-03-10 13:50:16
 */
@Mapper
@Repository
public interface IaasApplyNetworkPolicyMapper extends BaseMapper<IaasApplyNetworkPolicy> {

    List<IaasApplyNetworkPolicyVO> queryApplyNetworkPolicy(Long applyId);

    List<IaasApplyNetworkPolicyVO> queryBatchApplyNetworkPolicy(List<Long> list);
}

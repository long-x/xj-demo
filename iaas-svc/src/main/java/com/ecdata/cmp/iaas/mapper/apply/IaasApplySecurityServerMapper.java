package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplySecurityServer;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplySecurityServerVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 安全服务信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-03-10 13:50:16
 */
@Mapper
@Repository
public interface IaasApplySecurityServerMapper extends BaseMapper<IaasApplySecurityServer> {
    int deleteApplySecurityServer(Long id);

    List<IaasApplySecurityServerVO> querySecurityServer(Long applyId);

    List<IaasApplySecurityServerVO> queryBatchSecurityServer(List<Long> list);
}

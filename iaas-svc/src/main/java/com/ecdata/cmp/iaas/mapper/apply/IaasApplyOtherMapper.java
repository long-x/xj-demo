package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyOther;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyOtherVO;
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
public interface IaasApplyOtherMapper extends BaseMapper<IaasApplyOther> {
    int deleteApplyOther(Long id);

    List<IaasApplyOtherVO> queryApplyOther(Long applyId);

    List<IaasApplyOtherVO> queryBatchApplyOther(List<Long> list);
}

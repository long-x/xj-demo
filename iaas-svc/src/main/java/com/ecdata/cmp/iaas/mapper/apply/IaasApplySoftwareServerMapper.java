package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplySoftwareServer;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplySoftwareServerVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 软件服务
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-03-10 13:50:16
 */
@Mapper
@Repository
public interface IaasApplySoftwareServerMapper extends BaseMapper<IaasApplySoftwareServer> {

    List<IaasApplySoftwareServerVO> queryApplySoftwareServer(Long applyId);

    List<IaasApplySoftwareServerVO> queryBatchApplySoftwareServer(List<Long> list);
}

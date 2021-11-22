package com.ecdata.cmp.iaas.mapper.process;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasProcessApplyMapper extends BaseMapper<IaasProcessApply> {
    /**
     * 查询已申请服务
     *
     * @param page
     * @param params
     * @return
     */
    IPage<IaasProcessApplyVO> queryProcessApply(Page<IaasProcessApplyVO> page, @Param("map") Map<String, Object> params);

    /**
     * 服务申请详情
     *
     * @param processApplyId
     * @return
     */
    IaasProcessApplyVO queryProcessApplyDetails(Long processApplyId);


    /**
     * 根据业务组id获取区域id
     *
     * @param businessGroupId
     * @return
     */
    HashMap<String, Long> queryAreaIdByBusinessGroupId(@Param("businessGroupId") Long businessGroupId, @Param("provideId") Long provideId);


    /**
     * 不分页查询申请信息
     *
     * @param map
     * @return
     */
    List<IaasProcessApplyVO> queryCurrentUserProcessApply( @Param("map") Map<String, Object> map);
}

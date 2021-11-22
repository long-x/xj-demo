package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasResourcePool;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.IaasResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasResourcePoolMapper extends BaseMapper<IaasResourcePool> {
    /**
     * 修改更新记录
     *
     * @param id         供应商id
     * @param createUser 创建人id
     */
    void updateResourcePool(@Param("id")Long id, @Param("createUser")Long createUser);

    /**
     * 分页获连接池
     *
     * @param page   分页
     * @param params 关键字
     * @return IPage<ResourcePoolVO>
     */
    IPage<ResourcePoolVO> queryResourcePoolPage(Page<ResourcePoolVO> page, @Param("map") Map<String, Object> params);

    IaasClusterVo getResourceSumInfoById(IaasClusterVo clusterVo);

    List<ResourcePoolVO> getDatastoreInfoByClusterId(ResourcePoolVO resourcePoolVO);

    List<SysBusinessGroupVO> getBusinessGroupName(@Param("businessGroupName") String businessGroupName);

    void removeBusinessGroupResourcePool(@Param("poolId") Long poolId);

    void addBusinessGroupResourcePool(SysBusinessGroupResourcePoolVO businessGroupResourcePoolVO);

    List<SysBusinessGroupResourcePoolVO> getBusinessGroupNameByPoolId(@Param("poolId") Long poolId);

    /**
     * 已申请服务与虚拟机关联
     *
     * @param poolVO
     * @return
     */
    List<IaasResourcePoolVO> queryIaasResourcePoolVO(IaasResourcePoolVO poolVO);

    /**
     * 根据业务组id查询
     * @param id
     * @return
     */
    List<ResourcePoolVO> getPoolByGroupId(@Param("id")Long id);



}

package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasResourcePoolDatastore;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;
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
public interface IaasResourcePoolDatastoreMapper extends BaseMapper<IaasResourcePoolDatastore> {
    /**
     * 修改更新记录
     *
     * @param id         id
     * @param createUser 创建人id
     */
    void updateResourcePoolDatastore(Long id, Long createUser);

    /**
     * 分页获取信息
     *
     * @param page   分页
     * @param params 关键字
     * @return IPage<ResourcePoolDatastoreVO>
     */
    IPage<ResourcePoolDatastoreVO> queryResourcePoolDatastorePage(Page page, @Param("map") Map<String, Object> params);

    /**
     * 根据群id和资源池id获取主机被选中的存储
     *
     * @param params
     * @return
     */
    List<ResourcePoolDatastoreVO> queryResourcePoolDatastoreList(@Param("map") Map<String, Object> params);

    void removeByPoolId(@Param("poolId") Long poolId, @Param("createUser") Long createUser);
}

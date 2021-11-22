package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.DistributionDTO;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import com.ecdata.cmp.user.entity.SysBusinessGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 11:27
 * @modified By：
 */
public interface ISysBusinessGroupService extends IService<SysBusinessGroup> {

    /**
     * 新增
     */
    void insert(List<Map<String, Long>> list);

    /**
     * 删除
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 更新
     */
    void update(SysBusinessGroup sysBusinessGroup);

    /**
     * 根据主键 id 查询
     */
    SysBusinessGroup load(int id);

    /**
     * 分页查询
     */
    Map<String, Object> pageList(int offset, int pagesize);

    /**
     * 分页查询业务组信息
     */
    List<SysBusinessGroupVO> qrySysBusinessGroupInfo(String keyword);


    /**
     * 根据poolid查出业务组
     *
     * @param poolId
     * @return
     */
    List<SysBusinessGroupVO> getlistByPoolId(Long poolId);


    /**
     * 根据businessGroupName查出业务名（DISTINCT）
     *
     * @param businessGroupName
     * @return
     */
    List<SysBusinessGroupVO> getDisBusinessGroupName(@Param("businessGroupName") String businessGroupName);

    /**
     * 根据用户id查询业务组
     *
     * @param userId
     * @return
     */
    List<SysBusinessGroupVO> qrGroupByUserId(String userId);


    /**
     * 分页 查询业务组列表 显示父业务组名称
     *
     * @param page
     * @param keyword
     * @return
     */
    IPage<SysBusinessGroupVO> getGroupList(Page page, @Param("keyword") String keyword);


    /**
     * 业务组关联资源池信息 关联
     *
     * @param groupId
     * @return
     */
    IPage<SysBusinessGroupResourcePoolVO> getIaasResourcePpool(Page page, @Param("id") Long groupId,@Param("keyword") String keyword);


    /**
     * 业务组关联资源池信息 查看
     *
     * @param groupId
     * @return
     */
    List<SysBusinessGroupResourcePoolVO> getIaasResourcePpool(@Param("id") long groupId);

    /**
     * 根据资源池id查询资源池信息
     * @return
     */
    List<SysBusinessGroupResourcePoolVO>  getResourcePoolList(@Param("ids") List poolId);

    /**
     * 获取资源池id列表
     *
     * @param map 参数
     * @return id列表
     */
    List<Long> getPoolIds(Map<String, Object> map);

    /**
     * 获取用业务组户分布信息
     *
     * @return DistributionDTO
     */
    DistributionDTO getUserDistribution();


    /**
     * 根据用户id查询业务组
     * @param userId
     * @return
     */
    List<SysBusinessGroupVO> getBusinessGroupByUser(@Param("userId") String userId);

    /**
     * 删除业务组条件查询
     * @param id
     * @return
     */
    boolean getParentGroup(@Param("id") String id);

    /**
     * 根据id查询业务组信息
     * @param id
     * @return
     */
    SysBusinessGroup getBusinessGroupById(@Param("id") String id);

}

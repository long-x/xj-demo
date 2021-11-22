package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.DistributionBlockDTO;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import com.ecdata.cmp.user.entity.SysBusinessGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 13:41
 * @modified By：
 */
@Mapper
@Repository
public interface SysBusinessGroupMapper extends BaseMapper<SysBusinessGroup> {


    /**
     * [新增]
     *
     * @date 2019/11/20
     **/
    int insert(List<Map<String, Long>> list);

    /**
     * [刪除]
     *
     * @date 2019/11/20
     **/
    int modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * [更新]
     *
     * @date 2019/11/20
     **/
    int update(SysBusinessGroup sysBusinessGroup);

    /**
     * [查询] 根据主键 id 查询
     *
     * @date 2019/11/20
     **/
    SysBusinessGroup load(int id);

    /**
     * [查询] 分页查询
     *
     * @date 2019/11/20
     **/
    List<SysBusinessGroup> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @date 2019/11/20
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 分页查询业务组
     */
    List<SysBusinessGroupVO> qrySysBusinessGroupInfo(@Param("keyword") String keyword);

    /**
     * 分页查询业务 显示夫业务组名称
     *
     * @param page
     * @param keyword
     * @return
     */
    IPage<SysBusinessGroupVO> getGroupList(Page page, @Param("keyword") String keyword);


    /**
     * 根据poolid查出业务组list
     *
     * @param poolId
     * @return
     */
    List<SysBusinessGroupVO> getlistByPoolId(@Param("poolId") Long poolId);

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
    List<SysBusinessGroupVO> qrGroupByUserId(@Param("userId") String userId);


    IPage<SysBusinessGroupResourcePoolVO> getIaasResourcePpool(Page page, @Param("id") Long groupId,@Param("keyword") String keyword);

    /**
     * 根据资源池id查询资源池信息
     * @return
     */
    List<SysBusinessGroupResourcePoolVO>  getResourcePoolList(@Param("ids") List poolId);
    /**
     * 业务组关联资源池信息 查看
     *
     * @param groupId
     * @return
     */
    List<SysBusinessGroupResourcePoolVO> getIaasResourcePpool(@Param("id") long groupId);

    List<SysBusinessGroupResourcePoolVO> getIaasResourcePpool2(@Param("id") long groupId);
    /**
     * 获取资源池id列表
     *
     * @param map 参数
     * @return id列表
     */
    List<Long> getPoolIds(@Param("map") Map<String, Object> map);

    /**
     * 获取用业务组户分布信息
     *
     * @return List<DistributionBlockDTO>
     */
    List<DistributionBlockDTO> getUserDistribution();

    /**
     * 根据用户id查询业务组
     * @param userId
     * @return
     */
    List<SysBusinessGroupVO> getBusinessGroupByUser(@Param("userId") String userId);

    /**
     * 查询业务组有没有上级业务组
     * @param id
     * @return
     */
    Long getParentGroup(@Param("id") String id);

    /**
     * 查询业务组有没有关联用户
     * @param id
     * @return
     */
    Long getGroupMemberById(@Param("id") String id);

    /**
     * 查询业务组有没有关联资源池
     * @param id
     * @return
     */
    Long getGroupResourcePoolById(@Param("id") String id);

}

package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupResourcePool;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 11:31
 * @modified By：
 */
public interface ISysBusinessGroupResourcePoolService extends IService<SysBusinessGroupResourcePool> {

    /**
     * 删除
     */
    int deleteById(Long id);

    /**
     * 根据poolid批量删除
     */
    boolean deleteByPoolId(List<SysBusinessPoolVO> poolVOList);

    /**
     * 更新
     */
    void update(SysBusinessGroupResourcePool sysBusinessGroupResourcePool);

    /**
     * 根据主键 id 查询
     */
    SysBusinessGroupResourcePool load(int id);

    /**
     * 根据pod查找主表id
     */
    List<String> getGroupByPoolId(Long poolId);

    /**
     * 批量新增
     * @param poolVOList
     * @return
     */
    boolean save(List <SysBusinessPoolVO> poolVOList);

    /**
     * 批量更新
     * @param poolVOList
     * @return
     */
    boolean updateByIds(List <SysBusinessPoolVO> poolVOList);


    /**
     * 业务组 移除/关联 用户
     * @param memberAndUserVO
     * @return
     */
    boolean updateCorrelationPool(SysBusinessMemberAndUserAndPoolVO memberAndUserVO);



    /**
     * 分页查询业务组信息
     */
    IPage<SysBusinessPoolVO> qrySysBusinessPoolInfo(Page<SysBusinessPoolVO> page, String keyword);
}

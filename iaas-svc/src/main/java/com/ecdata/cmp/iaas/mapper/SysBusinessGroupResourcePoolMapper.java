package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupResourcePool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 10:10
 * @modified By：
 */
@Mapper
@Repository
public interface SysBusinessGroupResourcePoolMapper extends BaseMapper<SysBusinessGroupResourcePool> {


    /**
     * [刪除]
     *
     * @author xuj
     * @date 2019/11/21
     **/
    int deleteById(Long id);

    /**
     * 根据poolid删除
     */
    int deleteByPoolId(Long poolId);


    /**
     * 根据pod查找主表id
     */
    List<String> getGroupByPoolId(Long poolId);


    /**
     * [更新]
     *
     * @author xuj
     * @date 2019/11/21
     **/
    int update(SysBusinessGroupResourcePool sysBusinessGroupResourcePool);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author xuj
     * @date 2019/11/21
     **/
    SysBusinessGroupResourcePool load(int id);

    /**
     * 业务组 移除/关联 用户
     */
    int deletePoolByGroupId(Long businessGroupId);


    /**
     * 分页查询业务组
     */
    IPage<SysBusinessPoolVO> qrySysBusinessPoolInfo(Page page, @Param("keyword") String keyword);

}

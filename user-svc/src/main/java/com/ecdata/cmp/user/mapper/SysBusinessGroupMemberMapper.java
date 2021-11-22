package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 18:58
 * @modified By：
 */
@Mapper
@Repository
public interface SysBusinessGroupMemberMapper extends BaseMapper<SysBusinessGroupMember> {


    /**
     * [刪除]
     * @author xuj
     * @date 2019/11/21
     **/
    int deleteById(Long id);


    /**
     * 业务组 移除/关联 用户
     * @param businessGroupId
     * @return
     */
    int deleteMemberByGroupId(Long businessGroupId);


    /**
     * @Param: 项目组id查用户
     * @Description:
     * @Date: 2019/11/27 21:00
     */
    List<Long> getMemberByIds(@Param("ids") List<Long> ids);

    /**
     * 分页查询业务组
     */
    IPage<SysBusinessMemberVO> qrySysBusinessMemberInfo(Page page, @Param("keyword") String keyword);
}

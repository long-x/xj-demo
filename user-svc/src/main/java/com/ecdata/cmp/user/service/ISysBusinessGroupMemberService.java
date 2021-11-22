package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 18:51
 * @modified By：
 */
public interface ISysBusinessGroupMemberService extends IService<SysBusinessGroupMember> {


    /**
     * [批量刪除]
     * @author xuj
     * @date 2019/11/21
     **/
    boolean deleteIn(List<String> id);


    /**
     * [刪除]
     * @author xuj
     * @date 2019/11/21
     **/
    int delete(long id);


    /**
     * 根据主键 id 查询
     */
    SysBusinessGroupMember load(int id);


    /**
     * 业务组 移除/关联 用户
     * @param memberAndUserVO
     * @return
     */
    boolean updateCorrelationUser(SysBusinessMemberAndUserAndPoolVO memberAndUserVO);

    /**
     * 分页查询业务组信息
     */
    IPage<SysBusinessMemberVO> qrySysBusinessMemberInfo(Page<SysBusinessMemberVO> page, String keyword);


    /**
     * @Param: 项目组id查用户
     * @Description:
     * @Date: 2019/11/27 21:00
     */
    List<Long> getMemberByIds(@Param("ids") List<Long> ids);


}

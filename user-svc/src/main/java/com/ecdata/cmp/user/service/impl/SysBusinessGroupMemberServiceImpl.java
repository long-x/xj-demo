package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupMember;
import com.ecdata.cmp.user.mapper.SysBusinessGroupMemberMapper;
import com.ecdata.cmp.user.service.ISysBusinessGroupMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 18:57
 * @modified By：
 */
@Transactional
@Service
public class SysBusinessGroupMemberServiceImpl
        extends ServiceImpl<SysBusinessGroupMemberMapper,SysBusinessGroupMember>
        implements ISysBusinessGroupMemberService{


    @Override
    public boolean deleteIn(List<String> ids) {
        for (String id: ids) {
            if (baseMapper.deleteById(Long.parseLong(id))<1) {
                return false;
            }
        }
        return true;

    }

    @Override
    public int delete(long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public SysBusinessGroupMember load(int id) {
        return null;
    }

    /**
     * 关联用户，删除用户
     * 不管是新增，修改，删除 进行一次删除 再插入
     * 保证事务一致性
     */
    @Override
    public boolean updateCorrelationUser(SysBusinessMemberAndUserAndPoolVO memberAndUserVO) {
        //获取businessGroupId 删除库里面的数据
        Long businessGroupId = memberAndUserVO.getBusinessGroupId();
        List<String> userIds = memberAndUserVO.getUserId();
        baseMapper.deleteMemberByGroupId(businessGroupId);
            for (String userId:userIds) {
                SysBusinessGroupMember member = new SysBusinessGroupMember();
                member.setBusinessGroupId(businessGroupId);
                member.setUserId(Long.parseLong(userId));
                member.setId(SnowFlakeIdGenerator.getInstance().nextId());
                member.setCreateTime(DateUtil.getNow());
                if(!super.save(member)){
                    return false;
                }
            }
        return true;
    }

    @Override
    public IPage<SysBusinessMemberVO> qrySysBusinessMemberInfo(Page<SysBusinessMemberVO> page, String keyword) {
        IPage<SysBusinessMemberVO> result = baseMapper.qrySysBusinessMemberInfo(page, keyword);
        return result;
    }

    //根据业务组id查询关联用户id
    @Override
    public List<Long> getMemberByIds(List<Long> ids) {
     return baseMapper.getMemberByIds(ids);
    }
}

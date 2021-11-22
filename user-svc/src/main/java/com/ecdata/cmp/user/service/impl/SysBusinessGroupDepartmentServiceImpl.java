package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupDepartment;
import com.ecdata.cmp.user.mapper.SysBusinessGroupDepartmentMapper;
import com.ecdata.cmp.user.service.ISysBusinessGroupDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 18:57
 * @modified By：
 */
@Transactional
@Service
public class SysBusinessGroupDepartmentServiceImpl
        extends ServiceImpl<SysBusinessGroupDepartmentMapper,SysBusinessGroupDepartment>
        implements ISysBusinessGroupDepartmentService{


    /**
     * 关联用户，删除用户
     * 不管是新增，修改，删除 进行一次删除 再插入
     * 保证事务一致性
     */
    @Override
    public boolean updateCorrelationDepartment(SysBusinessMemberAndUserAndPoolVO memberAndUserVO) {
        //获取businessGroupId 删除库里面的数据
        Long businessGroupId = memberAndUserVO.getBusinessGroupId();
        List<String> departmentIds = memberAndUserVO.getDepartmentId();
        baseMapper.deleteDepartmentByGroupId(businessGroupId);
        for (String departmentId:departmentIds) {
            SysBusinessGroupDepartment member = new SysBusinessGroupDepartment();
            member.setBusinessGroupId(businessGroupId);
            member.setDepartmentId(Long.parseLong(departmentId));
            member.setId(SnowFlakeIdGenerator.getInstance().nextId());
            member.setCreateTime(DateUtil.getNow());
            if(!super.save(member)){
                return false;
            }
        }
        return true;

    }

    @Override
    public IPage<DepartmentVO> getDepartmentList(Page<DepartmentVO> page, Long id) {
        return baseMapper.getDepartmentList(page,id);
    }

    @Override
    public boolean deleteDepartment(SysBusinessMemberAndUserAndPoolVO vo) {
        List<String> departmentIds = vo.getDepartmentId();
        for (String departmentId:departmentIds) {
            baseMapper.deleteDepartment(vo.getBusinessGroupId(),Long.parseLong(departmentId));
        }

        return true;
    }

    @Override
    public SysBusinessGroupDepartment getGroupIdByDepartment(Long departmentId) {

        return baseMapper.getGroupIdByDepartment(departmentId);
    }


}

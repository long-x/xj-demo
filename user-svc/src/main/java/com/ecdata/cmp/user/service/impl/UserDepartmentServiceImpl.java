package com.ecdata.cmp.user.service.impl;

import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.UserDepartment;
import com.ecdata.cmp.user.mapper.UserDepartmentMapper;
import com.ecdata.cmp.user.service.IUserDepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-19
 */
@Service
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartment> implements IUserDepartmentService {

    @Override
    public void insertBatch(Long userId, List<DepartmentVO> departmentList) {
        if (departmentList == null || departmentList.size() == 0) {
            return;
        }
        LambdaQueryWrapper<UserDepartment> query = new LambdaQueryWrapper<>();
        for (DepartmentVO departmentVO : departmentList) {
            Long departmentId = departmentVO.getId();
            query.eq(UserDepartment::getDepartmentId, departmentId);
            query.eq(UserDepartment::getUserId, userId);
            List<UserDepartment> depList = baseMapper.selectList(query);
            if (depList == null || depList.size() == 0) {
                Long id = SnowFlakeIdGenerator.getInstance().nextId();
                baseMapper.insert(new UserDepartment(id, userId, departmentId, DateUtil.getNow()));
            }
        }
    }

    @Override
    public void updateUserDep(Long userId, List<DepartmentVO> departmentList) {
        LambdaQueryWrapper<UserDepartment> query = new LambdaQueryWrapper<>();
        query.eq(UserDepartment::getUserId, userId);
        baseMapper.delete(query);
        for (int i = 0; departmentList != null && i < departmentList.size(); i++) {
            Long id = SnowFlakeIdGenerator.getInstance().nextId();
            baseMapper.insert(new UserDepartment(id, userId, departmentList.get(i).getId(), DateUtil.getNow()));
        }
    }
}

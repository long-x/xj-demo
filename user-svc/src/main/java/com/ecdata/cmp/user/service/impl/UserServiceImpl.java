package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.ecdata.cmp.user.dto.ProjectVO;
import com.ecdata.cmp.user.dto.RoleVO;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.mapper.UserMapper;
import com.ecdata.cmp.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public boolean isExistUserName(User user, boolean exclude) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getName, user.getName());
        if (exclude) {
            queryWrapper.lambda().ne(User::getId, user.getId());
        }

        return baseMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public void insertBatchUserDep(List<Map<String, Long>> list) {
        baseMapper.insertBatchUserDep(list);
    }

    @Override
    public void deleteBatchUserDep(Long[] ids) {
        baseMapper.deleteBatchUserDep(ids);
    }

    @Override
    public void insertBatchUserRole(List<Map<String, Long>> list) {
        baseMapper.insertBatchUserRole(list);
    }

    @Override
    public void deleteBatchUserRole(Long[] ids) {
        baseMapper.deleteBatchUserRole(ids);
    }

    @Override
    public IPage<UserVO> qryUserInfo(Page<UserVO> page, String keyword) {
        IPage<UserVO> result = baseMapper.qryUserInfo(page, keyword);

        String split = " ";
        List<UserVO> userVOList = result.getRecords();
        for (UserVO userVO : userVOList) {
            List<DepartmentVO> departmentList = userVO.getDepartmentList();
            StringBuilder showDepartment = new StringBuilder();
            for (DepartmentVO department : departmentList) {
                showDepartment.append(department.getDepartmentName()).append(split);
            }
            userVO.setShowDepartment(showDepartment.toString());

            List<RoleVO> roleList = userVO.getRoleList();
            StringBuilder showRole = new StringBuilder();
            for (RoleVO role : roleList) {
                showRole.append(role.getRoleName()).append(split);
            }
            userVO.setShowRole(showRole.toString());

            List<ProjectVO> projectList = userVO.getProjectList();
            StringBuilder showProject = new StringBuilder();
            for (ProjectVO projectVO : projectList) {
                showProject.append(projectVO.getProjectName()).append(split);
            }
            userVO.setShowProject(showProject.toString());
        }
        return result;
    }

    @Override
    public User getSysAdmin() {
        List<User> userList = baseMapper.getSysAdmin();
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }
}

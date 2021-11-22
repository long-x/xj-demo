package com.ecdata.cmp.user.service.impl;

import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.ProjectVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.UserProject;
import com.ecdata.cmp.user.mapper.UserProjectMapper;
import com.ecdata.cmp.user.service.IUserProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-05-08
*/
@Service
public class UserProjectServiceImpl extends ServiceImpl<UserProjectMapper, UserProject> implements IUserProjectService {
    @Override
    public void insertBatch(Long userId, List<ProjectVO> projectList) {
        if (projectList == null || projectList.size() == 0) {
            return;
        }
        LambdaQueryWrapper<UserProject> query = new LambdaQueryWrapper<>();
        for (ProjectVO project : projectList) {
            Long projectId = project.getId();
            query.eq(UserProject::getProjectId, projectId);
            query.eq(UserProject::getUserId, userId);
            List<UserProject> depList = baseMapper.selectList(query);
            if (depList == null || depList.size() == 0) {
                baseMapper.insert(new UserProject(SnowFlakeIdGenerator.getInstance().nextId(), userId, projectId, DateUtil.getNow()));
            }
        }
    }

    @Override
    public void updateUserProject(Long userId, List<ProjectVO> projectList) {
        LambdaQueryWrapper<UserProject> query = new LambdaQueryWrapper<>();
        query.eq(UserProject::getUserId, userId);
        baseMapper.delete(query);
        for (int i = 0; projectList != null && i < projectList.size(); i++) {
            baseMapper.insert(new UserProject(SnowFlakeIdGenerator.getInstance().nextId(), userId, projectList.get(i).getId(), DateUtil.getNow()));
        }
    }
}

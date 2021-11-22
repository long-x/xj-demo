package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.ProjectVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.UserProject;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-05-08
*/
public interface IUserProjectService extends IService<UserProject> {

    /**
     * 批量插入用户项目
     * @param userId          用户id
     * @param projectList    角色列表
     */
    void insertBatch(Long userId, List<ProjectVO> projectList);
    /**
     * 批量更新用户项目
     * @param userId         用户id
     * @param projectList    角色列表
     */
    void updateUserProject(Long userId, List<ProjectVO> projectList);

}

package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.ProjectVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.Project;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-05-08
 */
public interface IProjectService extends IService<Project> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 查询项目信息
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return 项目信息
     */
    IPage<ProjectVO> qryProjectInfo(Page page, Map<String, Object> params);

    /**
     * 根据用户id获取项目列表
     *
     * @param userId 用户id
     * @return 项目信息
     */
    List<ProjectVO> getProjectByUserId(Long userId);

    /**
     * 刷新所有父主键字符串
     */
    void refreshParentIdsStr();

    /**
     * 更新子项目的父主键字符串
     *
     * @param project 项目信息
     */
    void updateSubParentIdsStr(Project project);

    /**
     * 查询用户父项目列表
     *
     * @param userId 用户id
     * @return 父项目列表
     */
    List<Project> qryParentProjectByUserId(Long userId);

    /**
     * 添加项目
     *
     * @param projectVO 项目vo对象
     * @return true/false
     * @throws IOException io异常
     */
    boolean addProject(ProjectVO projectVO) throws IOException;

    /**
     * 更新项目
     *
     * @param projectVO 项目vo对象
     * @return true/false
     * @throws IOException io异常
     */
    boolean updateProject(ProjectVO projectVO) throws IOException;

    /**
     * 删除项目
     *
     * @param projectId 项目id
     * @return true/false
     * @throws IOException io异常
     */
    boolean delProject(Long projectId) throws IOException;

    /**
     * 同步仅同步所有项目
     *
     * @param clusterId 集群id
     * @throws IOException io异常
     */
    void syncOnlyAllProject(Long clusterId) throws IOException;

    /**
     * 同步项目(包含项目下的容器)
     *
     * @param projectId   项目id
     * @throws IOException io异常
     */
    void syncOpenShiftProject(Long projectId) throws IOException;

    /**
     * 同步项目(包含项目下的容器)
     * @param clusterId     集群id
     * @param nameList      项目名列表
     * @throws IOException  io异常
     */
    void syncOpenShiftProject(Long clusterId, List<String> nameList) throws IOException;
}

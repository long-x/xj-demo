package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.ProjectVO;
import com.ecdata.cmp.user.entity.Project;
import com.ecdata.cmp.user.mapper.ProjectMapper;
import com.ecdata.cmp.user.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-05-08
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

//    /**
//     * opc集群客户端
//     */
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private OcpClusterClient ocpClusterClient;
//
//    /**
//     * opc集群客户端
//     */
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private OcpContainerClient ocpContainerClient;

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public IPage<ProjectVO> qryProjectInfo(Page page, Map<String, Object> params) {
        return baseMapper.qryProjectInfo(page, params);
    }

    @Override
    public List<ProjectVO> getProjectByUserId(Long userId) {
        return baseMapper.getProjectByUserId(userId);
    }

    @Override
    public void refreshParentIdsStr() {
        Map<Long, Project> map = new HashMap<>();
        List<Project> projectList = this.list();
        for (Project project : projectList) {
            map.put(project.getId(), project);
        }

        for (Project project : projectList) {
            Long parentId = project.getParentId();
            if (parentId != null && parentId > 0) {
                project.setParentIdsStr(this.getParentIdsStr(map, parentId));
            } else {
                project.setParentIdsStr("0");
            }
            project.updateById();
        }
    }

    private String getParentIdsStr(Map<Long, Project> map, Long parentId) {
        StringBuilder parentIdsStr = new StringBuilder();
        Project parent = map.get(parentId);
        if (parent == null) {
            return "0";
        }
        parentIdsStr.append(parent.getId());
        Long pid = parent.getParentId();
        if (pid != null && pid > 0) {
            parentIdsStr.append(",").append(this.getParentIdsStr(map, pid));
        }
        return parentIdsStr.toString();
    }

    @Override
    public void updateSubParentIdsStr(Project project) {
        Map<Long, Project> map = new HashMap<>();

        Long root = project.getId();
        List<Project> projectList = this.baseMapper.qryAllSubProject(root);

        if (projectList == null || projectList.size() == 0) {
            return;
        }

        for (Project depart : projectList) {
            map.put(depart.getId(), depart);
        }

        for (Project depart : projectList) {
            depart.setParentIdsStr(this.getParentIdsStr(map, depart.getParentId(), root));
            depart.updateById();
        }
    }

    @Override
    public List<Project> qryParentProjectByUserId(Long userId) {
        return baseMapper.qryParentProjectByUserId(userId);
    }

    private String getParentIdsStr(Map<Long, Project> map, Long parentId, Long root) {
        Project project = map.get(parentId);
        if (parentId.equals(root)) {
            return project.getParentIdsStr();
        } else {
            return parentId + "," + this.getParentIdsStr(map, project.getParentId(), root);
        }
    }

    @Override
    public boolean addProject(ProjectVO projectVO) throws IOException {
        Project project = new Project();
        BeanUtils.copyProperties(projectVO, project);
        Long parentId = project.getParentId();
        if (parentId == null || parentId < 1) {
            project.setParentIdsStr("0");
        } else {
            Project parent = this.getById(parentId);
            String parentIdsStr = parent.getParentIdsStr();
            if (StringUtils.isEmpty(parentIdsStr)) {
                parentIdsStr = parentId.toString();
            } else {
                parentIdsStr = parentId + "," + parentIdsStr;
            }
            project.setParentIdsStr(parentIdsStr);
        }
        project.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());

//        Long clusterId = project.getClusterId();
//        if (clusterId != null) {
//            ContainerProjectRequest request = new ContainerProjectRequest();
//            request.setClusterId(clusterId);
//            request.setProjectName(project.getProjectName());
//            request.setDisplayName(project.getDisplayName());
//            request.setDescription(project.getRemark());
//            ContainerProjectResponse response = ocpClusterClient.addContainerProject(AuthContext.getAuthz(), request);
//            com.ecdata.cmp.ocp.dto.openshift.Project date = response.getData();
//            project.setUid(date.getMetadata().getUid());
//        } else {
//            log.info("clusterId == null 不调用ocp接口");
//        }

        return this.save(project);
    }

    @Override
    public boolean updateProject(ProjectVO projectVO) throws IOException {
        Project project = new Project();
        BeanUtils.copyProperties(projectVO, project);
        Long id = project.getId();

        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Project::getId, id);
        Long parentId = project.getParentId();
        if (parentId == null) {
            queryWrapper.lambda().isNull(Project::getParentId);
        } else {
            queryWrapper.lambda().eq(Project::getId, parentId);
        }
        int count = this.count(queryWrapper);
        if (count == 0) {
            if (parentId == null || parentId < 1) {
                project.setParentIdsStr("0");
            } else {
                Project parent = this.getById(parentId);
                String parentIdsStr = parent.getParentIdsStr();
                if (StringUtils.isEmpty(parentIdsStr)) {
                    parentIdsStr = parentId.toString();
                } else {
                    parentIdsStr = parentId + "," + parentIdsStr;
                }
                project.setParentIdsStr(parentIdsStr);
            }
        }

        project.setUpdateUser(Sign.getUserId());
        project.setUpdateTime(DateUtil.getNow());

//        Long clusterId = project.getClusterId();
//        if (clusterId != null) {
//            if (StringUtils.isEmpty(project.getUid())) {
//                log.info("uid不存在，不调用ocp接口");
//            } else {
//                ContainerProjectRequest request = new ContainerProjectRequest();
//                request.setClusterId(clusterId);
//                request.setProjectName(project.getProjectName());
//                request.setDisplayName(project.getDisplayName());
//                request.setDescription(project.getRemark());
//                ContainerProjectResponse response = ocpClusterClient.updateContainerProject(AuthContext.getAuthz(), request);
//                com.ecdata.cmp.ocp.dto.openshift.Project date = response.getData();
//                project.setUid(date.getMetadata().getUid());
//            }
//        } else {
//            log.info("clusterId == null 不调用ocp接口");
//        }
        if (this.updateById(project)) {
            if (count == 0) {
                this.updateSubParentIdsStr(project);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delProject(Long projectId) throws IOException {
        Project project = this.getById(projectId);
//        Long clusterId = project.getClusterId();
//        if (clusterId != null) {
//            if (StringUtils.isEmpty(project.getUid())) {
//                log.info("uid不存在，不调用ocp接口");
//            } else {
//                ContainerProjectRequest request = new ContainerProjectRequest();
//                request.setClusterId(clusterId);
//                request.setProjectName(project.getProjectName());
//                ocpClusterClient.delContainerProject(AuthContext.getAuthz(), request);
//            }
//        } else {
//            log.info("clusterId == null 不调用ocp接口");
//        }
        if (this.removeById(projectId)) {
            this.modifyUpdateRecord(projectId, Sign.getUserId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void syncOnlyAllProject(Long clusterId) throws IOException {
//        List<Project> addProjects = new ArrayList<>();
//        List<Project> updateProjects = new ArrayList<>();
//        Long userId = Sign.getUserId();
//
//        List<Long> ids;
//        if (clusterId == null) {
//            final int TYPE = 2;
//            LongListResponse response = this.ocpClusterClient.listIds(AuthContext.getAuthz(), TYPE);
//            ids = response.getData();
//        } else {
//            ids = new ArrayList<>();
//            ids.add(clusterId);
//        }
//
//        for (Long cid : ids) {
//            Map<String, Project> uidMap = new HashMap<>();
//            Map<String, Project> nameMap = new HashMap<>();
//
//            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(Project::getClusterId, cid);
//            List<Project> projects = this.list(queryWrapper);
//            if (projects != null && projects.size() > 0) {
//                for (Project project : projects) {
//                    uidMap.put(project.getUid(), project);
//                    nameMap.put(project.getProjectName(), project);
//                }
//            }
//
//            ContainerProjectListResponse response = this.ocpClusterClient.getContainerProjectList(AuthContext.getAuthz(), cid);
//            List<com.ecdata.cmp.ocp.dto.openshift.Project> items = response.getData().getItems();
//            if (items != null && items.size() > 0) {
//                for (com.ecdata.cmp.ocp.dto.openshift.Project item : items) {
//                    ObjectMeta metadata = item.getMetadata();
//                    String uid = metadata.getUid();
//                    String name = metadata.getName();
//                    if (uidMap.containsKey(uid)) {
//                        Project p = uidMap.get(uid);
//                        if (!p.getProjectName().equals(name)) {
//                            p.setProjectName(name);
//                            p.setUpdateUser(userId);
//                            updateProjects.add(p);
//                        }
//                    } else if (nameMap.containsKey(name)) {
//                        Project p = nameMap.get(name);
//                        p.setUid(uid);
//                        p.setUpdateUser(userId);
//                        updateProjects.add(p);
//                    } else {
//                        Map<String, String> annotations = metadata.getAnnotations();
//                        Project p = new Project();
//                        p.setId(SnowFlakeIdGenerator.getInstance().nextId());
//                        p.setClusterId(cid);
//                        p.setUid(uid);
//                        p.setProjectName(name);
//                        p.setCreateUser(userId);
//                        if (annotations != null) {
//                            p.setDisplayName(annotations.get("openshift.io/display-name"));
//                            p.setRemark(annotations.get("openshift.io/description"));
//                        }
//                        addProjects.add(p);
//                    }
//                }
//            }
//        }
//        this.updateBatchById(updateProjects);
//        this.saveBatch(addProjects);
    }

    @Override
    public void syncOpenShiftProject(Long projectId) throws IOException {
//        Project project = this.getById(projectId);
//        Long clusterId = project.getClusterId();
//        String projectName = project.getProjectName();
//        if (clusterId != null && StringUtils.isNotEmpty(projectName)) {
//            String token = AuthContext.getAuthz();
//            BooleanResponse booleanResponse = this.ocpClusterClient.isExistProject(token, clusterId, projectName);
//            if (booleanResponse.getData()) {
//                ContainerProjectResponse projectResponse = this.ocpClusterClient.getContainerProject(token, clusterId, projectName);
//                ObjectMeta metadata = projectResponse.getData().getMetadata();
//                project.setUid(metadata.getUid());
//                Map<String, String> annotations = metadata.getAnnotations();
//                if (annotations != null && annotations.size() > 0) {
//                    project.setDisplayName(annotations.get("openshift.io/display-name"));
//                    project.setRemark(annotations.get("openshift.io/description"));
//                }
//                this.updateById(project);
//                this.ocpContainerClient.syncProjectContainer(token, clusterId, projectId, projectName);
//            } else {
//                ContainerProjectRequest request = new ContainerProjectRequest();
//                request.setClusterId(clusterId);
//                request.setProjectName(project.getProjectName());
//                request.setDisplayName(project.getDisplayName());
//                request.setDescription(project.getRemark());
//                ContainerProjectResponse response = ocpClusterClient.addContainerProject(AuthContext.getAuthz(), request);
//                com.ecdata.cmp.ocp.dto.openshift.Project date = response.getData();
//                project.setUid(date.getMetadata().getUid());
//                this.updateById(project);
//            }
//
//        }
    }

    @Override
    public void syncOpenShiftProject(Long clusterId, List<String> nameList) throws IOException {
        for (String name : nameList) {
            Project project;
            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Project::getClusterId, clusterId).eq(Project::getProjectName, name);
            List<Project> projectList = this.list(queryWrapper);
            if (projectList != null && projectList.size() > 0) {
                project = projectList.get(0);
            } else {
                project = new Project();
                project.setId(SnowFlakeIdGenerator.getInstance().nextId())
                        .setClusterId(clusterId)
                        .setProjectName(name)
                        .setDisplayName(name)
                        .setCreateUser(Sign.getUserId())
                        .setCreateTime(DateUtil.getNow());
                this.save(project);
            }
            this.syncOpenShiftProject(project.getId());
        }
    }
}

package com.ecdata.cmp.user.service.impl;

import com.ecdata.cmp.user.dto.DepartmentVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.Department;
import com.ecdata.cmp.user.mapper.DepartmentMapper;
import com.ecdata.cmp.user.service.IDepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Override
    public List<DepartmentVO> qryDepByUserId(Long userId) {
        return baseMapper.qryDepByUserId(userId);
    }


    @Override
    public List<DepartmentVO> getTreeList(String keyword) {
        LambdaQueryWrapper<Department> query = new LambdaQueryWrapper<>();
        query.orderByAsc(Department::getScore);
        List<Department> list = this.list(query);
        return this.toTreeData(list, keyword);
    }

    private List<DepartmentVO> toTreeData(List<Department> depList, String keyword) {
        List<DepartmentVO> tree = new ArrayList<>();
        List<DepartmentVO> childList = new ArrayList<>();
        for (Department department : depList) {
            DepartmentVO departmentVO = new DepartmentVO();
            BeanUtils.copyProperties(department, departmentVO);
            Long depId = department.getId();
            departmentVO.setKey(depId.toString());
            departmentVO.setValue(depId.toString());
            departmentVO.setTitle(department.getDepartmentName());
            Long parentId = departmentVO.getParentId();
            if (parentId == null || parentId < 1) {
                tree.add(departmentVO);
            } else {
                childList.add(departmentVO);
            }
        }
        this.setChildren(tree, childList);

        if (StringUtils.isNotEmpty(keyword)) {
            this.filterKeyword(tree, keyword);
        }

        return tree;
    }

    private void setChildren(List<DepartmentVO> parentList, List<DepartmentVO> childList) {
        for (DepartmentVO parent : parentList) {
            Long parentId = parent.getId();
            List<DepartmentVO> children = new ArrayList<>();
            Iterator<DepartmentVO> it = childList.iterator();
            while (it.hasNext()) {
                DepartmentVO child = it.next();
                if (parentId.equals(child.getParentId())) {
                    children.add(child);
                    it.remove();
                }
            }
            if (children.size() > 0 && childList.size() > 0) {
                this.setChildren(children, childList);
            }
            parent.setChildren(children);
        }
    }

    private boolean filterKeyword(List<DepartmentVO> tree, String keyword) {
        boolean flag = false;
        Iterator<DepartmentVO> treeIt = tree.iterator();
        while (treeIt.hasNext()) {
            DepartmentVO vo = treeIt.next();
            List<DepartmentVO> children = vo.getChildren();
            if (children != null && children.size() > 0) {
                if (this.filterKeyword(children, keyword)) {
                    flag = true;
                    continue;
                }
            }
            if (vo.getDepartmentName().contains(keyword)) {
                flag = true;
            } else {
                treeIt.remove();
            }
        }

        return flag;
    }


    @Override
    public List<DepartmentVO> qryTreeList(String keyword) {
        //未选中
        List<DepartmentVO> departmentVO = baseMapper.qryTreeList();
        departmentVO.stream().forEach(e -> e.setDisableCheckbox(false));
        //已选中
        List<DepartmentVO> list = baseMapper.qryInTreeList();
        list.stream().forEach(e -> e.setDisableCheckbox(true));
        departmentVO.addAll(list);
        return this.toTreeData2(departmentVO, keyword);
    }

    @Override
    public List<String> qryCheckTreeList() {
        List<String> list = new ArrayList<>();
        List<DepartmentVO> departmentVOS = baseMapper.qryInTreeList();
        for (DepartmentVO vo:departmentVOS) {
            list.add(vo.getId().toString());
        }
        return list;
    }

    private List<DepartmentVO> toTreeData2(List<DepartmentVO> depList, String keyword) {
        List<DepartmentVO> tree = new ArrayList<>();
        List<DepartmentVO> childList = new ArrayList<>();
        for (DepartmentVO department : depList) {
            DepartmentVO departmentVO = new DepartmentVO();
            BeanUtils.copyProperties(department, departmentVO);
            Long depId = department.getId();
            departmentVO.setKey(depId.toString());
            departmentVO.setValue(depId.toString());
            departmentVO.setTitle(department.getDepartmentName());
            Long parentId = departmentVO.getParentId();
            if (parentId == null || parentId < 1) {
                tree.add(departmentVO);
            } else {
                childList.add(departmentVO);
            }
        }
        this.setChildren(tree, childList);

        if (StringUtils.isNotEmpty(keyword)) {
            this.filterKeyword(tree, keyword);
        }

        return tree;
    }


    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public void refreshParentIdsStr() {
        Map<Long, Department> map = new HashMap<>();
        List<Department> departmentList = this.list();
        for (Department department : departmentList) {
            map.put(department.getId(), department);
        }

        for (Department department : departmentList) {
            Long parentId = department.getParentId();
            if (parentId != null && parentId > 0) {
                department.setParentIdsStr(this.getParentIdsStr(map, parentId));
            } else {
                department.setParentIdsStr("0");
            }
            department.updateById();
        }
    }

    @Override
    public List<Department> qryParentDeploymentByUserId(Long userId) {
        return baseMapper.qryParentDeploymentByUserId(userId);
    }

    private String getParentIdsStr(Map<Long, Department> map, Long parentId) {
        StringBuilder parentIdsStr = new StringBuilder();
        Department parent = map.get(parentId);
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

    public void updateSubParentIdsStr(Department department) {
        Map<Long, Department> map = new HashMap<>();

        Long root = department.getId();
        List<Department> departmentList = this.baseMapper.qryAllSubDeployment(root);

        if (departmentList == null || departmentList.size() == 0) {
            return;
        }

        for (Department depart : departmentList) {
            map.put(depart.getId(), depart);
        }

        for (Department depart : departmentList) {
           depart.setParentIdsStr(this.getParentIdsStr(map, depart.getParentId(), root));
           depart.updateById();
        }
    }

    private String getParentIdsStr(Map<Long, Department> map, Long parentId, Long root) {
        Department department = map.get(parentId);
        if (parentId.equals(root)) {
            return department.getParentIdsStr();
        } else {
            return parentId + "," + this.getParentIdsStr(map, department.getParentId(), root);
        }
    }

}

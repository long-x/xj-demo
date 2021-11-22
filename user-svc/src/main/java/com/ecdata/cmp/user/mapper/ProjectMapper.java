package com.ecdata.cmp.user.mapper;

import com.ecdata.cmp.user.dto.ProjectVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-05-08
*/
@Mapper
@Repository
public interface ProjectMapper extends BaseMapper<Project> {
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 查询项目信息
     * @param page      分页参数
     * @param params    查询参数
     * @return  项目信息
     */
    IPage<ProjectVO> qryProjectInfo(Page page, @Param("map") Map<String, Object> params);

    /**
     * 根据用户id获取项目列表
     * @param userId   用户id
     * @return  项目信息
     */
    List<ProjectVO> getProjectByUserId(Long userId);

    /**
     * 查询所有子项目(包含子项目的子项目)
     * @param id    项目id
     * @return  子项目列表
     */
    List<Project> qryAllSubProject(Long id);

    /**
     * 查询用户父项目列表
     * @param userId    用户id
     * @return  父项目列表
     */
    List<Project> qryParentProjectByUserId(Long userId);

    List<Long> qryUserIdByProjectIds(@Param("list") List<Long> ids);

}

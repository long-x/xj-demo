package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
public interface IUserService extends IService<User> {
    /**
     * 用户名是否已存在
     *
     * @param user    用户
     * @param exclude true：排除本身   false：不排除
     * @return true：存在；false：不存在
     */
    boolean isExistUserName(User user, boolean exclude);

    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 批量插入用户部门
     *
     * @param list 用户部门列表
     */
    void insertBatchUserDep(List<Map<String, Long>> list);

    /**
     * 批量删除用户部门
     *
     * @param ids 用户部门id列表
     */
    void deleteBatchUserDep(Long[] ids);

    /**
     * 批量插入用户角色
     *
     * @param list 用户角色列表
     */
    void insertBatchUserRole(List<Map<String, Long>> list);

    /**
     * 批量删除用户角色
     *
     * @param ids 用户角色id列表
     */
    void deleteBatchUserRole(Long[] ids);

    /**
     * 分页获取用户信息
     *
     * @param page    分页
     * @param keyword 关键字
     * @return IPage<UserVO>
     */
    IPage<UserVO> qryUserInfo(Page<UserVO> page, String keyword);

    /**
     * 获取系统管理员用户
     *
     * @return 系统管理员用户
     */
    User getSysAdmin();
}

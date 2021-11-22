package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.dto.response.BimSchemaChildResp;
import com.ecdata.cmp.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户信息（登入校验）
     *
     * @param tenantId 租户id
     * @param username 用户名
     * @return 用户信息
     */
    List<User> loadUserByUsername(@Param("tenantId") Long tenantId, @Param("username") String username);

    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

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
     * 查询用户信息
     *
     * @param page    分页参数
     * @param keyword 关键字
     * @return 用户信息
     */
    IPage<UserVO> qryUserInfo(Page page, @Param("keyword") String keyword);

    @Select("SELECT * FROM sys_user WHERE `name` = 'sysadmin'")
    List<User> getSysAdmin();

    String getUsersEmail(@Param("userIds") List<Long> userIds);

    String getUsersPhone(@Param("userIds") List<Long> userIds);

    List<String> getUsersPhoneList(@Param("userIds") List<Long> userIds);


    @Select("select COLUMN_NAME name,case when DATA_TYPE='varchar' then 'String' when DATA_TYPE='bigint' then 'long' when DATA_TYPE='datetime' then 'date' else 'int' end  type,if(IS_NULLABLE='NO',1,0) required,0 multivalued,COLUMN_COMMENT comment from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")
    List<BimSchemaChildResp> bimSchemaService(String tableName);

//    @Insert("insert into sys_user(name,account) values(#{name},#{account})")
//    void save(User user);
//    @Update("update sys_user set name=#{name},age=#{account} where id=#{id}")
//    boolean update(User user);
//    @Delete("delete from sys_user where id=#{id}")
//    boolean delete(int id);
//    @Select("select * from sys_user where id=#{id}")
//    User findById(int id);
//    @Select("select * from sys_user where name like #{name} and sex >= #{sex}")
//    List<User> findAll(User u);
}

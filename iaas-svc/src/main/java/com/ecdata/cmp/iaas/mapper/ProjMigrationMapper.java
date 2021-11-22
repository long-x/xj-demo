package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ecdata.cmp.iaas.entity.transferCloud.ProjMigration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/19 15:29,
 */
@Mapper
@Repository
public interface ProjMigrationMapper extends BaseMapper<ProjMigration> {

    @Select("select count(1) as number ,'项目总量' as type from proj_migration\n" +
            "union all\n" +
            "select IFNULL(sum(number),0) as numbers ,'资源未分配' as type from(\n" +
            "select IFNULL(\n" +
            "(select count(`status`) from proj_migration where `status`=0) ,0) as number,'未立项' as type  \n" +
            "union all\n" +
            "select IFNULL(\n" +
            "(select count(`status`) from proj_migration where `status`=1) ,0) as number,'已立项' as type \n" +
            ") as unres\n" +
            "union all\n" +
            "\n" +
            "select ifnull(sum(number),0) as numbers ,'资源已分配' as type  from(\n" +
            "select IFNULL(\n" +
            "(select count(`status`) from proj_migration where `status`=2),0) as number ,'分配资源' as type \n" +
            "union all\n" +
            "select IFNULL(\n" +
            "(select count(`status`) from proj_migration where `status`=3),0) as number ,'安全策略' as type \n" +
            "union all\n" +
            "select IFNULL(\n" +
            "(select count(`status`) from proj_migration where `status`=4),0) as number ,'应用迁移' as type \n" +
            "union all\n" +
            "select IFNULL(\n" +
            "(select count(`status`) from proj_migration where `status`=5),0) as number ,'完成' as type \n" +
            ") as res\n" +
            "\n")
    List<Map<String,Object>> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<ProjMigration> wrapper);


    @Select("select distinct supervisor_name from proj_migration")
    List<String> selectSupervisorNames();

    @Select("select distinct status from proj_migration order by status")
    List<String> selectStatus();

}

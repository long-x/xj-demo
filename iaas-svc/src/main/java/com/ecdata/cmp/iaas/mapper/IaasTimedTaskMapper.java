package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasTimedTask;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTimedTaskVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/29 16:25
 * @modified By：
 */
@Mapper
@Repository
public interface IaasTimedTaskMapper extends BaseMapper<IaasTimedTask> {


    List<IaasVirtualMachineVO> getVMByGropu(@Param("id") String id);

    List<BareMetalVO> getBareByGropu(@Param("id") String id);

    IaasTimedTaskVO getTaskInfo(@Param("id") Long id);


    IPage<IaasTimedTaskVO> getIaasTimedTaskVOPage(Page<IaasTimedTaskVO> page, @Param("map") Map map);


    List<IaasTimedTaskVO> onOrOffTaskList(@Param("map") Map map);

}

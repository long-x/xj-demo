package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasTimedTask;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTimedTaskVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/29 16:21
 * @modified By：
 */
public interface IaasTimedTaskService extends IService<IaasTimedTask> {

    boolean add(IaasTimedTaskVO entity);


    List<IaasVirtualMachineVO> getVMByGropu(String id);

    List<BareMetalVO> getBareByGropu(String id);

    boolean delTask(Long id) throws IOException;

    IaasTimedTaskVO getTaskInfo(Long id);


    IPage<IaasTimedTaskVO> getIaasTimedTaskVOPage(Page<IaasTimedTaskVO> page,Map map);


    List<IaasTimedTaskVO> onOrOffTaskList(Map map);
}

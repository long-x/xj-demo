package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachine;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 10:50
 * @modified By：
 */
public interface IaasVirtualMachineService extends IService<IaasVirtualMachine> {


    /**
     * 条件查询虚拟机资源使用情况
     *
     * @param map
     * @return
     */
    IPage<IaasVirtualMachineVO> selectIaasVirtualMachineAll(Page<IaasVirtualMachineVO> page, @Param("map") Map<String, Object> map);


    /**
     * 根据id查询虚拟机资源使用情况
     */
    IaasVirtualMachineVO qrtIaasVirtualMachineInfo(@Param("id") String id);


    /**
     * 条件查询虚拟机优化
     */
    List<String> qrMachineOptimize(@Param("map") Map<String, Object> map);

    IPage<IaasVirtualMachineVO> selectByIds(Page<IaasVirtualMachineVO> page, Map<String, Object> map);

    /**
     * 根据申请流程id查出虚拟机信息
     *
     * @param processApplyVmId
     * @return
     */
    IaasVirtualMachineVO queryMachinesByProcessApplyVmId(Long processApplyVmId);


    /**
     * 根据map里poolId查询
     * @param page
     * @param map
     * @return
     */
    IPage<IaasVirtualMachineVO> getVirtualMachineVOPage(Page<IaasVirtualMachineVO> page, @Param("map")Map map);




    List<IaasVirtualMachine> qryVMforTemplate();


    List<IaasVirtualMachineVO> getVirtualMachineVOPage(Map map);



    //根据vmkey查询虚拟机id
    boolean getVmByKey(List<String> list);


    /**
     * 云上系统统计表
     * @return
     */
    List<VMGroupVO> VmStatisticalList();


}

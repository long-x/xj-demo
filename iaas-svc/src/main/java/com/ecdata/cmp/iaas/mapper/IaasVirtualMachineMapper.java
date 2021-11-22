package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachine;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.vm.BMGroupVO;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/23 15:20
 * @modified By：
 */
@Mapper
@Repository
public interface IaasVirtualMachineMapper extends BaseMapper<IaasVirtualMachine> {
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

    IPage<IaasVirtualMachineVO> selectByIds(Page<IaasVirtualMachineVO> page, @Param("map") Map<String, Object> map);


    /**
     * 根据申请流程id查出虚拟机信息
     *
     * @param processApplyVmId
     * @return
     */
    IaasVirtualMachineVO queryMachinesByProcessApplyVmId(@Param("processApplyVmId") Long processApplyVmId);

    /**
     * 根据map里poolId查询
     *
     * @param page
     * @param map
     * @return
     */
    IPage<IaasVirtualMachineVO> getVirtualMachineVOPage(Page<IaasVirtualMachineVO> page, @Param("map") Map map);

    /**
     * 根据虚拟机key获取虚拟机信息
     *
     * @param key
     * @return
     */
    IaasVirtualMachine queryIaasVirtualMachineByKey(String key);


    /**
     * template中添加的虚拟机列表
     *
     * @return
     */
    List<IaasVirtualMachine> qryVMforTemplate();

    /**
     * 查询出项目下同步过的虚拟机
     *
     * @return
     */
    List<Map<String, Object>> queryVDCAndProjectInfo();

    List<IaasVirtualMachine> queryVMByHostId(Long hostId);

    /**
     * 纳管获取所有本地虚拟机
     * @param map
     * @return
     */
    List<IaasVirtualMachineVO> getVirtualMachineVOPage(@Param("map") Map map);



    /**
     * 根据vmkey查询虚拟机id
     */
    List<IaasVirtualMachineVO> getIdBykey(@Param("list") List list);

    /**
     * 删除虚拟机
     */
    void removeVm(long id);

    /**
     * 删除虚拟机网络表
     */
    void removeNetwork(long id);

    /**
     * 删除磁盘表
     */
    void removeDisk(long id);


    /**
     * 云上系统统计表
     * @return
     */
    List<VMGroupVO> VmStatisticalList();


    List<BMGroupVO> BmStatisticalList();
}

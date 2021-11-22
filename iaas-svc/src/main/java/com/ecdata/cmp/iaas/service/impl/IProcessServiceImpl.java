package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.activiti.client.ActivitiClient;
import com.ecdata.cmp.activiti.dto.ProcessDTO;
import com.ecdata.cmp.activiti.dto.request.CompleteTaskRequest;
import com.ecdata.cmp.activiti.dto.response.ActTaskPageResponse;
import com.ecdata.cmp.activiti.dto.vo.ActTaskVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasResourcePool;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachine;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineDisk;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineNetwork;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineDiskVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApply;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachine;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponent;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponentOperation;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponentParam;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponentScript;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineDisk;
import com.ecdata.cmp.iaas.mapper.IaasResourcePoolMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineDiskMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineNetworkMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineComponentScriptMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyVirtualMachineComponentMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyVirtualMachineComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyVirtualMachineComponentParamMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyVirtualMachineComponentScriptMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyVirtualMachineDiskMapper;
import com.ecdata.cmp.iaas.mapper.process.IaasProcessApplyVirtualMachineMapper;
import com.ecdata.cmp.iaas.service.IProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-25 11:17
 */
@Slf4j
@Service
public class IProcessServiceImpl implements IProcessService {

    @Autowired
    private IaasProcessApplyMapper iaasProcessApplyMapper;

    @Autowired
    private IaasVirtualMachineMapper iaasVirtualMachineMapper;

    @Autowired
    private IaasVirtualMachineDiskMapper iaasVirtualMachineDiskMapper;

    @Autowired
    private IaasVirtualMachineNetworkMapper iaasVirtualMachineNetworkMapper;

    @Autowired
    private IaasProcessApplyVirtualMachineMapper iaasProcessApplyVirtualMachineMapper;

    @Autowired
    private IaasProcessApplyVirtualMachineComponentMapper iaasProcessApplyVirtualMachineComponentMapper;

    @Autowired
    private IaasProcessApplyVirtualMachineDiskMapper iaasProcessApplyVirtualMachineDiskMapper;

    @Autowired
    private IaasProcessApplyVirtualMachineComponentParamMapper iaasProcessApplyVirtualMachineComponentParamMapper;

    @Autowired
    private IaasProcessApplyVirtualMachineComponentScriptMapper iaasProcessApplyVirtualMachineComponentScriptMapper;

    @Autowired
    private IaasProcessApplyVirtualMachineComponentOperationMapper iaasProcessApplyVirtualMachineComponentOperationMapper;

    @Autowired
    private IaasCatalogVirtualMachineComponentScriptMapper iaasCatalogVirtualMachineComponentScriptMapper;

    @Autowired
    private IaasCatalogVirtualMachineComponentOperationMapper iaasCatalogVirtualMachineComponentOperationMapper;

    @Autowired
    private IaasResourcePoolMapper iaasResourcePoolMapper;

    @Autowired
    private ActivitiClient activitiClient;

    /**
     * 申请保存  保存并发起申请
     *
     * @param processApplyVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveProcess(IaasProcessApplyVO processApplyVO) {
        BaseResponse baseResponse = new BaseResponse();
        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //保存流程申请表
        long processApplyId = SnowFlakeIdGenerator.getInstance().nextId();
        saveProcessApply(processApplyId, userId, now, processApplyVO);

        //处理虚拟机，组件等相关信息 todo 审核通过后,要创建虚拟机
        saveVirtualMachineList(processApplyId, userId, now, processApplyVO);

        //组装businessDetail {id:"ddd",name:""ss}
        String businessDetail = businessDetail(processApplyVO);
        //调用流程
        callActiviti(processApplyId,
                businessDetail,
                userId,
                now,
                baseResponse,
                processApplyVO);

        //资源池预留 计算虚拟机
        if (processApplyVO.getState() == 1) {
            handleReserveResourcePool(processApplyVO);
        }
        return baseResponse;
    }

    @Override
    public IPage<IaasProcessApplyVO> queryProcessApply(Page<IaasProcessApplyVO> page, Map<String, Object> params) {
        return iaasProcessApplyMapper.queryProcessApply(page, params);
    }

    @Override
    public List<IaasProcessApplyVO> queryCurrentUserProcessApply(Map<String, Object> params) {
        return iaasProcessApplyMapper.queryCurrentUserProcessApply(params);
    }

    @Override
    public IaasProcessApplyVO queryProcessApplyDetails(Long processApplyId) {
        IaasProcessApplyVO queryProcessApplyVO = iaasProcessApplyMapper.queryProcessApplyDetails(processApplyId);
        if (queryProcessApplyVO != null) {
            List<IaasProcessApplyVirtualMachineVO> catalogVirtualMachinVOList = queryProcessApplyVO.getCatalogVirtualMachinVOList();
            if (CollectionUtils.isNotEmpty(catalogVirtualMachinVOList)) {
                for (IaasProcessApplyVirtualMachineVO machineVO : catalogVirtualMachinVOList) {
                    List<IaasProcessApplyVirtualMachineComponentVO> children = machineVO.getChildren();
                    if (CollectionUtils.isEmpty(children)) {
                        continue;
                    }
                    for (IaasProcessApplyVirtualMachineComponentVO machineComponentVO : children) {
//                        machineComponentVO.setCompScripts(iaasProcessApplyVirtualMachineComponentScriptMapper.queryScripsByComponentId(machineComponentVO.getId()));
//                        machineComponentVO.setCompOps(iaasProcessApplyVirtualMachineComponentOperationMapper.queryOperationByComponentId(machineComponentVO.getId()));
                        setValueSelect(machineComponentVO.getCompParams());

                        List<IaasProcessApplyVirtualMachineComponentVO> children1 = machineComponentVO.getChildren();
                        if (CollectionUtils.isEmpty(children1)) {
                            continue;
                        }
                        for (IaasProcessApplyVirtualMachineComponentVO machineComponentVO1 : children1) {
//                            machineComponentVO1.setCompScripts(iaasProcessApplyVirtualMachineComponentScriptMapper.queryScripsByComponentId(machineComponentVO1.getId()));
//                            machineComponentVO1.setCompOps(iaasProcessApplyVirtualMachineComponentOperationMapper.queryOperationByComponentId(machineComponentVO1.getId()));
                            setValueSelect(machineComponentVO1.getCompParams());
                        }
                    }
                }
            }
        }

        return queryProcessApplyVO;
    }

    /**
     * 审核编辑  保存 保存并发起申请
     *
     * @param processApplyVO
     * @return
     */
    @Override
    public BaseResponse editProcess(IaasProcessApplyVO processApplyVO) {
        BaseResponse response = new BaseResponse();
        List<IaasProcessApplyVirtualMachineVO> vos = processApplyVO.getCatalogVirtualMachinVOList();

        if (CollectionUtils.isEmpty(vos)) {
            response.setCode(201);
            response.setMessage("保存数据不能为空！");
            log.error("保存数据不能为空！");
            return response;
        }

        //获取模板信息
        Long processId = processApplyVO.getId();
        Integer state = processApplyVO.getState();
        if (processId == null || state == null) {
            response.setCode(201);
            response.setMessage("服务申请或服务申请状态不能为空！");
            log.error("服务申请id或服务申请状态不能为空！");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //更新模板信息
        updateIaasProcess(processId, state, userId, now, processApplyVO);

        //删除模板下面的虚拟机，组件，组件参数信息
        deleteVirtualMachine(vos);

        //保存模板下面虚拟机信息
        saveVirtualMachineList(processId, userId, now, processApplyVO);

        //编辑的时候，就不用再调开始任务接口了。之前提交的时候已经掉过一次了，已经生产一条新实例了。
        if (processApplyVO.getState() == 1) {
            //提交并发起申请
            completeActiviti(processApplyVO, businessDetail(processApplyVO), response);

            //资源池预留 计算虚拟机
            handleReserveResourcePool(processApplyVO);
        }

        return response;
    }

    /**
     * 已申请服务  申请
     *
     * @param processApplyVO
     * @return
     */
    @Override
    public BaseResponse updateProcess(IaasProcessApplyVO processApplyVO) {
        BaseResponse baseResponse = new BaseResponse();
        //申请：1.只保存没有发起过申请；
        if (processApplyVO.getState() == 1) {
            completeActiviti(processApplyVO, "", baseResponse);

            IaasProcessApply processApply = new IaasProcessApply();
            processApply.setId(processApplyVO.getId());
            processApply.setState(processApplyVO.getState());
            processApply.setUpdateTime(DateUtil.getNow());
            processApply.setUpdateUser(Sign.getUserId());

            iaasProcessApplyMapper.updateById(processApply);

            //通过申请id，查询出虚拟机信息
            IaasProcessApplyVO iaasProcessApplyVO = iaasProcessApplyMapper.queryProcessApplyDetails(processApplyVO.getId());

            //资源池预留 计算虚拟机
            handleReserveResourcePool(iaasProcessApplyVO);
        }
        return baseResponse;
    }

    /**
     * 已申请服务 删除
     *
     * @param processApplyId
     * @param processInstanceId
     * @param deleteReason
     */
    @Override
    public void deleteProcess(Long processApplyId, String processInstanceId, String deleteReason) {
        IaasProcessApplyVO treeResponses = iaasProcessApplyMapper.queryProcessApplyDetails(processApplyId);

        //删除虚拟机组件等其他信息
        deleteVirtualMachine(treeResponses.getCatalogVirtualMachinVOList());

        //删除服务目录信息
        iaasProcessApplyMapper.deleteById(processApplyId);

        //删除流程实例,提交状态下才让其删除。todo 是否删除历史流程
        activitiClient.deleteProcessInstance(AuthContext.getAuthz(), processInstanceId, deleteReason, 1);
    }

    private void handleReserveResourcePool(IaasProcessApplyVO processApplyVO) {
        List<IaasProcessApplyVirtualMachineVO> virtualMachinVOList = processApplyVO.getCatalogVirtualMachinVOList();
        //计算虚拟机数量  虚拟机cpu 内存  物理内存

        IaasResourcePool savePool = new IaasResourcePool();

        savePool.setId(processApplyVO.getPoolId());
        savePool.setUpdateUser(Sign.getUserId());
        savePool.setUpdateTime(DateUtil.getNow());
        savePool.setMemoryReservedAllocate(processApplyVO.cpuSum());
        savePool.setVcpuReservedAllocate(processApplyVO.memorySum());
        savePool.setVmReservedAllocate(virtualMachinVOList.size());

        iaasResourcePoolMapper.updateById(savePool);
    }

    private String businessDetail(IaasProcessApplyVO vo) {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", vo.getId());
        map.put("catalogId", vo.getCatalogId());
        map.put("poolId", vo.getPoolId());
        map.put("providerId", vo.getProviderId());
        map.put("areaId", vo.getAreaId());
        map.put("clusterId", vo.getClusterId());
        map.put("hostId", vo.getHostId());
        map.put("datastoreId", vo.getDatastoreId());
        map.put("businessGroupId", vo.getBusinessGroupId());
        map.put("processApplyName", vo.getProcessApplyName());
        map.put("businessActivitiId", vo.getBusinessActivitiId());
        map.put("businessActivitiName", vo.getBusinessActivitiName());
        map.put("state", vo.getState());
        map.put("lease", vo.getLease());
        map.put("period", vo.getPeriod());
        map.put("remark", vo.getRemark());
        map.put("createUser", Sign.getUserId());
        map.put("createUserName", Sign.getUserDisplayName());
        map.put("createTime", DateUtil.getNow());
//        map.put("processInstanceId", vo.getProcessApplyName());

        return JSONObject.toJSONString(map);
    }

    private BaseResponse completeActiviti(IaasProcessApplyVO processApplyVO, String businessDetail, BaseResponse baseResponse) {
        //先查询到taskId,然后再调用完成接口
        ActTaskPageResponse actTaskPageResponse = activitiClient.queryUserTask(1, 1, Sign.getUserId(), processApplyVO.getProcessInstanceId(), null, null, false);

        if (actTaskPageResponse == null
                || actTaskPageResponse.getCode() != 0
                || actTaskPageResponse.getData() == null
                || CollectionUtils.isEmpty(actTaskPageResponse.getData().getData())) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("获取taskId异常");
            return baseResponse;
        }

        ActTaskVO actTaskVO = actTaskPageResponse.getData().getData().get(0);

        CompleteTaskRequest completeTask = new CompleteTaskRequest();
        completeTask.setTaskId(actTaskVO.getId());
        completeTask.setBusinessDetail(businessDetail);
        completeTask.setOutcome("提交");
        completeTask.setComment("测试");

        Map<String, Object> params = new HashMap<>();
        params.put("businessGroupId", processApplyVO.getBusinessGroupId());
        params.put("businessGroupName", processApplyVO.getBusinessGroupName());
        params.put("processName", processApplyVO.getProcessApplyName());

        //调用完成任务接口，走下一步
        JSONObjectResponse jsonObjectResponse = activitiClient.completeTask(AuthContext.getAuthz(), completeTask);

        if (jsonObjectResponse == null
                || jsonObjectResponse.getCode() != 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("完成流程失败！");
            return baseResponse;
        }

        return baseResponse;
    }

    private BaseResponse callActiviti(long processApplyId,
                                      String businessDetail,
                                      Long userId,
                                      Date now,
                                      BaseResponse baseResponse,
                                      IaasProcessApplyVO vo) {

        //审批流程
        JSONObjectResponse jsonObjectResponse = activitiClient.startProcess(assemblerProcessDTO(processApplyId, vo.getBusinessActivitiId(), vo.getBusinessGroupId(), vo.getBusinessGroupName(), vo.getProcessApplyName(), vo.getState(), businessDetail, vo.getBusinessActivitiName()));

        //流程id更新到服务申请表里
        if (jsonObjectResponse != null && jsonObjectResponse.getData() != null && StringUtils.isNotBlank(jsonObjectResponse.getData().getString("processInstanceId"))) {
            IaasProcessApply processApply = new IaasProcessApply();
            processApply.setId(processApplyId);
            processApply.setState(vo.getState());
            processApply.setProcessInstanceId(jsonObjectResponse.getData().getString("processInstanceId"));
            processApply.setUpdateTime(now);
            processApply.setUpdateUser(userId);
            iaasProcessApplyMapper.updateById(processApply);

            baseResponse.setCode(0);
        } else {
            baseResponse.setCode(201);
            baseResponse.setMessage("发起流程失败！");
        }
        return baseResponse;
    }

    private ProcessDTO assemblerProcessDTO(long processApplyId,
                                           Long processInstanceId,
                                           Long businessGroupId,
                                           String businessGroupName,
                                           String processName,
                                           int state,
                                           String businessDetail,
                                           String businessActivitiName) {
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setApplyFlag(state);//申请标志位(0:不发起申请;1:发起申请;)
        processDTO.setBusinessId(processApplyId);//业务id
        processDTO.setProcessWorkflowId(processInstanceId);//流程工作流id 业务组对应的几级审批流程
        processDTO.setBusinessDetail(businessDetail);//业务详情
        processDTO.setComment("测试流程申请");//发起申请时的意见
        processDTO.setProcessOperation(businessActivitiName);//自定义流程操作,（暂时取用业务组关联的流程名称） 如:apply
        processDTO.setBusinessGroupId(businessGroupId);
        processDTO.setBusinessGroupName(businessGroupName);
        processDTO.setProcessName(processName);

//        processDTO.setProcessDefinitionKey();//流程定义键
//        processDTO.setNotifyMessage();//发起申请时,审批人接收到的通知消息
//        processDTO.setNotifyDetail();//发起申请时,审批人接收到的通知详情
//        processDTO.setOutcome();//发起申请时的结果
//        processDTO.setProcessObject();//自定义流程对象, 如:process
        return processDTO;
    }

    private void deleteVirtualMachine(List<IaasProcessApplyVirtualMachineVO> vos) {
        for (IaasProcessApplyVirtualMachineVO machineTreeResponse : vos) {
            //删除虚拟机
            Long machineId = machineTreeResponse.getKey();
            iaasProcessApplyVirtualMachineMapper.deleteById(machineId);

            //删除虚拟机磁盘
            List<IaasProcessApplyVirtualMachineDiskVO> machineDiskVOList = machineTreeResponse.getMachineDiskVOList();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasProcessApplyVirtualMachineDiskVO diskVO : machineDiskVOList) {
                    iaasProcessApplyVirtualMachineDiskMapper.deleteById(diskVO.getId());
                }
            }

            //删除组件
            List<IaasProcessApplyVirtualMachineComponentVO> componentTreeResponses = machineTreeResponse.getChildren();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasProcessApplyVirtualMachineComponentVO parentcomponent : componentTreeResponses) {
                    iaasProcessApplyVirtualMachineComponentMapper.deleteById(parentcomponent.getKey());
                    //删除组件参数
                    iaasProcessApplyVirtualMachineComponentParamMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //删除组件脚本
                    iaasProcessApplyVirtualMachineComponentScriptMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //删除组件操作
                    iaasProcessApplyVirtualMachineComponentOperationMapper.deleteByVmComponentId(parentcomponent.getKey());

                    List<IaasProcessApplyVirtualMachineComponentVO> childrenTreeResponses = parentcomponent.getChildren();
                    if (CollectionUtils.isNotEmpty(childrenTreeResponses)) {
                        for (IaasProcessApplyVirtualMachineComponentVO childrenComponent : childrenTreeResponses) {
                            iaasProcessApplyVirtualMachineComponentMapper.deleteById(childrenComponent.getKey());
                            //删除组件参数
                            iaasProcessApplyVirtualMachineComponentParamMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //删除组件脚本
                            iaasProcessApplyVirtualMachineComponentScriptMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //删除组件操作
                            iaasProcessApplyVirtualMachineComponentOperationMapper.deleteByVmComponentId(childrenComponent.getKey());
                        }
                    }
                }
            }
        }
    }

    private void updateIaasProcess(Long processId, Integer state, Long userId, Date now, IaasProcessApplyVO processApplyVO) {
        IaasProcessApply processApply = new IaasProcessApply();
        BeanUtils.copyProperties(processApplyVO, processApply);
        processApply.setId(processId);
        processApply.setState(state);
        processApply.setUpdateUser(userId);
        processApply.setUpdateTime(now);

        iaasProcessApplyMapper.updateById(processApply);
    }

    private void setValueSelect(List<IaasProcessApplyVirtualMachineComponentParamVO> compParams) {
        for (IaasProcessApplyVirtualMachineComponentParamVO param : compParams) {
            String valueList = param.getValueList();
            if (StringUtils.isNotBlank(valueList)) {
                param.setValueSelect(JSON.parseArray(valueList));
            }
        }
    }

    private void saveVirtualMachineList(long processApplyId, Long userId, Date now, IaasProcessApplyVO processApplyVO) {
        //拿出所有的虚拟机进行排序
        List<IaasProcessApplyVirtualMachineVO> virtualMachinVOList = processApplyVO.getCatalogVirtualMachinVOList();
        if (CollectionUtils.isEmpty(virtualMachinVOList)) {
            log.info("虚拟机信息为空!");
            return;
        }

        virtualMachinVOList.sort(Comparator.comparing(IaasProcessApplyVirtualMachineVO::getSort));

        //循环保存虚拟机，并把保存生成的id放入到对应的组件中
        for (IaasProcessApplyVirtualMachineVO saveMachineVo : virtualMachinVOList) {
            long machineId = SnowFlakeIdGenerator.getInstance().nextId();
            //保存虚拟机信息
            saveMachine(machineId, processApplyId, userId, now, saveMachineVo);

            //保存虚拟机磁盘信息
            saveVirtualMachineDiskList(machineId, userId, now, saveMachineVo);

            //todo 保存创建成功后的虚拟机信息，暂时写死，后期接受MQ的创建成功的消息
            saveMQResponseMachineInfo(machineId, userId, now);
        }

        //拿出所有的父组件进行排序，进行保存
        List<IaasProcessApplyVirtualMachineComponentVO> sortParentEdMachineComponentVOS = new ArrayList<>();
        for (IaasProcessApplyVirtualMachineVO machineVO : virtualMachinVOList) {
            List<IaasProcessApplyVirtualMachineComponentVO> parentMachineComponentVOS = machineVO.getChildren();
            if (CollectionUtils.isNotEmpty(parentMachineComponentVOS)) {
                sortParentEdMachineComponentVOS.addAll(parentMachineComponentVOS);
            }
        }

        if (CollectionUtils.isEmpty(sortParentEdMachineComponentVOS)) {
            log.info("虚拟机组件信息为空!");
            return;
        }
        sortParentEdMachineComponentVOS.sort(Comparator.comparing(IaasProcessApplyVirtualMachineComponentVO::getSort));

        //循环保存组件信息，并把保存生成的组件id和虚拟机id放入到子组件中
        for (IaasProcessApplyVirtualMachineComponentVO saveComponentVO : sortParentEdMachineComponentVOS) {
            saveComponent(userId, now, saveComponentVO);
        }

        //拿出所有的子组件进行排序，进行保存
        List<IaasProcessApplyVirtualMachineComponentVO> sortedChildrenEdMachineComponentVOS = new ArrayList<>();
        for (IaasProcessApplyVirtualMachineComponentVO machineComponentVO : sortParentEdMachineComponentVOS) {
            List<IaasProcessApplyVirtualMachineComponentVO> childrenComponentVOS = machineComponentVO.getChildren();
            if (CollectionUtils.isNotEmpty(childrenComponentVOS)) {
                sortedChildrenEdMachineComponentVOS.addAll(childrenComponentVOS);
            }
        }
        sortedChildrenEdMachineComponentVOS.sort(Comparator.comparing(IaasProcessApplyVirtualMachineComponentVO::getSort));

        //循环保存子组件信息
        if (CollectionUtils.isNotEmpty(sortedChildrenEdMachineComponentVOS)) {
            for (IaasProcessApplyVirtualMachineComponentVO saveComponentVO : sortedChildrenEdMachineComponentVOS) {
                saveComponent(userId, now, saveComponentVO);
            }
        }
    }

    private void saveMQResponseMachineInfo(long machineId, Long userId, Date now) {
        IaasVirtualMachine machine = new IaasVirtualMachine();
        long id = SnowFlakeIdGenerator.getInstance().nextId();
        machine.setId(id);
        machine.setProcessApplyVmId(machineId);
        machine.setStatus(0);
        machine.setCreateUser(userId);
        machine.setCreateTime(now);
        machine.setUpdateTime(now);
        machine.setUpdateUser(userId);
        machine.setDeleted(false);

        iaasVirtualMachineMapper.insert(machine);
        log.info("保存返回成功的虚拟机信息");

        IaasVirtualMachineDisk machineDisk = new IaasVirtualMachineDisk();
        machineDisk.setId(SnowFlakeIdGenerator.getInstance().nextId());
        machineDisk.setVmId(id);
        machineDisk.setDiskName("虚拟机名称");
        machineDisk.setDiskType("磁盘类型");
        machineDisk.setDiskTotal(100.00);
        machineDisk.setDiskUsed(30.00);
        machineDisk.setCreateUser(userId);
        machineDisk.setCreateTime(now);
        machineDisk.setUpdateTime(now);
        machineDisk.setUpdateUser(userId);
        machineDisk.setDeleted(false);
        iaasVirtualMachineDiskMapper.insert(machineDisk);
        log.info("保存返回成功的虚拟机磁盘信息");

        IaasVirtualMachineNetwork machineNetwork = new IaasVirtualMachineNetwork();
        machineNetwork.setId(SnowFlakeIdGenerator.getInstance().nextId());
        machineNetwork.setVmId(id);
        machineNetwork.setGateway("gateway");
        machineNetwork.setIpAddress("10.10.11.112");
        machineNetwork.setDns("dns");
        machineNetwork.setCidr("cidr");
        machineNetwork.setCreateUser(userId);
        machineNetwork.setCreateTime(now);
        machineNetwork.setUpdateTime(now);
        machineNetwork.setUpdateUser(userId);
        machineNetwork.setIsDeleted(0);

        iaasVirtualMachineNetworkMapper.insert(machineNetwork);
        log.info("保存返回成功的虚拟机磁盘信息!");
    }

    private void saveVirtualMachineDiskList(long machineId, Long userId, Date now, IaasProcessApplyVirtualMachineVO saveMachineVo) {

        List<IaasProcessApplyVirtualMachineDiskVO> machineDiskVOS = saveMachineVo.getMachineDiskVOList();

        if (CollectionUtils.isEmpty(machineDiskVOS)) {
            log.error("虚拟机磁盘为空!");
            return;
        }

        for (IaasProcessApplyVirtualMachineDiskVO diskVO : machineDiskVOS) {
            if (diskVO == null) {
                continue;
            }
            saveVirtualMachineDisk(machineId, userId, now, diskVO);
        }
    }

    private void saveVirtualMachineDisk(Long machineId, Long userId, Date now, IaasProcessApplyVirtualMachineDiskVO diskVO) {
        IaasProcessApplyVirtualMachineDisk disk = new IaasProcessApplyVirtualMachineDisk();
        BeanUtils.copyProperties(diskVO, disk);

        disk.setId(SnowFlakeIdGenerator.getInstance().nextId());
        disk.setVmId(machineId);
        disk.setCreateUser(userId);
        disk.setCreateTime(now);
        disk.setUpdateTime(now);
        disk.setUpdateUser(userId);
        disk.setIsDeleted(0);

        iaasProcessApplyVirtualMachineDiskMapper.insert(disk);
        log.info("保存虚拟机磁盘信息成功！");
    }

    private void saveComponent(Long userId, Date now, IaasProcessApplyVirtualMachineComponentVO saveComponentVO) {
        IaasProcessApplyVirtualMachineComponent component = new IaasProcessApplyVirtualMachineComponent();
        BeanUtils.copyProperties(saveComponentVO, component);

        long componentId = SnowFlakeIdGenerator.getInstance().nextId();
        component.setId(componentId);
        component.setDisplayName(saveComponentVO.getTitle());
        component.setComponentId(saveComponentVO.getKey());
        component.setCreateUser(userId);
        component.setCreateTime(now);
        component.setUpdateTime(now);
        component.setUpdateUser(userId);
        component.setIsDeleted(0);

        iaasProcessApplyVirtualMachineComponentMapper.insert(component);
        log.info("保存虚拟机组件信息成功！");

        //把虚拟机id和组件id放入到子组件信息中
        List<IaasProcessApplyVirtualMachineComponentVO> children = saveComponentVO.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            for (IaasProcessApplyVirtualMachineComponentVO componentVO : children) {
                componentVO.setProcessApplyVmId(saveComponentVO.getProcessApplyVmId());
                componentVO.setParentId(componentId);
            }
        }

        //保存组件参数信息
        saveVirtualMachineComponentParamList(componentId, userId, now, saveComponentVO);

        //保存组件脚本信息
        saveVirtualMachineComponentScriptList(componentId, userId, now, saveComponentVO.getKey());
    }

    private void saveVirtualMachineComponentScriptList(long componentId, Long userId, Date now, Long historyComponentId) {
        List<IaasProcessApplyVirtualMachineComponentScriptVO> compScripts = iaasCatalogVirtualMachineComponentScriptMapper.queryScripsByComponentId(historyComponentId);

        if (CollectionUtils.isEmpty(compScripts)) {
            return;
        }

        for (IaasProcessApplyVirtualMachineComponentScriptVO scriptVO : compScripts) {
            if (scriptVO == null) {
                continue;
            }

            long componentScriptId = SnowFlakeIdGenerator.getInstance().nextId();

            //保存组件脚本信息
            saveVirtualMachineComponentScript(componentScriptId, componentId, userId, now, scriptVO);

            //查询出历史操作信息
            //保存组件操作信息
            Long scriptId = scriptVO.getId();
            List<IaasProcessApplyVirtualMachineComponentOperationVO> compOps = iaasCatalogVirtualMachineComponentOperationMapper.queryOperationByScriptId(scriptId);
            if (CollectionUtils.isNotEmpty(compOps)) {
                for (IaasProcessApplyVirtualMachineComponentOperationVO operationVO : compOps) {
                    if (operationVO == null) {
                        continue;
                    }
                    saveVirtualMachineComponentOperation(componentScriptId, componentId, userId, now, operationVO);
                }
            }
        }
    }

    private void saveVirtualMachineComponentOperation(long componentScriptId, long componentId, Long userId, Date now, IaasProcessApplyVirtualMachineComponentOperationVO operationVO) {
        IaasProcessApplyVirtualMachineComponentOperation componentOperation = new IaasProcessApplyVirtualMachineComponentOperation();
        BeanUtils.copyProperties(operationVO, componentOperation);

        componentOperation.setId(SnowFlakeIdGenerator.getInstance().nextId());
        componentOperation.setOperationId(operationVO.getId());
        componentOperation.setVmComponentId(componentId);
        componentOperation.setVmComponentScriptId(componentScriptId);
        componentOperation.setCreateUser(userId);
        componentOperation.setCreateTime(now);
        componentOperation.setUpdateTime(now);
        componentOperation.setUpdateUser(userId);
        componentOperation.setIsDeleted(0);

        iaasProcessApplyVirtualMachineComponentOperationMapper.insert(componentOperation);
        log.info("保存虚拟机组件脚本信息成功！");
    }

    private void saveVirtualMachineComponentScript(long componentScriptId, long componentId, Long userId, Date now, IaasProcessApplyVirtualMachineComponentScriptVO scriptVO) {
        IaasProcessApplyVirtualMachineComponentScript componentScript = new IaasProcessApplyVirtualMachineComponentScript();
        BeanUtils.copyProperties(scriptVO, componentScript);

        componentScript.setId(componentScriptId);
        componentScript.setVmComponentId(componentId);
        componentScript.setCreateUser(userId);
        componentScript.setCreateTime(now);
        componentScript.setUpdateTime(now);
        componentScript.setUpdateUser(userId);
        componentScript.setIsDeleted(0);
        iaasProcessApplyVirtualMachineComponentScriptMapper.insert(componentScript);
        log.info("保存组件脚本信息成功!");
    }

    private void saveVirtualMachineComponentParamList(long componentId, Long userId, Date now, IaasProcessApplyVirtualMachineComponentVO saveComponentVO) {
        List<IaasProcessApplyVirtualMachineComponentParamVO> compParams = saveComponentVO.getCompParams();
        if (CollectionUtils.isNotEmpty(compParams)) {
            for (IaasProcessApplyVirtualMachineComponentParamVO componentParamVO : compParams) {
                if (componentParamVO == null) {
                    continue;
                }
                Integer isShow = componentParamVO.getIsShow();
                if (isShow == 1) {
                    saveVirtualMachineComponentParam(componentId, userId, now, componentParamVO);
                }
            }
        }
    }

    private void saveVirtualMachineComponentParam(long componentId, Long userId, Date now, IaasProcessApplyVirtualMachineComponentParamVO componentParamVO) {
        IaasProcessApplyVirtualMachineComponentParam componentParam = new IaasProcessApplyVirtualMachineComponentParam();
        BeanUtils.copyProperties(componentParamVO, componentParam);

        componentParam.setId(SnowFlakeIdGenerator.getInstance().nextId());
        componentParam.setVmComponentId(componentId);
        componentParam.setComponentParamId(componentParamVO.getId());
        componentParam.setCreateUser(userId);
        componentParam.setCreateTime(now);
        componentParam.setUpdateTime(now);
        componentParam.setUpdateUser(userId);
        componentParam.setIsDeleted(0);

        iaasProcessApplyVirtualMachineComponentParamMapper.insert(componentParam);
        log.info("保存组件参数信息成功!");
    }

    private void saveMachine(long machineId, long processApplyId, Long userId, Date now, IaasProcessApplyVirtualMachineVO saveMachineVo) {
        IaasProcessApplyVirtualMachine machine = new IaasProcessApplyVirtualMachine();

        BeanUtils.copyProperties(saveMachineVo, machine);
        machine.setId(machineId);
        machine.setVmName(saveMachineVo.getTitle());
        machine.setProcessApplyId(processApplyId);
        machine.setCreateUser(userId);
        machine.setCreateTime(now);
        machine.setUpdateTime(now);
        machine.setUpdateUser(userId);
        machine.setIsDeleted(0);

        iaasProcessApplyVirtualMachineMapper.insert(machine);
        log.info("保存虚拟机信息成功！");

        //把虚拟机id放入到组件信息中
        List<IaasProcessApplyVirtualMachineComponentVO> machineComponentVOS = saveMachineVo.getChildren();
        for (IaasProcessApplyVirtualMachineComponentVO componentVO : machineComponentVOS) {
            componentVO.setProcessApplyVmId(machineId);
            componentVO.setParentId(machineId);
        }
    }

    private void saveProcessApply(long processApplyId,
                                  Long userId,
                                  Date now,
                                  IaasProcessApplyVO processApplyVO) {
        IaasProcessApply processApply = new IaasProcessApply();
        BeanUtils.copyProperties(processApplyVO, processApply);
        processApply.setProcessInstanceId("");
        processApply.setId(processApplyId);
        processApply.setCreateUser(userId);
        processApply.setCreateTime(now);
        processApply.setUpdateTime(now);
        processApply.setUpdateUser(userId);
        processApply.setIsDeleted(0);

        iaasProcessApplyMapper.insert(processApply);
        log.info("保存申请流程信息成功！");
    }
}

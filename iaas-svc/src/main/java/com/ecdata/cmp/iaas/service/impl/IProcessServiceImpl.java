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
 * ??????:
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
     * ????????????  ?????????????????????
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

        //?????????????????????
        long processApplyId = SnowFlakeIdGenerator.getInstance().nextId();
        saveProcessApply(processApplyId, userId, now, processApplyVO);

        //??????????????????????????????????????? todo ???????????????,??????????????????
        saveVirtualMachineList(processApplyId, userId, now, processApplyVO);

        //??????businessDetail {id:"ddd",name:""ss}
        String businessDetail = businessDetail(processApplyVO);
        //????????????
        callActiviti(processApplyId,
                businessDetail,
                userId,
                now,
                baseResponse,
                processApplyVO);

        //??????????????? ???????????????
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
     * ????????????  ?????? ?????????????????????
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
            response.setMessage("???????????????????????????");
            log.error("???????????????????????????");
            return response;
        }

        //??????????????????
        Long processId = processApplyVO.getId();
        Integer state = processApplyVO.getState();
        if (processId == null || state == null) {
            response.setCode(201);
            response.setMessage("????????????????????????????????????????????????");
            log.error("????????????id????????????????????????????????????");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //??????????????????
        updateIaasProcess(processId, state, userId, now, processApplyVO);

        //????????????????????????????????????????????????????????????
        deleteVirtualMachine(vos);

        //?????????????????????????????????
        saveVirtualMachineList(processId, userId, now, processApplyVO);

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (processApplyVO.getState() == 1) {
            //?????????????????????
            completeActiviti(processApplyVO, businessDetail(processApplyVO), response);

            //??????????????? ???????????????
            handleReserveResourcePool(processApplyVO);
        }

        return response;
    }

    /**
     * ???????????????  ??????
     *
     * @param processApplyVO
     * @return
     */
    @Override
    public BaseResponse updateProcess(IaasProcessApplyVO processApplyVO) {
        BaseResponse baseResponse = new BaseResponse();
        //?????????1.?????????????????????????????????
        if (processApplyVO.getState() == 1) {
            completeActiviti(processApplyVO, "", baseResponse);

            IaasProcessApply processApply = new IaasProcessApply();
            processApply.setId(processApplyVO.getId());
            processApply.setState(processApplyVO.getState());
            processApply.setUpdateTime(DateUtil.getNow());
            processApply.setUpdateUser(Sign.getUserId());

            iaasProcessApplyMapper.updateById(processApply);

            //????????????id???????????????????????????
            IaasProcessApplyVO iaasProcessApplyVO = iaasProcessApplyMapper.queryProcessApplyDetails(processApplyVO.getId());

            //??????????????? ???????????????
            handleReserveResourcePool(iaasProcessApplyVO);
        }
        return baseResponse;
    }

    /**
     * ??????????????? ??????
     *
     * @param processApplyId
     * @param processInstanceId
     * @param deleteReason
     */
    @Override
    public void deleteProcess(Long processApplyId, String processInstanceId, String deleteReason) {
        IaasProcessApplyVO treeResponses = iaasProcessApplyMapper.queryProcessApplyDetails(processApplyId);

        //????????????????????????????????????
        deleteVirtualMachine(treeResponses.getCatalogVirtualMachinVOList());

        //????????????????????????
        iaasProcessApplyMapper.deleteById(processApplyId);

        //??????????????????,?????????????????????????????????todo ????????????????????????
        activitiClient.deleteProcessInstance(AuthContext.getAuthz(), processInstanceId, deleteReason, 1);
    }

    private void handleReserveResourcePool(IaasProcessApplyVO processApplyVO) {
        List<IaasProcessApplyVirtualMachineVO> virtualMachinVOList = processApplyVO.getCatalogVirtualMachinVOList();
        //?????????????????????  ?????????cpu ??????  ????????????

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
        //????????????taskId,???????????????????????????
        ActTaskPageResponse actTaskPageResponse = activitiClient.queryUserTask(1, 1, Sign.getUserId(), processApplyVO.getProcessInstanceId(), null, null, false);

        if (actTaskPageResponse == null
                || actTaskPageResponse.getCode() != 0
                || actTaskPageResponse.getData() == null
                || CollectionUtils.isEmpty(actTaskPageResponse.getData().getData())) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("??????taskId??????");
            return baseResponse;
        }

        ActTaskVO actTaskVO = actTaskPageResponse.getData().getData().get(0);

        CompleteTaskRequest completeTask = new CompleteTaskRequest();
        completeTask.setTaskId(actTaskVO.getId());
        completeTask.setBusinessDetail(businessDetail);
        completeTask.setOutcome("??????");
        completeTask.setComment("??????");

        Map<String, Object> params = new HashMap<>();
        params.put("businessGroupId", processApplyVO.getBusinessGroupId());
        params.put("businessGroupName", processApplyVO.getBusinessGroupName());
        params.put("processName", processApplyVO.getProcessApplyName());

        //???????????????????????????????????????
        JSONObjectResponse jsonObjectResponse = activitiClient.completeTask(AuthContext.getAuthz(), completeTask);

        if (jsonObjectResponse == null
                || jsonObjectResponse.getCode() != 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("?????????????????????");
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

        //????????????
        JSONObjectResponse jsonObjectResponse = activitiClient.startProcess(assemblerProcessDTO(processApplyId, vo.getBusinessActivitiId(), vo.getBusinessGroupId(), vo.getBusinessGroupName(), vo.getProcessApplyName(), vo.getState(), businessDetail, vo.getBusinessActivitiName()));

        //??????id???????????????????????????
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
            baseResponse.setMessage("?????????????????????");
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
        processDTO.setApplyFlag(state);//???????????????(0:???????????????;1:????????????;)
        processDTO.setBusinessId(processApplyId);//??????id
        processDTO.setProcessWorkflowId(processInstanceId);//???????????????id ????????????????????????????????????
        processDTO.setBusinessDetail(businessDetail);//????????????
        processDTO.setComment("??????????????????");//????????????????????????
        processDTO.setProcessOperation(businessActivitiName);//?????????????????????,???????????????????????????????????????????????? ???:apply
        processDTO.setBusinessGroupId(businessGroupId);
        processDTO.setBusinessGroupName(businessGroupName);
        processDTO.setProcessName(processName);

//        processDTO.setProcessDefinitionKey();//???????????????
//        processDTO.setNotifyMessage();//???????????????,?????????????????????????????????
//        processDTO.setNotifyDetail();//???????????????,?????????????????????????????????
//        processDTO.setOutcome();//????????????????????????
//        processDTO.setProcessObject();//?????????????????????, ???:process
        return processDTO;
    }

    private void deleteVirtualMachine(List<IaasProcessApplyVirtualMachineVO> vos) {
        for (IaasProcessApplyVirtualMachineVO machineTreeResponse : vos) {
            //???????????????
            Long machineId = machineTreeResponse.getKey();
            iaasProcessApplyVirtualMachineMapper.deleteById(machineId);

            //?????????????????????
            List<IaasProcessApplyVirtualMachineDiskVO> machineDiskVOList = machineTreeResponse.getMachineDiskVOList();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasProcessApplyVirtualMachineDiskVO diskVO : machineDiskVOList) {
                    iaasProcessApplyVirtualMachineDiskMapper.deleteById(diskVO.getId());
                }
            }

            //????????????
            List<IaasProcessApplyVirtualMachineComponentVO> componentTreeResponses = machineTreeResponse.getChildren();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasProcessApplyVirtualMachineComponentVO parentcomponent : componentTreeResponses) {
                    iaasProcessApplyVirtualMachineComponentMapper.deleteById(parentcomponent.getKey());
                    //??????????????????
                    iaasProcessApplyVirtualMachineComponentParamMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //??????????????????
                    iaasProcessApplyVirtualMachineComponentScriptMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //??????????????????
                    iaasProcessApplyVirtualMachineComponentOperationMapper.deleteByVmComponentId(parentcomponent.getKey());

                    List<IaasProcessApplyVirtualMachineComponentVO> childrenTreeResponses = parentcomponent.getChildren();
                    if (CollectionUtils.isNotEmpty(childrenTreeResponses)) {
                        for (IaasProcessApplyVirtualMachineComponentVO childrenComponent : childrenTreeResponses) {
                            iaasProcessApplyVirtualMachineComponentMapper.deleteById(childrenComponent.getKey());
                            //??????????????????
                            iaasProcessApplyVirtualMachineComponentParamMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //??????????????????
                            iaasProcessApplyVirtualMachineComponentScriptMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //??????????????????
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
        //????????????????????????????????????
        List<IaasProcessApplyVirtualMachineVO> virtualMachinVOList = processApplyVO.getCatalogVirtualMachinVOList();
        if (CollectionUtils.isEmpty(virtualMachinVOList)) {
            log.info("?????????????????????!");
            return;
        }

        virtualMachinVOList.sort(Comparator.comparing(IaasProcessApplyVirtualMachineVO::getSort));

        //?????????????????????????????????????????????id???????????????????????????
        for (IaasProcessApplyVirtualMachineVO saveMachineVo : virtualMachinVOList) {
            long machineId = SnowFlakeIdGenerator.getInstance().nextId();
            //?????????????????????
            saveMachine(machineId, processApplyId, userId, now, saveMachineVo);

            //???????????????????????????
            saveVirtualMachineDiskList(machineId, userId, now, saveMachineVo);

            //todo ?????????????????????????????????????????????????????????????????????MQ????????????????????????
            saveMQResponseMachineInfo(machineId, userId, now);
        }

        //???????????????????????????????????????????????????
        List<IaasProcessApplyVirtualMachineComponentVO> sortParentEdMachineComponentVOS = new ArrayList<>();
        for (IaasProcessApplyVirtualMachineVO machineVO : virtualMachinVOList) {
            List<IaasProcessApplyVirtualMachineComponentVO> parentMachineComponentVOS = machineVO.getChildren();
            if (CollectionUtils.isNotEmpty(parentMachineComponentVOS)) {
                sortParentEdMachineComponentVOS.addAll(parentMachineComponentVOS);
            }
        }

        if (CollectionUtils.isEmpty(sortParentEdMachineComponentVOS)) {
            log.info("???????????????????????????!");
            return;
        }
        sortParentEdMachineComponentVOS.sort(Comparator.comparing(IaasProcessApplyVirtualMachineComponentVO::getSort));

        //??????????????????????????????????????????????????????id????????????id?????????????????????
        for (IaasProcessApplyVirtualMachineComponentVO saveComponentVO : sortParentEdMachineComponentVOS) {
            saveComponent(userId, now, saveComponentVO);
        }

        //???????????????????????????????????????????????????
        List<IaasProcessApplyVirtualMachineComponentVO> sortedChildrenEdMachineComponentVOS = new ArrayList<>();
        for (IaasProcessApplyVirtualMachineComponentVO machineComponentVO : sortParentEdMachineComponentVOS) {
            List<IaasProcessApplyVirtualMachineComponentVO> childrenComponentVOS = machineComponentVO.getChildren();
            if (CollectionUtils.isNotEmpty(childrenComponentVOS)) {
                sortedChildrenEdMachineComponentVOS.addAll(childrenComponentVOS);
            }
        }
        sortedChildrenEdMachineComponentVOS.sort(Comparator.comparing(IaasProcessApplyVirtualMachineComponentVO::getSort));

        //???????????????????????????
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
        log.info("????????????????????????????????????");

        IaasVirtualMachineDisk machineDisk = new IaasVirtualMachineDisk();
        machineDisk.setId(SnowFlakeIdGenerator.getInstance().nextId());
        machineDisk.setVmId(id);
        machineDisk.setDiskName("???????????????");
        machineDisk.setDiskType("????????????");
        machineDisk.setDiskTotal(100.00);
        machineDisk.setDiskUsed(30.00);
        machineDisk.setCreateUser(userId);
        machineDisk.setCreateTime(now);
        machineDisk.setUpdateTime(now);
        machineDisk.setUpdateUser(userId);
        machineDisk.setDeleted(false);
        iaasVirtualMachineDiskMapper.insert(machineDisk);
        log.info("??????????????????????????????????????????");

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
        log.info("??????????????????????????????????????????!");
    }

    private void saveVirtualMachineDiskList(long machineId, Long userId, Date now, IaasProcessApplyVirtualMachineVO saveMachineVo) {

        List<IaasProcessApplyVirtualMachineDiskVO> machineDiskVOS = saveMachineVo.getMachineDiskVOList();

        if (CollectionUtils.isEmpty(machineDiskVOS)) {
            log.error("?????????????????????!");
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
        log.info("????????????????????????????????????");
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
        log.info("????????????????????????????????????");

        //????????????id?????????id???????????????????????????
        List<IaasProcessApplyVirtualMachineComponentVO> children = saveComponentVO.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            for (IaasProcessApplyVirtualMachineComponentVO componentVO : children) {
                componentVO.setProcessApplyVmId(saveComponentVO.getProcessApplyVmId());
                componentVO.setParentId(componentId);
            }
        }

        //????????????????????????
        saveVirtualMachineComponentParamList(componentId, userId, now, saveComponentVO);

        //????????????????????????
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

            //????????????????????????
            saveVirtualMachineComponentScript(componentScriptId, componentId, userId, now, scriptVO);

            //???????????????????????????
            //????????????????????????
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
        log.info("??????????????????????????????????????????");
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
        log.info("??????????????????????????????!");
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
        log.info("??????????????????????????????!");
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
        log.info("??????????????????????????????");

        //????????????id????????????????????????
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
        log.info("?????????????????????????????????");
    }
}

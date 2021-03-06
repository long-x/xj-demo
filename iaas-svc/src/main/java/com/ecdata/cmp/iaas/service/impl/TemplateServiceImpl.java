package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasTemplate;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachine;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineComponent;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineComponentOperation;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineComponentParam;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineComponentScript;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineDisk;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineDiskVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.response.template.TemplateVirtualMachineComponentTreeResponse;
import com.ecdata.cmp.iaas.entity.dto.response.template.TemplateVirtualMachineTreeResponse;
import com.ecdata.cmp.iaas.mapper.component.ComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.component.ComponentScriptMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineComponentMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineComponentParamMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineComponentScriptMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineDiskMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineMapper;
import com.ecdata.cmp.iaas.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ??????:
 *
 * @author xxj
 * @create 2019-11-18 14:53
 */
@Service
@Slf4j
public class TemplateServiceImpl implements ITemplateService {
    @Autowired
    private IaasTemplateMapper iaasTemplateMapper;

    @Autowired
    private IaasTemplateVirtualMachineMapper virtualMachineMapper;

    @Autowired
    private IaasTemplateVirtualMachineDiskMapper machineDiskMapper;

    @Autowired
    private IaasTemplateVirtualMachineComponentMapper componentMapper;

    @Autowired
    private IaasTemplateVirtualMachineComponentParamMapper componentParamMapper;

    @Autowired
    private IaasTemplateVirtualMachineComponentOperationMapper componentOperationMapper;

    @Autowired
    private IaasTemplateVirtualMachineComponentScriptMapper componentScriptMapper;

    @Autowired
    private ComponentScriptMapper historyComponentScriptMapper;

    @Autowired
    private ComponentOperationMapper historyComponentOperationMapper;

    @Override
    public IPage<IaasTemplateVO> queryIaasTemplate(Page<IaasTemplateVO> page, Map<String, Object> params) {
        return iaasTemplateMapper.queryIaasTemplate(page, params);
    }

    @Override
    public void updateTemplate(IaasTemplate template) {
        iaasTemplateMapper.updateTemplate(template);
    }

    @Override
    public void deleteTemplate(Long templateId) {
        List<TemplateVirtualMachineTreeResponse> treeResponses = iaasTemplateMapper.queryMachineTree(templateId);

        //????????????????????????????????????
        deleteVirtualMachine(treeResponses);

        //??????????????????
        iaasTemplateMapper.deleteById(templateId);
    }

    @Override
    public void saveIaasTemplate(IaasTemplate template) {
        iaasTemplateMapper.insert(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveTemplateMachineComponent(List<TemplateVirtualMachineTreeResponse> vos) {
        BaseResponse response = new BaseResponse();

        if (CollectionUtils.isEmpty(vos)) {
            response.setCode(201);
            response.setMessage("???????????????????????????");
            log.error("???????????????????????????");
            return response;
        }

        //??????????????????
        Long templateId = vos.get(0).getTemplateId();
        Integer state = vos.get(0).getState();
        if (templateId == null || state == null) {
            response.setCode(201);
            response.setMessage("??????id??????????????????????????????");
            log.error("??????id??????????????????????????????");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //?????????????????????????????????
        if (state == 2) {
            updateIaasTemplate(state, templateId, userId, now);
        }

        //?????????????????????????????????
        saveTemplateInfo(userId, templateId, now, vos);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editTemplate(List<TemplateVirtualMachineTreeResponse> vos) {
        BaseResponse response = new BaseResponse();
        if (CollectionUtils.isEmpty(vos)) {
            response.setCode(201);
            response.setMessage("???????????????????????????");
            log.error("???????????????????????????");
            return response;
        }

        //??????????????????
        Long templateId = vos.get(0).getTemplateId();
        Integer state = vos.get(0).getState();
        if (templateId == null || state == null) {
            response.setCode(201);
            response.setMessage("??????id??????????????????????????????");
            log.error("??????id??????????????????????????????");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //??????????????????
        updateIaasTemplate(state, templateId, userId, now);

        //????????????????????????????????????????????????????????????
        deleteVirtualMachine(vos);

        //?????????????????????????????????
        saveTemplateInfo(userId, templateId, now, vos);

        return response;
    }

    @Override
    public IaasTemplateVO queryTemplateInfo(IaasTemplateVO templateVO) {
        return iaasTemplateMapper.queryTemplateInfo(templateVO);
    }

    @Override
    public List<IaasTemplateVO> queryTemplate() {
        return iaasTemplateMapper.queryTemplate();
    }

    @Override
    public List<TemplateVirtualMachineTreeResponse> queryMachineTree(Long templateId) {
        List<TemplateVirtualMachineTreeResponse> treeResponses = iaasTemplateMapper.queryMachineTree(templateId);

        for (TemplateVirtualMachineTreeResponse response : treeResponses) {
            List<TemplateVirtualMachineComponentTreeResponse> children = response.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }

            //?????????
            for (TemplateVirtualMachineComponentTreeResponse componentTreeResponse : children) {
                List<IaasTemplateVirtualMachineComponentParamVO> compParams = componentTreeResponse.getCompParams();
                if (CollectionUtils.isEmpty(compParams)) {
                    continue;
                }
                setValueSelect(compParams);

                //?????????
                List<TemplateVirtualMachineComponentTreeResponse> children1 = componentTreeResponse.getChildren();
                if (CollectionUtils.isEmpty(compParams)) {
                    continue;
                }
                for (TemplateVirtualMachineComponentTreeResponse childComponent : children1) {
                    List<IaasTemplateVirtualMachineComponentParamVO> compParams1 = childComponent.getCompParams();
                    setValueSelect(compParams1);
                }
            }
        }
        return treeResponses;
    }

    @Override
    public List<IaasCatalogVO> checkTemplateIFUse(Long id) {
        return iaasTemplateMapper.checkTemplateIFUse(id);
    }

    private void setValueSelect(List<IaasTemplateVirtualMachineComponentParamVO> compParams) {
        for (IaasTemplateVirtualMachineComponentParamVO param : compParams) {
            String valueList = param.getValueList();
            if (StringUtils.isNotBlank(valueList)) {
                param.setValueSelect(JSON.parseArray(valueList));
            }
        }
    }

    private void deleteVirtualMachine(List<TemplateVirtualMachineTreeResponse> vos) {
        for (TemplateVirtualMachineTreeResponse machineTreeResponse : vos) {
            //???????????????
            Long machineId = machineTreeResponse.getKey();
            virtualMachineMapper.deleteById(machineId);

            //?????????????????????
            List<IaasTemplateVirtualMachineDiskVO> machineDiskVOList = machineTreeResponse.getMachineDiskVOList();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasTemplateVirtualMachineDiskVO diskVO : machineDiskVOList) {
                    machineDiskMapper.deleteById(diskVO.getId());
                }
            }

            //????????????
            List<TemplateVirtualMachineComponentTreeResponse> componentTreeResponses = machineTreeResponse.getChildren();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (TemplateVirtualMachineComponentTreeResponse parentcomponent : componentTreeResponses) {
                    componentMapper.deleteById(parentcomponent.getKey());
                    //??????????????????
                    componentParamMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //??????????????????
                    componentScriptMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //??????????????????
                    componentOperationMapper.deleteByVmComponentId(parentcomponent.getKey());

                    List<TemplateVirtualMachineComponentTreeResponse> childrenTreeResponses = parentcomponent.getChildren();
                    if (CollectionUtils.isNotEmpty(childrenTreeResponses)) {
                        for (TemplateVirtualMachineComponentTreeResponse childrenComponent : childrenTreeResponses) {
                            componentMapper.deleteById(childrenComponent.getKey());
                            //??????????????????
                            componentParamMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //??????????????????
                            componentScriptMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //??????????????????
                            componentOperationMapper.deleteByVmComponentId(childrenComponent.getKey());
                        }
                    }
                }
            }
        }
    }

    private void saveTemplateInfo(Long userId, Long templateId, Date now, List<TemplateVirtualMachineTreeResponse> vos) {
        for (TemplateVirtualMachineTreeResponse machineVO : vos) {
            if (machineVO == null) {
                continue;
            }
            //?????????????????????
            long machineId = SnowFlakeIdGenerator.getInstance().nextId();
            saveTemplateVirtualMachine(templateId, machineId, userId, now, machineVO);

            //???????????????????????????
            saveVirtualMachineDisk(machineId, userId, now, machineVO);

            //??????????????????
            saveVirtualMachineComponent(machineId, userId, now, machineVO);
        }
    }

    private void saveVirtualMachineComponent(long machineId, Long userId, Date now, TemplateVirtualMachineTreeResponse machineVO) {

        List<TemplateVirtualMachineComponentTreeResponse> machineComponentVOS = machineVO.getChildren();

        if (CollectionUtils.isEmpty(machineComponentVOS)) {
            log.error("???????????????????????????!");
            return;
        }

        for (TemplateVirtualMachineComponentTreeResponse componentVO : machineComponentVOS) {
            if (componentVO == null) {
                continue;
            }
            List<TemplateVirtualMachineComponentTreeResponse> children = componentVO.getChildren();

            long componentId = SnowFlakeIdGenerator.getInstance().nextId();

            Long parentId = (CollectionUtils.isEmpty(children)) ? null : componentId;

            //???????????????????????????
            saveComponent(machineId, componentId, machineId, userId, now, componentVO);

            //????????????????????????
            if (CollectionUtils.isNotEmpty(children)) {
                for (TemplateVirtualMachineComponentTreeResponse childrenComponentVO : children) {
                    long childComponentId = SnowFlakeIdGenerator.getInstance().nextId();
                    saveComponent(parentId, childComponentId, machineId, userId, now, childrenComponentVO);
                }
            }
        }
    }

    private void saveComponent(Long parentId, long componentId, long machineId, long userId, Date now, TemplateVirtualMachineComponentTreeResponse componentVO) {
        //??????????????????
        virtualComponent(parentId, componentId, machineId, userId, now, componentVO);

        //????????????????????????
        List<IaasTemplateVirtualMachineComponentParamVO> componentParamVOList = componentVO.getCompParams();
        if (CollectionUtils.isNotEmpty(componentParamVOList)) {
            for (IaasTemplateVirtualMachineComponentParamVO componentParamVO : componentParamVOList) {
                if (componentParamVO == null) {
                    continue;
                }
                saveVirtualMachineComponentParam(componentId, userId, now, componentParamVO);
            }
        }

        //????????????????????????????????????
        //????????????id
        Long componentIdScrips = componentVO.getComponentId();
        List<IaasTemplateVirtualMachineComponentScriptVO> compScripts = historyComponentScriptMapper.queryScripsByComponentId(componentIdScrips);
        if (CollectionUtils.isEmpty(compScripts)) {
            return;
        }

        for (IaasTemplateVirtualMachineComponentScriptVO scriptVO : compScripts) {
            if (scriptVO == null) {
                continue;
            }

            long componentScriptId = SnowFlakeIdGenerator.getInstance().nextId();

            //????????????????????????
            saveVirtualMachineComponentScript(componentScriptId, componentId, userId, now, scriptVO);

            //???????????????????????????
            //????????????????????????
            Long scriptId = scriptVO.getId();
            List<IaasTemplateVirtualMachineComponentOperationVO> compOps = historyComponentOperationMapper.queryOperationByScriptId(scriptId);
            if (CollectionUtils.isNotEmpty(compOps)) {
                for (IaasTemplateVirtualMachineComponentOperationVO operationVO : compOps) {
                    if (operationVO == null) {
                        continue;
                    }
                    saveVirtualMachineComponentOperation(componentScriptId, componentId, userId, now, operationVO);
                }
            }
        }

    }

    private void saveVirtualMachineComponentOperation(long componentScriptId, long componentId, Long userId, Date now, IaasTemplateVirtualMachineComponentOperationVO compOp) {
        IaasTemplateVirtualMachineComponentOperation componentOperation = new IaasTemplateVirtualMachineComponentOperation();
        BeanUtils.copyProperties(compOp, componentOperation);

        componentOperation.setId(SnowFlakeIdGenerator.getInstance().nextId());
        componentOperation.setOperationId(compOp.getId());
        componentOperation.setTenantId(null);
        componentOperation.setVmComponentId(componentId);
        componentOperation.setVmComponentScriptId(componentScriptId);
        componentOperation.setCreateUser(userId);
        componentOperation.setCreateTime(now);
        componentOperation.setUpdateTime(now);
        componentOperation.setUpdateUser(userId);
        componentOperation.setIsDeleted(0);
        //tenantId??????
        log.info("????????????(??????) "+componentOperation);
        componentOperationMapper.insert(componentOperation);
        log.info("???????????????????????????(??????)???????????????");
    }

    private void saveVirtualMachineComponentScript(long componentScriptId, long componentId, Long userId, Date now, IaasTemplateVirtualMachineComponentScriptVO scriptVO) {
        IaasTemplateVirtualMachineComponentScript componentScript = new IaasTemplateVirtualMachineComponentScript();
        BeanUtils.copyProperties(scriptVO, componentScript);

        componentScript.setId(componentScriptId);
        componentScript.setScriptId(scriptVO.getId());
        componentScript.setTenantId(null);
        componentScript.setVmComponentId(componentId);
        componentScript.setCreateUser(userId);
        componentScript.setCreateTime(now);
        componentScript.setUpdateTime(now);
        componentScript.setUpdateUser(userId);
        componentScript.setIsDeleted(0);
        log.info("???????????? "+componentScript);
        componentScriptMapper.insert(componentScript);
        log.info("??????????????????????????????????????????");
    }

    private void saveVirtualMachineComponentParam(long componentId, Long userId, Date now, IaasTemplateVirtualMachineComponentParamVO componentParamVO) {
        IaasTemplateVirtualMachineComponentParam componentParam = new IaasTemplateVirtualMachineComponentParam();
        BeanUtils.copyProperties(componentParamVO, componentParam);

        componentParam.setId(SnowFlakeIdGenerator.getInstance().nextId());
        componentParam.setVmComponentId(componentId);
        componentParam.setComponentParamId(componentParamVO.getComponentId());
        componentParam.setParamType(componentParamVO.getParamType());
        componentParam.setRequired(componentParamVO.getRequired());
        componentParam.setCreateUser(userId);
        componentParam.setCreateTime(now);
        componentParam.setUpdateTime(now);
        componentParam.setUpdateUser(userId);
        componentParam.setIsDeleted(0);

        componentParamMapper.insert(componentParam);
        log.info("??????????????????????????????????????????");
    }

    private void virtualComponent(Long parentId, Long componentId, long machineId, Long userId, Date now, TemplateVirtualMachineComponentTreeResponse componentVO) {
        IaasTemplateVirtualMachineComponent component = new IaasTemplateVirtualMachineComponent();
        BeanUtils.copyProperties(componentVO, component);

        component.setId(componentId);
        component.setDisplayName(componentVO.getTitle());
        component.setVmId(machineId);
        component.setCreateUser(userId);
        component.setCreateTime(now);
        component.setUpdateTime(now);
        component.setUpdateUser(userId);
        component.setIsDeleted(0);
        component.setParentId(parentId);

        componentMapper.insert(component);
        log.info("????????????????????????????????????");
    }

    /**
     * ???????????????????????????
     *
     * @param machineId
     * @param userId
     * @param now
     * @param machineVO
     */
    private void saveVirtualMachineDisk(Long machineId, Long userId, Date now, TemplateVirtualMachineTreeResponse machineVO) {

        List<IaasTemplateVirtualMachineDiskVO> machineDiskVOS = machineVO.getMachineDiskVOList();

        if (CollectionUtils.isEmpty(machineDiskVOS)) {
            log.error("?????????????????????!");
            return;
        }

        for (IaasTemplateVirtualMachineDiskVO diskVO : machineDiskVOS) {
            if (diskVO == null) {
                continue;
            }
            virtualMachineDisk(machineId, userId, now, diskVO);
        }
    }

    private void virtualMachineDisk(Long machineId, Long userId, Date now, IaasTemplateVirtualMachineDiskVO diskVO) {
        IaasTemplateVirtualMachineDisk disk = new IaasTemplateVirtualMachineDisk();
        BeanUtils.copyProperties(diskVO, disk);

        disk.setId(SnowFlakeIdGenerator.getInstance().nextId());
        disk.setVmId(machineId);
        disk.setCreateUser(userId);
        disk.setCreateTime(now);
        disk.setUpdateTime(now);
        disk.setUpdateUser(userId);
        disk.setIsDeleted(0);

        machineDiskMapper.insert(disk);
        log.info("????????????????????????????????????");
    }

    /**
     * ?????????????????????
     *
     * @param templateId
     * @param machineId
     * @param userId
     * @param now
     * @param machineVO
     */
    private void saveTemplateVirtualMachine(Long templateId, Long machineId, Long userId, Date now, TemplateVirtualMachineTreeResponse machineVO) {
        IaasTemplateVirtualMachine machine = new IaasTemplateVirtualMachine();
        BeanUtils.copyProperties(machineVO, machine);
        machine.setId(machineId);
        machine.setVmName(machineVO.getTitle());
        machine.setTemplateId(templateId);
        machine.setCreateUser(userId);
        machine.setCreateTime(now);
        machine.setUpdateTime(now);
        machine.setUpdateUser(userId);
        machine.setIsDeleted(0);

        virtualMachineMapper.insert(machine);
        log.info("??????????????????????????????", machineId);
    }

    private void updateIaasTemplate(Integer state, Long templateId, Long userId, Date now) {
        IaasTemplate template = new IaasTemplate();
        template.setId(templateId);
        template.setState(state);
        template.setUpdateUser(userId);
        template.setUpdateTime(now);
        iaasTemplateMapper.updateTemplate(template);
        log.info("???????????????????????????", templateId);
    }

}

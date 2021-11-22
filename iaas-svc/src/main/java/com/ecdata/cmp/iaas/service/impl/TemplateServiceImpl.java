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
 * 描述:
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

        //删除虚拟机组件等其他信息
        deleteVirtualMachine(treeResponses);

        //删除模板信息
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
            response.setMessage("保存数据不能为空！");
            log.error("保存数据不能为空！");
            return response;
        }

        //获取模板信息
        Long templateId = vos.get(0).getTemplateId();
        Integer state = vos.get(0).getState();
        if (templateId == null || state == null) {
            response.setCode(201);
            response.setMessage("模板id或模板状态不能为空！");
            log.error("模板id或模板状态不能为空！");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //保存并发布更新模板状态
        if (state == 2) {
            updateIaasTemplate(state, templateId, userId, now);
        }

        //保存模板下面虚拟机信息
        saveTemplateInfo(userId, templateId, now, vos);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editTemplate(List<TemplateVirtualMachineTreeResponse> vos) {
        BaseResponse response = new BaseResponse();
        if (CollectionUtils.isEmpty(vos)) {
            response.setCode(201);
            response.setMessage("保存数据不能为空！");
            log.error("保存数据不能为空！");
            return response;
        }

        //获取模板信息
        Long templateId = vos.get(0).getTemplateId();
        Integer state = vos.get(0).getState();
        if (templateId == null || state == null) {
            response.setCode(201);
            response.setMessage("模板id或模板状态不能为空！");
            log.error("模板id或模板状态不能为空！");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //更新模板信息
        updateIaasTemplate(state, templateId, userId, now);

        //删除模板下面的虚拟机，组件，组件参数信息
        deleteVirtualMachine(vos);

        //保存模板下面虚拟机信息
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

            //父组件
            for (TemplateVirtualMachineComponentTreeResponse componentTreeResponse : children) {
                List<IaasTemplateVirtualMachineComponentParamVO> compParams = componentTreeResponse.getCompParams();
                if (CollectionUtils.isEmpty(compParams)) {
                    continue;
                }
                setValueSelect(compParams);

                //子组件
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
            //删除虚拟机
            Long machineId = machineTreeResponse.getKey();
            virtualMachineMapper.deleteById(machineId);

            //删除虚拟机磁盘
            List<IaasTemplateVirtualMachineDiskVO> machineDiskVOList = machineTreeResponse.getMachineDiskVOList();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasTemplateVirtualMachineDiskVO diskVO : machineDiskVOList) {
                    machineDiskMapper.deleteById(diskVO.getId());
                }
            }

            //删除组件
            List<TemplateVirtualMachineComponentTreeResponse> componentTreeResponses = machineTreeResponse.getChildren();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (TemplateVirtualMachineComponentTreeResponse parentcomponent : componentTreeResponses) {
                    componentMapper.deleteById(parentcomponent.getKey());
                    //删除组件参数
                    componentParamMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //删除组件脚本
                    componentScriptMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //删除组件操作
                    componentOperationMapper.deleteByVmComponentId(parentcomponent.getKey());

                    List<TemplateVirtualMachineComponentTreeResponse> childrenTreeResponses = parentcomponent.getChildren();
                    if (CollectionUtils.isNotEmpty(childrenTreeResponses)) {
                        for (TemplateVirtualMachineComponentTreeResponse childrenComponent : childrenTreeResponses) {
                            componentMapper.deleteById(childrenComponent.getKey());
                            //删除组件参数
                            componentParamMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //删除组件脚本
                            componentScriptMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //删除组件操作
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
            //保存虚拟机信息
            long machineId = SnowFlakeIdGenerator.getInstance().nextId();
            saveTemplateVirtualMachine(templateId, machineId, userId, now, machineVO);

            //保存虚拟机磁盘信息
            saveVirtualMachineDisk(machineId, userId, now, machineVO);

            //保存组件信息
            saveVirtualMachineComponent(machineId, userId, now, machineVO);
        }
    }

    private void saveVirtualMachineComponent(long machineId, Long userId, Date now, TemplateVirtualMachineTreeResponse machineVO) {

        List<TemplateVirtualMachineComponentTreeResponse> machineComponentVOS = machineVO.getChildren();

        if (CollectionUtils.isEmpty(machineComponentVOS)) {
            log.error("虚拟机组件信息为空!");
            return;
        }

        for (TemplateVirtualMachineComponentTreeResponse componentVO : machineComponentVOS) {
            if (componentVO == null) {
                continue;
            }
            List<TemplateVirtualMachineComponentTreeResponse> children = componentVO.getChildren();

            long componentId = SnowFlakeIdGenerator.getInstance().nextId();

            Long parentId = (CollectionUtils.isEmpty(children)) ? null : componentId;

            //保存父类组件细信息
            saveComponent(machineId, componentId, machineId, userId, now, componentVO);

            //保存子类组件信息
            if (CollectionUtils.isNotEmpty(children)) {
                for (TemplateVirtualMachineComponentTreeResponse childrenComponentVO : children) {
                    long childComponentId = SnowFlakeIdGenerator.getInstance().nextId();
                    saveComponent(parentId, childComponentId, machineId, userId, now, childrenComponentVO);
                }
            }
        }
    }

    private void saveComponent(Long parentId, long componentId, long machineId, long userId, Date now, TemplateVirtualMachineComponentTreeResponse componentVO) {
        //保存组件信息
        virtualComponent(parentId, componentId, machineId, userId, now, componentVO);

        //保存组件参数信息
        List<IaasTemplateVirtualMachineComponentParamVO> componentParamVOList = componentVO.getCompParams();
        if (CollectionUtils.isNotEmpty(componentParamVOList)) {
            for (IaasTemplateVirtualMachineComponentParamVO componentParamVO : componentParamVOList) {
                if (componentParamVO == null) {
                    continue;
                }
                saveVirtualMachineComponentParam(componentId, userId, now, componentParamVO);
            }
        }

        //查出历史组件脚本信息保存
        //历史组件id
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

            //保存组件脚本信息
            saveVirtualMachineComponentScript(componentScriptId, componentId, userId, now, scriptVO);

            //查询出历史操作信息
            //保存组件操作信息
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
        //tenantId重复
        log.info("保存脚本(操作) "+componentOperation);
        componentOperationMapper.insert(componentOperation);
        log.info("保存虚拟机组件脚本(操作)信息成功！");
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
        log.info("保存脚本 "+componentScript);
        componentScriptMapper.insert(componentScript);
        log.info("保存虚拟机组件脚本信息成功！");
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
        log.info("保存虚拟机组件参数信息成功！");
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
        log.info("保存虚拟机组件信息成功！");
    }

    /**
     * 保存虚拟机磁盘信息
     *
     * @param machineId
     * @param userId
     * @param now
     * @param machineVO
     */
    private void saveVirtualMachineDisk(Long machineId, Long userId, Date now, TemplateVirtualMachineTreeResponse machineVO) {

        List<IaasTemplateVirtualMachineDiskVO> machineDiskVOS = machineVO.getMachineDiskVOList();

        if (CollectionUtils.isEmpty(machineDiskVOS)) {
            log.error("虚拟机磁盘为空!");
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
        log.info("保存虚拟机磁盘信息成功！");
    }

    /**
     * 保存虚拟机信息
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
        log.info("保存虚拟机信息成功！", machineId);
    }

    private void updateIaasTemplate(Integer state, Long templateId, Long userId, Date now) {
        IaasTemplate template = new IaasTemplate();
        template.setId(templateId);
        template.setState(state);
        template.setUpdateUser(userId);
        template.setUpdateTime(now);
        iaasTemplateMapper.updateTemplate(template);
        log.info("更新模板信息成功！", templateId);
    }

}

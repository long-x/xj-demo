package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalog;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachin;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponent;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponentOperation;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponentParam;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponentScript;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineDisk;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineComponentVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineDiskVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineTreeVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.dto.response.catalog.CatalogMachineComponentResponse;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.IaasTemplateVirtualMachineComponentScriptMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachinMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineComponentMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineComponentParamMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineComponentScriptMapper;
import com.ecdata.cmp.iaas.mapper.catalog.IaasCatalogVirtualMachineDiskMapper;
import com.ecdata.cmp.iaas.service.ICatalogService;
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
 * @create 2019-11-24 10:50
 */
@Service
@Slf4j
public class ICatalogServiceImpl implements ICatalogService {

    @Autowired
    private IaasCatalogMapper iaasCatalogMapper;

    @Autowired
    private IaasCatalogVirtualMachinMapper iaasCatalogVirtualMachinMapper;

    @Autowired
    private IaasCatalogVirtualMachineDiskMapper iaasCatalogVirtualMachineDiskMapper;

    @Autowired
    private IaasCatalogVirtualMachineComponentMapper iaasCatalogVirtualMachineComponentMapper;

    @Autowired
    private IaasCatalogVirtualMachineComponentParamMapper iaasCatalogVirtualMachineComponentParamMapper;

    @Autowired
    private IaasTemplateVirtualMachineComponentScriptMapper iaasTemplateVirtualMachineComponentScriptMapper;

    @Autowired
    private IaasTemplateVirtualMachineComponentOperationMapper iaasTemplateVirtualMachineComponentOperationMapper;

    @Autowired
    private IaasCatalogVirtualMachineComponentScriptMapper iaasCatalogVirtualMachineComponentScriptMapper;

    @Autowired
    private IaasCatalogVirtualMachineComponentOperationMapper iaasCatalogVirtualMachineComponentOperationMapper;

    @Override
    public IPage<IaasCatalogVO> queryIaasCatalog(Page<IaasCatalogVO> page, Map<String, Object> params) {
        return iaasCatalogMapper.queryIaasCatalog(page, params);
    }

    @Override
    public CatalogMachineComponentResponse queryMachineTree(Long catalogId) {
        CatalogMachineComponentResponse componentResponse = new CatalogMachineComponentResponse();

        IaasCatalogVO iaasCatalogVO = iaasCatalogMapper.queryIaasCatalogById(catalogId);

        componentResponse.setIaasCatalogVO(iaasCatalogVO);

        List<IaasCatalogVirtualMachineTreeVO> treeResponses = iaasCatalogMapper.queryMachineTree(catalogId);

        for (IaasCatalogVirtualMachineTreeVO response : treeResponses) {
            List<IaasCatalogVirtualMachineComponentVO> children = response.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }

            //?????????
            for (IaasCatalogVirtualMachineComponentVO componentTreeResponse : children) {
                List<IaasCatalogVirtualMachineComponentParamVO> compParams = componentTreeResponse.getCompParams();
                if (CollectionUtils.isEmpty(compParams)) {
                    continue;
                }
                setValueSelect(compParams);

                //?????????
                List<IaasCatalogVirtualMachineComponentVO> children1 = componentTreeResponse.getChildren();
                if (CollectionUtils.isEmpty(compParams)) {
                    continue;
                }
                for (IaasCatalogVirtualMachineComponentVO childComponent : children1) {
                    List<IaasCatalogVirtualMachineComponentParamVO> compParams1 = childComponent.getCompParams();
                    setValueSelect(compParams1);
                }
            }
        }

        componentResponse.setCatalogVirtualMachinVOList(treeResponses);
        return componentResponse;
    }

    @Override
    public void savaIaasCatalog(IaasCatalog catalog) {
        iaasCatalogMapper.insert(catalog);
        log.info("????????????????????????");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editCatalogMachineComponent(CatalogMachineComponentResponse catalogMachineComponent) {
        BaseResponse response = new BaseResponse();
        List<IaasCatalogVirtualMachineTreeVO> vos = catalogMachineComponent.getCatalogVirtualMachinVOList();

        if (CollectionUtils.isEmpty(vos)) {
            response.setCode(201);
            response.setMessage("???????????????????????????");
            log.error("???????????????????????????");
            return response;
        }

        //??????????????????
        IaasCatalogVO iaasCatalogVO = catalogMachineComponent.getIaasCatalogVO();
        Long catalogId = iaasCatalogVO.getId();
        Integer state = iaasCatalogVO.getState();
        Long providerId = iaasCatalogVO.getProviderId();
        if (catalogId == null || state == null) {
            response.setCode(201);
            response.setMessage("????????????id????????????????????????????????????");
            log.error("????????????id????????????????????????????????????");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //??????????????????
        updateIaasCatalog(providerId, state, catalogId, userId, now);

        //????????????????????????????????????????????????????????????
        deleteVirtualMachine(vos);

        //?????????????????????????????????
        saveCatalogInfo(userId, catalogId, now, vos);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCatalog(Long catalogId) {
        List<IaasCatalogVirtualMachineTreeVO> treeResponses = iaasCatalogMapper.queryMachineTree(catalogId);

        //????????????????????????????????????
        deleteVirtualMachine(treeResponses);

        //????????????????????????
        iaasCatalogMapper.deleteById(catalogId);
    }

    @Override
    public void updateCatalog(IaasCatalog catalog) {
        iaasCatalogMapper.updateIaasCatalog(catalog);
    }

    @Override
    public List<IaasProcessApplyVO> checkCatalogIFUse(Long id) {
        return iaasCatalogMapper.checkCatalogIFUse(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveCatalogMachineComponent(CatalogMachineComponentResponse catalogMachineComponent) {
        BaseResponse response = new BaseResponse();

        List<IaasCatalogVirtualMachineTreeVO> vos = catalogMachineComponent.getCatalogVirtualMachinVOList();
        if (CollectionUtils.isEmpty(vos)) {
            response.setCode(201);
            response.setMessage("???????????????????????????");
            log.error("???????????????????????????");
            return response;
        }

        //????????????????????????
        IaasCatalogVO iaasCatalogVO = catalogMachineComponent.getIaasCatalogVO();
        Long catalogId = iaasCatalogVO.getId();
        Integer state = iaasCatalogVO.getState();
        Long providerId = iaasCatalogVO.getProviderId();
        if (catalogId == null || state == null) {
            response.setCode(201);
            response.setMessage("????????????id????????????????????????????????????");
            log.error("????????????id????????????????????????????????????");
            return response;
        }

        Long userId = Sign.getUserId();
        Date now = DateUtil.getNow();

        //???????????????????????????????????????
        updateIaasCatalog(providerId, state, catalogId, userId, now);

        //?????????????????????????????????
        saveCatalogInfo(userId, catalogId, now, vos);

        return response;
    }

    private void setValueSelect(List<IaasCatalogVirtualMachineComponentParamVO> compParams) {
        for (IaasCatalogVirtualMachineComponentParamVO param : compParams) {
            String valueList = param.getValueList();
            if (StringUtils.isNotBlank(valueList)) {
                param.setValueSelect(JSON.parseArray(valueList));
            }
        }
    }

    private void deleteVirtualMachine(List<IaasCatalogVirtualMachineTreeVO> vos) {
        for (IaasCatalogVirtualMachineTreeVO machineTreeResponse : vos) {
            //???????????????
            Long machineId = machineTreeResponse.getKey();
            iaasCatalogVirtualMachinMapper.deleteById(machineId);

            //?????????????????????
            List<IaasCatalogVirtualMachineDiskVO> machineDiskVOList = machineTreeResponse.getMachineDiskVOList();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasCatalogVirtualMachineDiskVO diskVO : machineDiskVOList) {
                    iaasCatalogVirtualMachineDiskMapper.deleteById(diskVO.getId());
                }
            }

            //????????????
            List<IaasCatalogVirtualMachineComponentVO> componentTreeResponses = machineTreeResponse.getChildren();
            if (CollectionUtils.isNotEmpty(machineDiskVOList)) {
                for (IaasCatalogVirtualMachineComponentVO parentcomponent : componentTreeResponses) {
                    iaasCatalogVirtualMachineComponentMapper.deleteById(parentcomponent.getKey());
                    //??????????????????
                    iaasCatalogVirtualMachineComponentParamMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //??????????????????
                    iaasCatalogVirtualMachineComponentScriptMapper.deleteByVmComponentId(parentcomponent.getKey());
                    //??????????????????
                    iaasCatalogVirtualMachineComponentOperationMapper.deleteByVmComponentId(parentcomponent.getKey());

                    List<IaasCatalogVirtualMachineComponentVO> childrenTreeResponses = parentcomponent.getChildren();
                    if (CollectionUtils.isNotEmpty(childrenTreeResponses)) {
                        for (IaasCatalogVirtualMachineComponentVO childrenComponent : childrenTreeResponses) {
                            iaasCatalogVirtualMachineComponentMapper.deleteById(childrenComponent.getKey());
                            //??????????????????
                            iaasCatalogVirtualMachineComponentParamMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //??????????????????
                            iaasCatalogVirtualMachineComponentScriptMapper.deleteByVmComponentId(childrenComponent.getKey());
                            //??????????????????
                            iaasCatalogVirtualMachineComponentOperationMapper.deleteByVmComponentId(childrenComponent.getKey());
                        }
                    }
                }
            }
        }
    }

    private void saveCatalogInfo(Long userId, Long templateId, Date now, List<IaasCatalogVirtualMachineTreeVO> vos) {
        for (IaasCatalogVirtualMachineTreeVO machineVO : vos) {
            if (machineVO == null) {
                continue;
            }
            //?????????????????????
            long machineId = SnowFlakeIdGenerator.getInstance().nextId();
            saveCatalogVirtualMachine(templateId, machineId, userId, now, machineVO);

            //???????????????????????????
            saveVirtualMachineDisk(machineId, userId, now, machineVO);

            //??????????????????
            saveVirtualMachineComponent(machineId, userId, now, machineVO);
        }
    }

    private void saveVirtualMachineComponent(long machineId, Long userId, Date now, IaasCatalogVirtualMachineTreeVO machineVO) {

        List<IaasCatalogVirtualMachineComponentVO> machineComponentVOS = machineVO.getChildren();

        if (CollectionUtils.isEmpty(machineComponentVOS)) {
            log.error("???????????????????????????!");
            return;
        }

        for (IaasCatalogVirtualMachineComponentVO componentVO : machineComponentVOS) {
            if (componentVO == null) {
                continue;
            }
            List<IaasCatalogVirtualMachineComponentVO> children = componentVO.getChildren();

            long componentId = SnowFlakeIdGenerator.getInstance().nextId();

            Long parentId = (CollectionUtils.isEmpty(children)) ? null : componentId;

            //???????????????????????????
            saveComponent(machineId, componentId, machineId, userId, now, componentVO);

            //????????????????????????
            if (CollectionUtils.isNotEmpty(children)) {
                for (IaasCatalogVirtualMachineComponentVO childrenComponentVO : children) {
                    long childComponentId = SnowFlakeIdGenerator.getInstance().nextId();
                    saveComponent(parentId, childComponentId, machineId, userId, now, childrenComponentVO);
                }
            }
        }
    }

    private void saveComponent(Long parentId, long componentId, long machineId, long userId, Date now, IaasCatalogVirtualMachineComponentVO componentVO) {
        //??????????????????
        virtualComponent(parentId, componentId, machineId, userId, now, componentVO);

        //????????????????????????
        List<IaasCatalogVirtualMachineComponentParamVO> componentParamVOList = componentVO.getCompParams();
        if (CollectionUtils.isNotEmpty(componentParamVOList)) {
            for (IaasCatalogVirtualMachineComponentParamVO componentParamVO : componentParamVOList) {
                if (componentParamVO == null) {
                    continue;
                }
                saveVirtualMachineComponentParam(componentId, userId, now, componentParamVO);
            }
        }

        //????????????????????????????????????
        //????????????id
        Long componentIdScrips = componentVO.getKey();
        List<IaasCatalogVirtualMachineComponentScriptVO> compScripts = iaasTemplateVirtualMachineComponentScriptMapper.queryTemplateScripsByComponentId(componentIdScrips);
        if (CollectionUtils.isEmpty(compScripts)) {
            return;
        }

        for (IaasCatalogVirtualMachineComponentScriptVO scriptVO : compScripts) {
            if (scriptVO == null) {
                continue;
            }

            long componentScriptId = SnowFlakeIdGenerator.getInstance().nextId();

            //????????????????????????
            saveVirtualMachineComponentScript(componentScriptId, componentId, userId, now, scriptVO);

            //???????????????????????????
            //????????????????????????
            Long scriptId = scriptVO.getId();
            List<IaasCatalogVirtualMachineComponentOperationVO> compOps = iaasTemplateVirtualMachineComponentOperationMapper.queryTemplateOperationByScriptId(scriptId);
            if (CollectionUtils.isNotEmpty(compOps)) {
                for (IaasCatalogVirtualMachineComponentOperationVO operationVO : compOps) {
                    if (operationVO == null) {
                        continue;
                    }
                    saveVirtualMachineComponentOperation(componentScriptId, componentId, userId, now, operationVO);
                }
            }
        }

    }

    private void saveVirtualMachineComponentOperation(long componentScriptId, long componentId, Long userId, Date now, IaasCatalogVirtualMachineComponentOperationVO compOp) {
        IaasCatalogVirtualMachineComponentOperation componentOperation = new IaasCatalogVirtualMachineComponentOperation();
        BeanUtils.copyProperties(compOp, componentOperation);

        componentOperation.setId(SnowFlakeIdGenerator.getInstance().nextId());
        componentOperation.setOperationId(compOp.getId());
        componentOperation.setVmComponentId(componentId);
        componentOperation.setVmComponentScriptId(componentScriptId);
        componentOperation.setCreateUser(userId);
        componentOperation.setCreateTime(now);
        componentOperation.setUpdateTime(now);
        componentOperation.setUpdateUser(userId);
        componentOperation.setIsDeleted(0);
        componentOperation.setTenantId(null);

        iaasCatalogVirtualMachineComponentOperationMapper.insert(componentOperation);
        log.info("??????????????????????????????????????????");
    }

    private void saveVirtualMachineComponentScript(long componentScriptId, long componentId, Long userId, Date now, IaasCatalogVirtualMachineComponentScriptVO scriptVO) {
        IaasCatalogVirtualMachineComponentScript componentScript = new IaasCatalogVirtualMachineComponentScript();
        BeanUtils.copyProperties(scriptVO, componentScript);

        componentScript.setId(componentScriptId);
        componentScript.setScriptId(scriptVO.getId());
        componentScript.setVmComponentId(componentId);
        componentScript.setCreateUser(userId);
        componentScript.setCreateTime(now);
        componentScript.setUpdateTime(now);
        componentScript.setUpdateUser(userId);
        componentScript.setIsDeleted(0);
        componentScript.setTenantId(null);

        iaasCatalogVirtualMachineComponentScriptMapper.insert(componentScript);
        log.info("??????????????????????????????????????????");
    }

    private void saveVirtualMachineComponentParam(long componentId, Long userId, Date now, IaasCatalogVirtualMachineComponentParamVO componentParamVO) {
        IaasCatalogVirtualMachineComponentParam componentParam = new IaasCatalogVirtualMachineComponentParam();
        BeanUtils.copyProperties(componentParamVO, componentParam);

        componentParam.setId(SnowFlakeIdGenerator.getInstance().nextId());
        componentParam.setVmComponentId(componentId);
        componentParam.setComponentParamId(componentParamVO.getId());
        componentParam.setCreateUser(userId);
        componentParam.setCreateTime(now);
        componentParam.setUpdateTime(now);
        componentParam.setUpdateUser(userId);
        componentParam.setIsDeleted(0);

        iaasCatalogVirtualMachineComponentParamMapper.insert(componentParam);
        log.info("??????????????????????????????????????????");
    }

    private void virtualComponent(Long parentId, Long componentId, long machineId, Long userId, Date now, IaasCatalogVirtualMachineComponentVO componentVO) {
        IaasCatalogVirtualMachineComponent component = new IaasCatalogVirtualMachineComponent();
        BeanUtils.copyProperties(componentVO, component);

        component.setId(componentId);
        component.setDisplayName(componentVO.getTitle());
        component.setComponentId(componentVO.getKey());
        component.setVmId(machineId);
        component.setCreateUser(userId);
        component.setCreateTime(now);
        component.setUpdateTime(now);
        component.setUpdateUser(userId);
        component.setIsDeleted(0);
        component.setParentId(parentId);

        iaasCatalogVirtualMachineComponentMapper.insert(component);
        log.info("????????????????????????????????????");
    }

    private void saveVirtualMachineDisk(Long machineId, Long userId, Date now, IaasCatalogVirtualMachineTreeVO machineVO) {

        List<IaasCatalogVirtualMachineDiskVO> machineDiskVOS = machineVO.getMachineDiskVOList();

        if (CollectionUtils.isEmpty(machineDiskVOS)) {
            log.error("?????????????????????!");
            return;
        }

        for (IaasCatalogVirtualMachineDiskVO diskVO : machineDiskVOS) {
            if (diskVO == null) {
                continue;
            }
            virtualMachineDisk(machineId, userId, now, diskVO);
        }
    }

    private void virtualMachineDisk(Long machineId, Long userId, Date now, IaasCatalogVirtualMachineDiskVO diskVO) {
        IaasCatalogVirtualMachineDisk disk = new IaasCatalogVirtualMachineDisk();
        BeanUtils.copyProperties(diskVO, disk);

        disk.setId(SnowFlakeIdGenerator.getInstance().nextId());
        disk.setVmId(machineId);
        disk.setCreateUser(userId);
        disk.setCreateTime(now);
        disk.setUpdateTime(now);
        disk.setUpdateUser(userId);
        disk.setIsDeleted(0);

        iaasCatalogVirtualMachineDiskMapper.insert(disk);
        log.info("????????????????????????????????????");
    }

    private void saveCatalogVirtualMachine(Long catalogId, Long machineId, Long userId, Date now, IaasCatalogVirtualMachineTreeVO machineVO) {
        IaasCatalogVirtualMachin machine = new IaasCatalogVirtualMachin();
        BeanUtils.copyProperties(machineVO, machine);
        machine.setId(machineId);
        machine.setVmName(machineVO.getTitle());
        machine.setCatalogId(catalogId);
        machine.setCreateUser(userId);
        machine.setCreateTime(now);
        machine.setUpdateTime(now);
        machine.setUpdateUser(userId);
        machine.setIsDeleted(0);

        iaasCatalogVirtualMachinMapper.insert(machine);
        log.info("??????????????????????????????", machineId);
    }

    private void updateIaasCatalog(Long providerId, Integer state, Long catalogId, Long userId, Date now) {
        IaasCatalog catalog = new IaasCatalog();
        catalog.setId(catalogId);
        catalog.setState(state);
        catalog.setProviderId(providerId);
        catalog.setUpdateUser(userId);
        catalog.setUpdateTime(now);
        iaasCatalogMapper.updateIaasCatalog(catalog);
        log.info("?????????????????????????????????", catalogId);
    }
}

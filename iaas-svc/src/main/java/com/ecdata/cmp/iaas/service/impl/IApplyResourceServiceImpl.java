package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.activiti.client.ActivitiClient;
import com.ecdata.cmp.activiti.client.WorkflowClient;
import com.ecdata.cmp.activiti.dto.ProcessDTO;
import com.ecdata.cmp.activiti.dto.request.CandidateRequest;
import com.ecdata.cmp.activiti.dto.request.CompleteTaskRequest;
import com.ecdata.cmp.activiti.dto.response.ActTaskListResponse;
import com.ecdata.cmp.activiti.dto.response.ActTaskPageResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowResponse;
import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;
import com.ecdata.cmp.activiti.dto.vo.ActTaskVO;
import com.ecdata.cmp.activiti.dto.vo.WorkflowStepVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.CloudVmClient;
import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.iaas.entity.SysFile;
import com.ecdata.cmp.iaas.entity.apply.*;
import com.ecdata.cmp.iaas.entity.dto.apply.*;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import com.ecdata.cmp.iaas.entity.dto.request.IaasApplyRequest;
import com.ecdata.cmp.iaas.entity.dto.response.apply.ApplyResponse;
import com.ecdata.cmp.iaas.mapper.apply.*;
import com.ecdata.cmp.iaas.mapper.file.SysFileMapper;
import com.ecdata.cmp.iaas.service.IApplyResourceService;
import com.ecdata.cmp.iaas.service.IAuditService;
import com.ecdata.cmp.iaas.utils.PDFReport;
import com.ecdata.cmp.user.client.RoleClient;
import com.ecdata.cmp.user.client.UserClient;
import com.ecdata.cmp.user.dto.response.UserListResponse;
import com.ecdata.cmp.user.dto.response.UserResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/3/2 14:47
 */
@Service
public class IApplyResourceServiceImpl implements IApplyResourceService {
    private static Logger logger = LoggerFactory.getLogger(IApplyResourceServiceImpl.class);
    private static Long SYS_USER = 2777994821132290L;
    @Value("${pdf.download-url}")
    private String downloadUrl;

    @Autowired
    private IApplyResourceService applyResourceService;
    @Autowired
    private IaasApplyResourceMapper iaasResourceApplyMapper;

    @Autowired
    private IaasApplyConfigInfoMapper iaasApplyConfigInfoMapper;

    @Autowired
    private IaasApplyMemoryDiskMapper iaasApplyMemoryDiskMapper;

    @Autowired
    private IaasApplyPortMappingAskMapper iaasApplyPortMappingAskMapper;

    @Autowired
    private IaasApplyNetworkAskMapper iaasApplyNetworkAskMapper;

    @Autowired
    private IaasApplyServiceSecurityResourcesMapper serviceSecurityResourcesMapper;

    @Autowired
    private IaasApplyCalculateMapper iaasApplyCalculateMapper;

    @Autowired
    private IaasApplyNetworkPolicyMapper iaasApplyNetworkPolicyMapper;

    @Autowired
    private IaasApplySecurityServerMapper iaasApplySecurityServerMapper;

    @Autowired
    private IaasApplyStorageMapper iaasApplyStorageMapper;

    @Autowired
    private IaasApplySoftwareServerMapper iaasApplySoftwareServerMapper;

    @Autowired
    private IaasApplyOtherMapper iaasApplyOtherMapper;

    @Autowired
    private ActivitiClient activitiClient;

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private IaasApplyRelationInfoMapper relationInfoMapper;

    @Autowired
    private IAuditService auditService;

    @Autowired
    private CloudVmClient cloudVmClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RoleClient roleClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplyResponse saveResourceApply(IaasApplyRequest vo) {
        ApplyResponse baseResponse = new ApplyResponse();

        Optional.ofNullable(queryApplyResourceByProjectId(vo.businessGroupId()))
                .map(IaasApplyConfigInfoListVO::getConfigInfoList).filter(CollectionUtils::isNotEmpty)
                .flatMap(e -> e.stream().findFirst().map(IaasApplyConfigInfoVO::getApplyId))
                .ifPresent(e -> Optional.ofNullable(vo.getIaasApplyResourceVO()).ifPresent(t -> t.setId(e)));

        //1.??????????????????
        Long applyId = vo.applyId() == null ? SnowFlakeIdGenerator.getInstance().nextId() : vo.applyId();

        IaasApplyResource applyResource = iaasResourceApplyMapper.selectById(applyId);

        handleResource(applyId, "0", vo.getIaasApplyResourceVO(), applyResource);

        //??????????????????
        handleFile(applyId, vo.getIaasApplyResourceVO());

        //??????????????????????????????0???????????????????????????
        if (vo.vmNum() > 1) {
            for (int i = 0; i < vo.vmNum(); i++) {
                //??????????????????
                handleApply(i + 1, applyId, "0", vo.getIaasApplyConfigInfoVO(), vo.getIaasApplyServiceSecurityResourcesVO(), vo.getIaasApplyNetworkAskVO());
            }
        } else {
            handleApply(0, applyId, "0", vo.getIaasApplyConfigInfoVO(), vo.getIaasApplyServiceSecurityResourcesVO(), vo.getIaasApplyNetworkAskVO());
        }

        if (applyResource == null) {
            //??????????????????
            callActivitis(false, "0", applyId, baseResponse, vo.getIaasApplyResourceVO());
        }
        baseResponse.setApplyId(applyId);
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteApplyAll(Long applyId) {
        BaseResponse baseResponse = new BaseResponse();
        List<IaasApplyResourceVO> applyResourceVOs = iaasResourceApplyMapper.queryApplyResource(applyId);

        if (CollectionUtils.isEmpty(applyResourceVOs)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("???????????????");
            return baseResponse;
        }

        IaasApplyResourceVO applyResourceVO = applyResourceVOs.get(0);
        IaasApplyResource applyResource = new IaasApplyResource();
        applyResource.setId(applyResourceVO.getId());
        applyResource.setDeleted(true);
        iaasResourceApplyMapper.updateById(applyResource);

        //??????????????????
//        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(SysFile::getBusinessId, applyId);
//        sysFileMapper.delete(queryWrapper);

        List<IaasApplyConfigInfoVO> configInfoVOList = applyResourceVO.getIaasApplyConfigInfoVOList();
        if (CollectionUtils.isNotEmpty(configInfoVOList)) {
            for (IaasApplyConfigInfoVO configInfoVO : configInfoVOList) {
                deleteApplyConfigInfo(configInfoVO);
            }
        }

        if (StringUtils.isNotBlank(applyResourceVO.getProcessInstanceId())) {
            try {
                //??????????????????
                baseResponse = activitiClient.deleteProcessInstance(AuthContext.getAuthz(), applyResourceVO.getProcessInstanceId(), "", 1);
            } catch (Exception e) {
                logger.error("????????????????????????!", e);
            }
        }
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteApplyResource(Long configId) {
        BaseResponse baseResponse = new BaseResponse();
        List<IaasApplyConfigInfoVO> configInfoVOList = iaasApplyConfigInfoMapper.queryApplyConfigInfoList(null, configId);

        if (CollectionUtils.isEmpty(configInfoVOList)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("???????????????");
            return baseResponse;
        }

        deleteApplyConfigInfo(configInfoVOList.get(0));
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateResourceApply(IaasApplyRequest vo) {
        //??????????????????
        IaasApplyResourceVO applyResourceVO = vo.getIaasApplyResourceVO();

        IaasApplyResource applyResource = new IaasApplyResource();
        BeanUtils.copyProperties(applyResourceVO, applyResource);
        applyResource.setUpdateTime(DateUtil.getNow());
        applyResource.setUpdateUser(Sign.getUserId());
        iaasResourceApplyMapper.updateById(applyResource);

        //????????????
        handleFile(applyResource.getId(), applyResourceVO);

        //??????????????????
        updateConfig(vo);

        BaseResponse baseResponse = new BaseResponse();
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (vo.getIaasApplyResourceVO().getState() == 1) {
            //?????????????????????
            completeActiviti(vo.getIaasApplyResourceVO().getProcessInstanceId(), businessDetail("0", vo.getIaasApplyResourceVO()), baseResponse);
        }

        return baseResponse;
    }

    @Override
    public BaseResponse startResourceApply(IaasApplyResourceVO vo) {
        BaseResponse baseResponse = new BaseResponse();
        IaasApplyResource applyResource = new IaasApplyResource();
        applyResource.setId(vo.getId());
        applyResource.setState(vo.getState());
        applyResource.setUpdateUser(Sign.getUserId());
        applyResource.setUpdateTime(DateUtil.getNow());

        iaasResourceApplyMapper.updateById(applyResource);
        if (vo.getState() == 1) {
            //?????????????????????
            completeActiviti(vo.getProcessInstanceId(), businessDetail("0", vo), baseResponse);
        }
        return baseResponse;
    }

    @Override
    public BaseResponse submitApply(IaasApplyResourceVO applyResourceVO) {
        //??????pdf
        String pdfUrl = generatePdf(applyResourceVO.getId(), applyResourceVO.getBase64Pdf());

        IaasApplyResource applyResource = new IaasApplyResource();
        applyResource.setId(applyResourceVO.getId());
        applyResource.setState(1);
        applyResource.setPdfUrl(pdfUrl);
        applyResource.setUpdateUser(Sign.getUserId());
        applyResource.setUpdateTime(DateUtil.getNow());
        iaasResourceApplyMapper.updateById(applyResource);

        BaseResponse baseResponse = new BaseResponse();

        if (applyResourceVO.getState() == 1) {
            //?????????????????????
            completeActiviti(applyResourceVO.getProcessInstanceId(), businessDetail("0", applyResourceVO), baseResponse);
        }
        if (applyResourceVO != null && applyResourceVO.getUserId() != null)
            applyResourceService.addCandidate(applyResourceVO.getId(), applyResourceVO.getUserId());

        return baseResponse;
    }

    @Override
    public List<IaasApplyResourceVO> queryApplyResource(Long applyId) {
        if (applyId == null) {
            return new ArrayList<>();
        }
        List<IaasApplyResourceVO> applyResourceVOs = iaasResourceApplyMapper.queryApplyResource(applyId);
        return applyResourceVOs;
    }

    @Override
    public IPage<IaasApplyResourceVO> queryApplyResource(Page<IaasApplyResourceVO> page, Map<String, Object> params) {
        //  ????????????????????????
        IPage<IaasApplyResourceVO> iaasApplyResourceVOIPage = iaasResourceApplyMapper.queryApplyResourcePage(page, params);
        List<IaasApplyResourceVO> applyResources = iaasApplyResourceVOIPage.getRecords();
        if (applyResources != null && applyResources.size() > 0){
            for(IaasApplyResourceVO apply: applyResources) {
                List<ActHistoricTaskInstanceVO> actHistoricTaskInstanceVOS = auditService.queryProcessTracking(apply.getProcessInstanceId());
                if (actHistoricTaskInstanceVOS != null && actHistoricTaskInstanceVOS.size() > 0){
                    apply.setProcessNode(actHistoricTaskInstanceVOS.get(0).getName());
                }
                if (StringUtils.equals(apply.getProcessNode(),"????????????")){
                    apply.setProcessState("1");
                }if (StringUtils.equals(apply.getProcessNode(),"IT????????????")){
                    apply.setProcessState("2");
                }if (StringUtils.equals(apply.getProcessNode(),"????????????")){
                    apply.setProcessState("3");
                }if (StringUtils.equals(apply.getProcessNode(),"????????????")){
                    apply.setProcessState("4");
                }if (StringUtils.equals(apply.getProcessNode(),"??????????????????")){
                    apply.setProcessState("5");
                }if (StringUtils.equals(apply.getProcessNode(),"??????????????????")){
                    apply.setProcessState("6");
                }if (StringUtils.equals(apply.getProcessNode(),"????????????")){
                    apply.setProcessState("7");
                }if (StringUtils.equals(apply.getProcessNode(),"????????????")){
                    apply.setProcessState("8");
                }if (StringUtils.equals(apply.getProcessNode(),"???????????????")){
                    apply.setProcessState("9");
                }if (StringUtils.equals(apply.getProcessNode(),null) || StringUtils.equals(apply.getProcessNode(),"")){
                    apply.setProcessState("0");
                }
            }
        }
        return iaasApplyResourceVOIPage;
    }

    @Override
    public IaasApplyConfigInfoVO queryApplyConfigInfo(Long configId) {
        List<IaasApplyConfigInfoVO> configInfoVOList = iaasApplyConfigInfoMapper.queryApplyConfigInfoList(null, configId);

        if (CollectionUtils.isEmpty(configInfoVOList)) {
            return new IaasApplyConfigInfoVO();
        }
        return configInfoVOList.get(0);
    }

    @Override
    public int queryApplyServerNamePrefix(String serverNamePrefix) {
        int  serverNamePrefix1 = iaasApplyConfigInfoMapper.queryApplyServerNamePrefix(serverNamePrefix);
        return serverNamePrefix1;
    }

    @Override
    public BaseResponse updateApplyConfigInfo(IaasApplyRequest applyRequest) {

        updateConfig(applyRequest);
        return BaseResponse.builder().build();
    }

    @Override
    public IaasApplyResourceVO queryApplyDetails(Long applyId) {
        List<IaasApplyResourceVO> iaasApplyResourceVOS = iaasResourceApplyMapper.queryApplyResource(applyId);
        if (CollectionUtils.isEmpty(iaasApplyResourceVOS)) {
            return new IaasApplyResourceVO();
        }

        IaasApplyResourceVO iaasApplyResourceVO = iaasApplyResourceVOS.get(0);

        iaasApplyResourceVO.setIaasApplyCalculateVOS(iaasApplyCalculateMapper.queryApplyCalculate(applyId));
        iaasApplyResourceVO.setIaasApplyNetworkPolicyVOS(iaasApplyNetworkPolicyMapper.queryApplyNetworkPolicy(applyId));
        iaasApplyResourceVO.setIaasApplySecurityServerVOS(iaasApplySecurityServerMapper.querySecurityServer(applyId));
        iaasApplyResourceVO.setIaasApplyStorageVOS(iaasApplyStorageMapper.queryApplyStorage(applyId));
        iaasApplyResourceVO.setIaasApplySoftwareServerVOS(iaasApplySoftwareServerMapper.queryApplySoftwareServer(applyId));
        iaasApplyResourceVO.setIaasApplyOtherVOS(iaasApplyOtherMapper.queryApplyOther(applyId));
        return iaasApplyResourceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyResourceAndStartRecoveryApply(IaasApplyResourceVO applyResourceVO) {
        // ??????????????????
        List<IaasApplyConfigInfoVO> configInfoVOList = applyResourceVO.getIaasApplyConfigInfoVOList();
        Iterator<IaasApplyConfigInfoVO> it = configInfoVOList.iterator();
        while(it.hasNext()){
            IaasApplyConfigInfoVO configInfoVO = it.next();
            if (!StringUtils.equals(configInfoVO.getApplyType(), "1")){
                it.remove();
            }
        }
        //  hhj ????????????PDF???????????????base64
        String base64PDF = "";
        String random = RandomStringUtils.randomAlphanumeric(Constants.TEN);
        try{
            File file = new File(random + ".pdf");
            new PDFReport(file).generatePDF(applyResourceVO);
            base64PDF = PDFToBase(file);
            file.delete();
        }catch (Exception e){
            logger.warn("????????????PDF???????????????");
        }
        String pdfUrl = generatePdfTiming(applyResourceVO.getId(), base64PDF, applyResourceVO.getCreateUser());
        applyResourceVO.setPdfUrl(pdfUrl);
        IaasApplyResource updateResource = new IaasApplyResource();
        updateResource.setState(3);
        updateResource.setId(applyResourceVO.getId());
        iaasResourceApplyMapper.updateById(updateResource);
        Long applyId = SnowFlakeIdGenerator.getInstance().nextId();

        handleResource(applyId, "1", applyResourceVO, null);

        //????????????????????????
        BaseResponse baseResponse = new BaseResponse();
        if (CollectionUtils.isNotEmpty(configInfoVOList)) {
            for (IaasApplyConfigInfoVO configInfoVO : configInfoVOList) {
                handleApply(0, applyId, "1", configInfoVO, configInfoVO.getIaasApplyServiceSecurityResourcesVO(), configInfoVO.getIaasApplyNetworkAskVO());
            }
            //????????????????????????
            baseResponse = callActivitis(false, "1", applyId, baseResponse, applyResourceVO);
            return baseResponse.getMessage();
        }
        return "????????????????????????";
    }

    @Override
    public IaasApplyConfigInfoListVO queryApplyResourceByProjectId(Long projectId) {
        LambdaQueryWrapper<IaasApplyResource> query = new LambdaQueryWrapper<>();
        query.eq(IaasApplyResource::getBusinessGroupId, projectId);
        List<IaasApplyResource> iaasApplyResources = iaasResourceApplyMapper.selectList(query);

        IaasApplyConfigInfoListVO configInfoListVO = new IaasApplyConfigInfoListVO();
        if (CollectionUtils.isNotEmpty(iaasApplyResources)) {
            List<Long> approvalEd = iaasApplyResources.stream().filter(s -> s.getState() == 2).map(IaasApplyResource::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(approvalEd)) {
                Optional.ofNullable(iaasApplyConfigInfoMapper.queryApplyConfigInfoListByApplyId(approvalEd))
                        .filter(CollectionUtils::isNotEmpty)
                        .ifPresent(e -> configInfoListVO.setApprovalEdConfigInfoList(
                                e.stream().peek(m -> m.setState(3)).collect(Collectors.toList())));
            }

            List<Long> draft = iaasApplyResources.stream().filter(s -> s.getState() == 0).map(IaasApplyResource::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(draft)) {
                Optional.ofNullable(iaasApplyConfigInfoMapper.queryApplyConfigInfoListByApplyId(draft))
                        .filter(CollectionUtils::isNotEmpty)
                        .ifPresent(e -> configInfoListVO.setConfigInfoList(
                                e.stream().peek(m -> m.setState(1)).collect(Collectors.toList())));
            }

            List<Long> inApproval = iaasApplyResources.stream().filter(s -> (s.getState() != 2 && s.getState() != 0)).map(IaasApplyResource::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(inApproval)) {
                Optional.ofNullable(iaasApplyConfigInfoMapper.queryApplyConfigInfoListByApplyId(inApproval))
                        .filter(CollectionUtils::isNotEmpty)
                        .ifPresent(e -> configInfoListVO.setInApprovalConfigInfoList(
                                e.stream().peek(m -> m.setState(2)).collect(Collectors.toList())));
            }
        }

        return configInfoListVO;
    }

    @Override
    public ApplyResponse saveChangeResourceApply(IaasApplyRequest vo) {
        ApplyResponse baseResponse = new ApplyResponse();

        //1.??????????????????
        Long applyId = vo.applyId() == null ? SnowFlakeIdGenerator.getInstance().nextId() : vo.applyId();
        IaasApplyResource applyResource = iaasResourceApplyMapper.selectById(applyId);

        handleResource(applyId, "0", vo.getIaasApplyResourceVO(), applyResource);

        //??????????????????
        handleFile(applyId, vo.getIaasApplyResourceVO());

        //??????????????????????????????0???????????????????????????
//        if (vo.vmNum() > 1) {
//            for (int i = 0; i < vo.vmNum(); i++) {
//                //??????????????????
//                handleApply(i + 1, applyId, "0", vo.getIaasApplyConfigInfoVO(), vo.getIaasApplyServiceSecurityResourcesVO(), vo.getIaasApplyNetworkAskVO());
//            }
//        } else {
//            handleApply(0, applyId, "0", vo.getIaasApplyConfigInfoVO(), vo.getIaasApplyServiceSecurityResourcesVO(), vo.getIaasApplyNetworkAskVO());
//        }

        if (applyResource == null) {
            //??????????????????
            callActivitis(false, "0", applyId, baseResponse, vo.getIaasApplyResourceVO());
        }
        baseResponse.setApplyId(applyId);
        return baseResponse;
    }

    @Override
    public IaasApplyResourceVO copyResourceApply(Long OldApplyId) {
        List<IaasApplyResourceVO> iaasApplyResourceVOS = iaasResourceApplyMapper.queryApplyResource(OldApplyId);

        IaasApplyResourceVO applyResourceVO = iaasApplyResourceVOS.get(0);
        applyResourceVO.setState(0);
        applyResourceVO.setCreateUser(Sign.getUserId());
        applyResourceVO.setCreateUserName(Sign.getUserDisplayName());
        Long applyId = SnowFlakeIdGenerator.getInstance().nextId();

        handleResource(applyId, "0", applyResourceVO, null);

        //????????????????????????
//        List<IaasApplyConfigInfoVO> configInfoVOList = applyResourceVO.getIaasApplyConfigInfoVOList();
//        if (CollectionUtils.isNotEmpty(configInfoVOList)) {
//            for (IaasApplyConfigInfoVO configInfoVO : configInfoVOList) {
//                handleApply(0, applyId, "0", configInfoVO, configInfoVO.getIaasApplyServiceSecurityResourcesVO(), configInfoVO.getIaasApplyNetworkAskVO());
//            }
//        }

        List<IaasApplyResourceVO> response = iaasResourceApplyMapper.queryApplyResource(applyId);
        return response.get(0);
    }

    @Override
    public IaasApplyResourceVO queryApplyRelationDetails(Long applyId) {
        List<IaasApplyResourceVO> iaasApplyResourceVOS = iaasResourceApplyMapper.queryApplyResource(applyId);
        if (CollectionUtils.isEmpty(iaasApplyResourceVOS)) {
            return new IaasApplyResourceVO();
        }

        IaasApplyResourceVO iaasApplyResourceVO = iaasApplyResourceVOS.get(0);

        //????????????????????????
        LambdaQueryWrapper<IaasApplyRelationInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IaasApplyRelationInfo::getProcessInstanceId, iaasApplyResourceVO.getProcessInstanceId());
//        queryWrapper.eq(IaasApplyRelationInfo::getRelationUser, Sign.getUserId());
        List<IaasApplyRelationInfo> iaasApplyRelationInfos = relationInfoMapper.selectList(queryWrapper);

        List<Long> businessIds = iaasApplyRelationInfos.stream().map(IaasApplyRelationInfo::getBusinessId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(businessIds)) {
            return iaasApplyResourceVO;
        }
//        if (CollectionUtils.isNotEmpty(businessIds)) {
            iaasApplyResourceVO.setIaasApplyCalculateVOS(iaasApplyCalculateMapper.queryApplyBatchCalculate(businessIds));
            iaasApplyResourceVO.setIaasApplyNetworkPolicyVOS(iaasApplyNetworkPolicyMapper.queryBatchApplyNetworkPolicy(businessIds));
            iaasApplyResourceVO.setIaasApplySecurityServerVOS(iaasApplySecurityServerMapper.queryBatchSecurityServer(businessIds));
            iaasApplyResourceVO.setIaasApplyStorageVOS(iaasApplyStorageMapper.queryBatchApplyStorage(businessIds));
            iaasApplyResourceVO.setIaasApplySoftwareServerVOS(iaasApplySoftwareServerMapper.queryBatchApplySoftwareServer(businessIds));
            iaasApplyResourceVO.setIaasApplyOtherVOS(iaasApplyOtherMapper.queryBatchApplyOther(businessIds));
//        } else {
//        iaasApplyResourceVO.setIaasApplyCalculateVOS(iaasApplyCalculateMapper.queryApplyCalculate(applyId));
//        iaasApplyResourceVO.setIaasApplyNetworkPolicyVOS(iaasApplyNetworkPolicyMapper.queryApplyNetworkPolicy(applyId));
//        iaasApplyResourceVO.setIaasApplySecurityServerVOS(iaasApplySecurityServerMapper.querySecurityServer(applyId));
//        iaasApplyResourceVO.setIaasApplyStorageVOS(iaasApplyStorageMapper.queryApplyStorage(applyId));
//        iaasApplyResourceVO.setIaasApplySoftwareServerVOS(iaasApplySoftwareServerMapper.queryApplySoftwareServer(applyId));
//        iaasApplyResourceVO.setIaasApplyOtherVOS(iaasApplyOtherMapper.queryApplyOther(applyId));
//        }
        return iaasApplyResourceVO;
    }

    @Override
    public void removeResourceAndApplyInfo(Long applyId) {
        iaasApplyConfigInfoMapper.update(null, Wrappers.<IaasApplyConfigInfo>lambdaUpdate()
                .set(IaasApplyConfigInfo::getApplyId, null)
                .eq(IaasApplyConfigInfo::getApplyId, applyId));
    }

    @Override
    public ApplyResponse handleConfigChange(IaasApplyRequest vo) {
        ApplyResponse baseResponse = new ApplyResponse();

        Optional.ofNullable(queryApplyResourceByProjectId(vo.businessGroupId()))
                .map(IaasApplyConfigInfoListVO::getConfigInfoList).filter(CollectionUtils::isNotEmpty)
                .flatMap(e -> e.stream().findFirst().map(IaasApplyConfigInfoVO::getApplyId))
                .ifPresent(e -> Optional.ofNullable(vo.getIaasApplyResourceVO()).ifPresent(t -> t.setId(e)));

        //1.??????????????????
        Long applyId = vo.applyId() == null ? SnowFlakeIdGenerator.getInstance().nextId() : vo.applyId();

        IaasApplyResource applyResource = iaasResourceApplyMapper.selectById(applyId);

        handleResource(applyId, "0", vo.getIaasApplyResourceVO(), applyResource);

        List<IaasApplyConfigInfoVO> configInfoVOList = iaasApplyConfigInfoMapper.queryApplyConfigInfoList(null, vo.getConfigId());

        if (CollectionUtils.isEmpty(configInfoVOList)) {
            return baseResponse;
        }

        IaasApplyConfigInfoVO configInfoVO = configInfoVOList.get(0);
        configInfoVO.setOperationType(vo.getType());

        handleApply(0, applyId, "0", configInfoVO, configInfoVO.getIaasApplyServiceSecurityResourcesVO(), configInfoVO.getIaasApplyNetworkAskVO());

        if (applyResource == null) {
            //??????????????????
            callActivitis(false, "0", applyId, baseResponse, vo.getIaasApplyResourceVO());
        }
        baseResponse.setApplyId(applyId);
        return baseResponse;
    }

    @Override
    public String queryApplyResourceByProcessInstanceId(String processInstanceId) {
        IaasApplyResourceVO iaasApplyResourceVO = iaasResourceApplyMapper.queryApplyResourceByPro(processInstanceId);
        List<CloudVmVO> cloudVmVOS = null;
        if (iaasApplyResourceVO != null){
            cloudVmVOS = iaasResourceApplyMapper.queryCloudVm(iaasApplyResourceVO.getId());
            logger.info("cloudVmVOS={}", cloudVmVOS);
        }
        // ?????????????????????
        if (CollectionUtils.isEmpty(cloudVmVOS)) {
            return "??????????????????????????????";
        }else{
            for (CloudVmVO cloudVmVO : cloudVmVOS) {
                try {
                    String authz = AuthContext.getAuthz();
                    cloudVmClient.add(authz, cloudVmVO);
                } catch (Exception e) {
                    return "????????????????????????";
                }
            }
            return "????????????????????????!";
        }
    }

    @Override
    public List<IaasApplyResourceVO> recycleResourcesAuto() {
        //??????????????????????????????
        List<IaasApplyResourceVO> applyResourceVOList = iaasResourceApplyMapper.queryApplyResourceLeaseExpire(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return applyResourceVOList;
    }

    @Override
    public IaasApplyConfigInfoVO queryApplyServiceSecurityResources(int applyType, String sysBusinessGroupId) {
        List<IaasApplyConfigInfoVO> vos = iaasResourceApplyMapper.queryApplyServiceSecurityResources(applyType, sysBusinessGroupId);
        IaasApplyConfigInfoVO configInfoVO = null;
        IaasApplyServiceSecurityResourcesVO iaasApplyServiceSecurityResourcesVO = null;
        if(vos != null && vos.size() > 0){
            configInfoVO = vos.get(0);
            if (configInfoVO != null){

                if (configInfoVO.getIaasApplyServiceSecurityResourcesVO() != null){
                    iaasApplyServiceSecurityResourcesVO = configInfoVO.getIaasApplyServiceSecurityResourcesVO();
                }else{
                    configInfoVO.setIaasApplyServiceSecurityResourcesVO(iaasApplyServiceSecurityResourcesVO);
                }
                iaasApplyServiceSecurityResourcesVO.setIsDas(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsDas()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsSslVpn(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsSslVpn()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsLogAudit(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsLogAudit()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsLoopholeScan(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsLoopholeScan()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsNextNfw(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsNextNfw()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsSinforAc(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsSinforAc()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsAntivirus(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsAntivirus()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsBastionHost(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsBastionHost()).orElse("0"));
                iaasApplyServiceSecurityResourcesVO.setIsSecurityPlatform(Optional.ofNullable(iaasApplyServiceSecurityResourcesVO.getIsSecurityPlatform()).orElse("0"));
                for (IaasApplyConfigInfoVO vo: vos){
                    if (vo != null && vo.getIaasApplyServiceSecurityResourcesVO() != null){
                        IaasApplyServiceSecurityResourcesVO securityResourcesVO = vo.getIaasApplyServiceSecurityResourcesVO();
                        if (securityResourcesVO != null){
                            if (StringUtils.equals(securityResourcesVO.getIsDas(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsDas("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsSslVpn(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsSslVpn("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsLogAudit(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsLogAudit("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsLoopholeScan(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsLoopholeScan("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsNextNfw(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsNextNfw("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsSinforAc(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsSinforAc("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsAntivirus(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsAntivirus("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsBastionHost(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsBastionHost("1");
                            } if (StringUtils.equals(securityResourcesVO.getIsSecurityPlatform(),"1")){
                                iaasApplyServiceSecurityResourcesVO.setIsSecurityPlatform("1");
                            }
                        }
                    }
                }
            }
            return configInfoVO;
        }

        return null;
    }

    @Override
    public void addCandidate(Long id, Long userId) {
        //  ??????taskId
        List<IaasApplyResourceVO> iaasApplyResourceVOList = iaasResourceApplyMapper.queryApplyResource(id);
        String processInstanceId = iaasApplyResourceVOList.get(0).getProcessInstanceId();
        ActTaskListResponse actTaskListResponse = activitiClient.queryRunTask(processInstanceId, false);
        List<ActTaskVO> actTaskVOList = actTaskListResponse.getData();
        String taskId = actTaskVOList.get(0).getId();
        //  ???????????????
        // 1. ?????????????????????IT???????????????????????? 107785981244067841
        List<Long> roleIdList = new ArrayList<>();
        WorkflowResponse detailById = workflowClient.getDetailById(152777148528160771L);
        List<WorkflowStepVO> workflowStepVOList = detailById.getData().getWorkflowStepVOList();
        if (workflowStepVOList != null && workflowStepVOList.size() > 0){
            for (WorkflowStepVO workflowStepVO : workflowStepVOList) {
                if (workflowStepVO.getStepName().equals("IT????????????")){
                    roleIdList = workflowStepVO.getRightDTO().getRoleIdList();
                }
            }
        }
        // 2. ???????????????
        if (roleIdList != null && roleIdList.size() > 0){
            CandidateRequest candidateRequest = new CandidateRequest();
            candidateRequest.setType(2);
            candidateRequest.setTaskId(taskId);
            for (Long role : roleIdList) {
                candidateRequest.setCandidateId(role);
                activitiClient.delCandidateUser(candidateRequest);
            }
        }

        //  ???????????????
        if(userId != null && taskId != null){
            CandidateRequest candidateRequest = new CandidateRequest();
            candidateRequest.setType(1); // ??????
            candidateRequest.setTaskId(taskId); // taskId
            candidateRequest.setCandidateId(userId); // ?????????????????????ID
            activitiClient.addCandidate(candidateRequest);
            logger.info("?????????????????????!");
        }
    }

    @Override
    public UserListResponse getITDirectors() {
        List<Long> roleIdList = new ArrayList<>();
        WorkflowResponse detailById = workflowClient.getDetailById(152777148528160771L);
        List<WorkflowStepVO> workflowStepVOList = detailById.getData().getWorkflowStepVOList();
        if (workflowStepVOList != null && workflowStepVOList.size() > 0){
            for (WorkflowStepVO workflowStepVO : workflowStepVOList) {
                if (workflowStepVO.getStepName().equals("IT????????????")){
                    roleIdList = workflowStepVO.getRightDTO().getRoleIdList();
                }
            }
        }
        UserListResponse userListResponse = new UserListResponse();
        if (roleIdList != null && roleIdList.size() > 0){
            userListResponse = roleClient.getITDirectors(roleIdList.get(0));
        }
        return userListResponse;
    }



    private String generatePdf(Long applyId, String base64Pdf) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = null;
        try {
            imageByte = decoder.decodeBuffer(base64Pdf.split("base64,")[1]);
            for (int i = 0; i < imageByte.length; ++i) {
                if (imageByte[i] < 0) {// ??????????????????
                    imageByte[i] += 256;
                }
            }

            SysFile uploadFile = new SysFile();
            long id = SnowFlakeIdGenerator.getInstance().nextId();
            uploadFile.setId(id);
            uploadFile.setBusinessId(applyId);
            uploadFile.setFileName(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + ".pdf");
            uploadFile.setCreateUser(Sign.getUserId());
            uploadFile.setCreateTime(new Date());
            uploadFile.setIsDeleted(0);
            uploadFile.setImg(imageByte);
            sysFileMapper.insert(uploadFile);
            return downloadUrl + id;
        } catch (Exception e) {
            return "";
        }
    }

    private String generatePdfTiming(Long applyId, String base64Pdf, long createUser) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = null;
        try {
            imageByte = decoder.decodeBuffer(base64Pdf);
            for (int i = 0; i < imageByte.length; ++i) {
                if (imageByte[i] < 0) {// ??????????????????
                    imageByte[i] += 256;
                }
            }

            SysFile uploadFile = new SysFile();
            long id = SnowFlakeIdGenerator.getInstance().nextId();
            uploadFile.setId(id);
            uploadFile.setBusinessId(applyId);
            uploadFile.setFileName(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + ".pdf");
            uploadFile.setCreateUser(createUser);
            uploadFile.setCreateTime(new Date());
            uploadFile.setIsDeleted(0);
            uploadFile.setImg(imageByte);
            sysFileMapper.insert(uploadFile);
            return downloadUrl + id;
        } catch (Exception e) {
            return "";
        }
    }

    //??????????????????
    public BaseResponse callActivitis(boolean isGroup,
                                      String operationType,
                                      long applyId,
                                      BaseResponse baseResponse,
                                      IaasApplyResourceVO applyResourceVO) {
        //??????businessDetail {id:"ddd",name:""ss}
        String businessDetail = businessDetail(operationType, applyResourceVO);

        //????????????
        Long createUser = applyResourceVO.getCreateUser();
        JSONObjectResponse jsonObjectResponse = activitiClient.startProcess(assemblerProcessDTO(applyId, createUser, applyResourceVO.getBusinessActivitiId(),
                applyResourceVO.getBusinessGroupId(),
                applyResourceVO.getBusinessGroupName(),
                applyResourceVO.getBusinessGroupName(),
                applyResourceVO.getState(), businessDetail, applyResourceVO.getBusinessActivitiName()));

        //??????id???????????????????????????
        if (jsonObjectResponse != null && jsonObjectResponse.getData() != null && StringUtils.isNotBlank(jsonObjectResponse.getData().getString("processInstanceId"))) {
            IaasApplyResource applyResource = new IaasApplyResource();
            applyResource.setId(applyId);
            applyResource.setState(applyResourceVO.getState());
            applyResource.setUpdateTime(DateUtil.getNow());
            applyResource.setUpdateUser("0".equals(operationType) ? Sign.getUserId() : 2777994821132290L);
            if (isGroup) {//??????????????????
                applyResource.setGroupProcessInstanceId(jsonObjectResponse.getData().getString("processInstanceId"));
            } else {
                applyResource.setProcessInstanceId(jsonObjectResponse.getData().getString("processInstanceId"));
            }
            iaasResourceApplyMapper.updateById(applyResource);

            baseResponse.setCode(0);
        } else {
            baseResponse.setCode(201);
            baseResponse.setMessage("?????????????????????");
        }
        return baseResponse;
    }

    //??????????????????
    private void handleFile(Long applyId, IaasApplyResourceVO iaasApplyResourceVO) {
        //?????????????????????
//        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(SysFile::getBusinessId, applyId);
//        sysFileMapper.delete(queryWrapper);

        List<FileVo> fileIds = iaasApplyResourceVO.getFileVos();
        if (CollectionUtils.isNotEmpty(fileIds)) {
            for (FileVo vo : fileIds) {
                SysFile sysFile = new SysFile();
                sysFile.setId(vo.getFileId());
                sysFile.setBusinessId(applyId);
                sysFile.setBusinessType(vo.getType());
                sysFileMapper.updateById(sysFile);
            }
        }
    }

    //?????????????????????
    private void updateConfig(IaasApplyRequest vo) {
        IaasApplyConfigInfoVO applyConfigInfoVO = vo.getIaasApplyConfigInfoVO();
        //??????????????????
        IaasApplyConfigInfo applyConfigInfo = new IaasApplyConfigInfo();
        BeanUtils.copyProperties(applyConfigInfoVO, applyConfigInfo);
        applyConfigInfo.setUpdateTime(DateUtil.getNow());
        applyConfigInfo.setUpdateUser(Sign.getUserId());
        iaasApplyConfigInfoMapper.updateById(applyConfigInfo);

        //????????????????????????
        IaasApplyServiceSecurityResourcesVO securityResourcesVO = vo.getIaasApplyServiceSecurityResourcesVO();
        if (securityResourcesVO != null) {
            IaasApplyServiceSecurityResources securityResources = new IaasApplyServiceSecurityResources();
            BeanUtils.copyProperties(securityResourcesVO, securityResources);
            securityResources.setUpdateTime(DateUtil.getNow());
            securityResources.setUpdateUser(Sign.getUserId());
            serviceSecurityResourcesMapper.updateById(securityResources);
        }

        //??????????????????
        List<IaasApplyNetworkAskVO> iaasApplyNetworkAskVOs = vo.getIaasApplyNetworkAskVO();
        if (CollectionUtils.isNotEmpty(iaasApplyNetworkAskVOs)) {
            List<Long> oldDiskIds = iaasApplyNetworkAskMapper.queryNetworkByConfigId(applyConfigInfoVO.getId());
            List<Long> newNetworkIds = new ArrayList<>();
            for (IaasApplyNetworkAskVO iaasApplyNetworkAskVO : iaasApplyNetworkAskVOs) {
                IaasApplyNetworkAsk query = iaasApplyNetworkAskMapper.selectById(iaasApplyNetworkAskVO.getId());
                if (query != null) {//???????????????
                    newNetworkIds.add(iaasApplyNetworkAskVO.getId());
                    IaasApplyNetworkAsk networkAsk = new IaasApplyNetworkAsk();
                    BeanUtils.copyProperties(iaasApplyNetworkAskVO, networkAsk);
                    networkAsk.setUpdateTime(DateUtil.getNow());
                    networkAsk.setUpdateUser(Sign.getUserId());
                    iaasApplyNetworkAskMapper.updateById(networkAsk);
                } else {//???????????????
                    IaasApplyNetworkAsk networkAsk = new IaasApplyNetworkAsk();
                    BeanUtils.copyProperties(iaasApplyNetworkAskVO, networkAsk);
                    networkAsk.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    networkAsk.setConfigId(applyConfigInfoVO.getId());
                    networkAsk.setCreateTime(DateUtil.getNow());
                    networkAsk.setCreateUser(Sign.getUserId());
                    networkAsk.setDeleted(false);

                    iaasApplyNetworkAskMapper.insert(networkAsk);
                }
            }

            if (CollectionUtils.isNotEmpty(oldDiskIds)) {
                //???????????????
                oldDiskIds.removeAll(newNetworkIds);

                for (Long diskId : oldDiskIds) {
                    IaasApplyNetworkAsk networkAsk = new IaasApplyNetworkAsk();
                    networkAsk.setId(diskId);
                    networkAsk.setDeleted(true);
                    iaasApplyNetworkAskMapper.updateById(networkAsk);
                }
            }
        }

        //?????????????????????????????????????????????????????????????????????
        List<IaasApplyMemoryDiskVO> memoryDiskVOList = applyConfigInfoVO.getMemoryDiskVOList();
        if (CollectionUtils.isNotEmpty(memoryDiskVOList)) {
            List<Long> oldDiskIds = iaasApplyMemoryDiskMapper.queryDiskByConfigId(applyConfigInfoVO.getId());
            List<Long> newDiskIds = new ArrayList<>();
            for (IaasApplyMemoryDiskVO diskVO : memoryDiskVOList) {
                IaasApplyMemoryDisk disk = new IaasApplyMemoryDisk();
                BeanUtils.copyProperties(diskVO, disk);
                IaasApplyMemoryDisk memoryDisk = iaasApplyMemoryDiskMapper.selectById(diskVO.getId());
                if (memoryDisk != null) {//??????????????????
                    newDiskIds.add(diskVO.getId());
                    disk.setUpdateTime(DateUtil.getNow());
                    disk.setUpdateUser(Sign.getUserId());
                    iaasApplyMemoryDiskMapper.updateById(disk);
                } else {//???????????????
                    disk.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    disk.setConfigId(applyConfigInfoVO.getId());
                    disk.setCreateTime(DateUtil.getNow());
                    disk.setCreateUser(Sign.getUserId());
                    disk.setDeleted(false);
                    iaasApplyMemoryDiskMapper.insert(disk);
                }
            }

            if (CollectionUtils.isNotEmpty(oldDiskIds)) {
                //???????????????
                oldDiskIds.removeAll(newDiskIds);

                for (Long diskId : oldDiskIds) {
                    IaasApplyMemoryDisk memoryDisk = new IaasApplyMemoryDisk();
                    memoryDisk.setId(diskId);
                    memoryDisk.setDeleted(true);
                    iaasApplyMemoryDiskMapper.updateById(memoryDisk);
                }
            }
        }

        //??????????????????????????????????????????????????????????????????????????????
        List<IaasApplyPortMappingAskVO> portMappingAskVOList = applyConfigInfoVO.getPortMappingAskVOS();
        if (CollectionUtils.isNotEmpty(portMappingAskVOList)) {
            List<Long> oldMappingIds = iaasApplyPortMappingAskMapper.queryPortMappingByConfigId(applyConfigInfoVO.getId());
            List<Long> newMappingIds = new ArrayList<>();
            for (IaasApplyPortMappingAskVO mappingAskVO : portMappingAskVOList) {
                IaasApplyPortMappingAsk mapping = new IaasApplyPortMappingAsk();
                BeanUtils.copyProperties(mappingAskVO, mapping);
                IaasApplyPortMappingAsk mappingAsk = iaasApplyPortMappingAskMapper.selectById(mappingAskVO.getId());
                if (mappingAsk != null) {//??????????????????
                    newMappingIds.add(mappingAskVO.getId());
                    mapping.setUpdateTime(DateUtil.getNow());
                    mapping.setUpdateUser(Sign.getUserId());
                    iaasApplyPortMappingAskMapper.updateById(mapping);
                } else {//???????????????
                    mapping.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    mapping.setConfigId(applyConfigInfoVO.getId());
                    mapping.setCreateTime(DateUtil.getNow());
                    mapping.setCreateUser(Sign.getUserId());
                    mapping.setDeleted(false);
                    iaasApplyPortMappingAskMapper.insert(mapping);
                }
            }

            if (CollectionUtils.isNotEmpty(oldMappingIds)) {
                //???????????????
                oldMappingIds.removeAll(newMappingIds);

                for (Long diskId : oldMappingIds) {
                    IaasApplyPortMappingAsk memoryDisk = new IaasApplyPortMappingAsk();
                    memoryDisk.setId(diskId);
                    memoryDisk.setDeleted(true);
                    iaasApplyPortMappingAskMapper.updateById(memoryDisk);
                }
            }
        }
    }

    //??????????????????????????????????????????
    private void deleteApplyConfigInfo(IaasApplyConfigInfoVO configInfoVO) {
        IaasApplyConfigInfo configInfo = new IaasApplyConfigInfo();
        configInfo.setId(configInfoVO.getId());
        configInfo.setDeleted(true);
        iaasApplyConfigInfoMapper.updateById(configInfo);

        List<IaasApplyNetworkAskVO> iaasApplyNetworkAskVOs = configInfoVO.getIaasApplyNetworkAskVO();
        if (CollectionUtils.isNotEmpty(iaasApplyNetworkAskVOs)) {
            for (IaasApplyNetworkAskVO iaasApplyNetworkAskVO : iaasApplyNetworkAskVOs) {
                IaasApplyNetworkAsk networkAsk = new IaasApplyNetworkAsk();
                networkAsk.setId(iaasApplyNetworkAskVO.getId());
                networkAsk.setDeleted(true);
                iaasApplyNetworkAskMapper.updateById(networkAsk);
            }
        }

        IaasApplyServiceSecurityResourcesVO securityResourcesVO = configInfoVO.getIaasApplyServiceSecurityResourcesVO();
        if (securityResourcesVO != null) {
            IaasApplyServiceSecurityResources securityResources = new IaasApplyServiceSecurityResources();
            securityResources.setId(securityResourcesVO.getId());
            securityResources.setDeleted(true);
            serviceSecurityResourcesMapper.updateById(securityResources);
        }

        List<IaasApplyMemoryDiskVO> memoryDiskVOList = configInfoVO.getMemoryDiskVOList();
        if (CollectionUtils.isNotEmpty(memoryDiskVOList)) {
            for (IaasApplyMemoryDiskVO memoryDiskVO : memoryDiskVOList) {
                IaasApplyMemoryDisk memoryDisk = new IaasApplyMemoryDisk();
                memoryDisk.setId(memoryDiskVO.getId());
                memoryDisk.setDeleted(true);
                iaasApplyMemoryDiskMapper.updateById(memoryDisk);
            }
        }

        List<IaasApplyPortMappingAskVO> portMappingAskVOS = configInfoVO.getPortMappingAskVOS();
        if (CollectionUtils.isNotEmpty(memoryDiskVOList)) {
            for (IaasApplyPortMappingAskVO portMappingAskVO : portMappingAskVOS) {
                IaasApplyPortMappingAsk portMapping = new IaasApplyPortMappingAsk();
                portMapping.setId(portMappingAskVO.getId());
                portMapping.setDeleted(true);
                iaasApplyPortMappingAskMapper.updateById(portMapping);
            }
        }
    }

    //??????businessDetail
    private String businessDetail(String operationType, IaasApplyResourceVO vo) {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", vo.getId());
        map.put("operationType", operationType);
        map.put("businessGroupId", vo.getBusinessGroupId());
        map.put("businessGroupName", vo.getBusinessGroupName());
        map.put("businessActivitiId", vo.getBusinessActivitiId());
        map.put("businessActivitiName", vo.getBusinessActivitiName());
        map.put("state", vo.getState());
        map.put("lease", vo.getLease());
        map.put("projectId", vo.getProjectId());
        map.put("projectName", vo.getProjectName());
        map.put("vdcId", vo.getVdcId());
        map.put("vdcName", vo.getVdcName());
        map.put("createTime", DateUtil.getNow());
        map.put("remark", vo.getRemark());
        if ("0".equals(operationType)) {
            map.put("createUser", vo.getCreateUser() == null ? Sign.getUserId() : vo.getCreateUser());
            map.put("createUserName", StringUtils.isBlank(vo.getCreateUserName()) ? Sign.getUserDisplayName() : vo.getCreateUserName());
        } else {
            map.put("createUser", vo.getCreateUser());
            UserResponse userResponse = userClient.queryById(vo.getCreateUser());
            map.put("createUserName", userResponse.getData().getDisplayName());
        }

        return JSONObject.toJSONString(map);
    }

    //??????????????????
    private BaseResponse completeActiviti(String processInstanceId, String businessDetail, BaseResponse baseResponse) {
        //????????????taskId,???????????????????????????
        ActTaskPageResponse actTaskPageResponse = activitiClient.queryUserTask(1, 1, Sign.getUserId(), processInstanceId, null, null, false);

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
        completeTask.setComment("??????????????????");

        Map<String, Object> params = new HashMap<>();
//        params.put("businessGroupId", processApplyVO.getBusinessGroupId());

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

    //????????????????????????
    private ProcessDTO assemblerProcessDTO(long processApplyId,
                                           Long createUser,
                                           Long businessActivitiId,
                                           Long businessGroupId,
                                           String businessGroupName,
                                           String processName,
                                           int state,
                                           String businessDetail,
                                           String businessActivitiName) {
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setApplyFlag(state);//???????????????(0:???????????????;1:????????????;)
        processDTO.setBusinessId(processApplyId);//??????id
        processDTO.setProcessWorkflowId(businessActivitiId);//???????????????id
        processDTO.setBusinessDetail(businessDetail);//????????????
        processDTO.setComment("??????????????????");//????????????????????????
        processDTO.setProcessOperation(businessActivitiName);//?????????????????????,???????????????????????????????????????????????? ???:apply
        processDTO.setBusinessGroupId(businessGroupId);
        processDTO.setBusinessGroupName(businessGroupName);
        processDTO.setProcessName(processName);
        if (createUser != null){
            processDTO.setUserId(createUser);
        }
//        processDTO.setProcessDefinitionKey();//???????????????
//        processDTO.setNotifyMessage();//???????????????,?????????????????????????????????
//        processDTO.setNotifyDetail();//???????????????,?????????????????????????????????
//        processDTO.setOutcome();//????????????????????????
//        processDTO.setProcessObject();//?????????????????????, ???:process
        Map<String, Object> params = new HashMap<>();
        params.put("workorderId", processApplyId);
        processDTO.setParams(params);
        return processDTO;
    }

    //??????????????????
    public void handleApply(int index,
                            Long applyId,
                            String operationType,
                            IaasApplyConfigInfoVO configInfoVO,
                            IaasApplyServiceSecurityResourcesVO securityResourcesVO,
                            List<IaasApplyNetworkAskVO> networkAskVOs) {

        long configId = SnowFlakeIdGenerator.getInstance().nextId();
        //1.??????????????????
        saveConfigInfo(index, applyId, configId, operationType, configInfoVO);

        //2.????????????????????????
        saveSecurityResources(configId, operationType, securityResourcesVO);

        //3.??????????????????
        saveNetworkAsk(configId, operationType, networkAskVOs);
    }

    //??????????????????
    private void saveNetworkAsk(long configId, String operationType, List<IaasApplyNetworkAskVO> networkAskVOs) {
        if (CollectionUtils.isEmpty(networkAskVOs)) {
            return;
        }

        for (IaasApplyNetworkAskVO networkAskVO : networkAskVOs) {
            IaasApplyNetworkAsk networkAsk = new IaasApplyNetworkAsk();
            BeanUtils.copyProperties(networkAskVO, networkAsk);
            networkAsk.setId(SnowFlakeIdGenerator.getInstance().nextId());
            networkAsk.setConfigId(configId);
            networkAsk.setCreateTime(DateUtil.getNow());
            networkAsk.setCreateUser("0".equals(operationType) ? Sign.getUserId() : networkAskVO.getCreateUser());
            networkAsk.setDeleted(false);

            iaasApplyNetworkAskMapper.insert(networkAsk);
        }
    }

    //????????????????????????
    private void saveSecurityResources(long configId, String operationType, IaasApplyServiceSecurityResourcesVO securityResourcesVO) {
        IaasApplyServiceSecurityResources securityResources = new IaasApplyServiceSecurityResources();
        BeanUtils.copyProperties(securityResourcesVO, securityResources);
        securityResources.setId(SnowFlakeIdGenerator.getInstance().nextId());
        securityResources.setConfigId(configId);
        securityResources.setCreateTime(DateUtil.getNow());
        securityResources.setCreateUser("0".equals(operationType) ? Sign.getUserId() : securityResourcesVO.getCreateUser());
        securityResources.setDeleted(false);

        serviceSecurityResourcesMapper.insert(securityResources);
        logger.info("???????????????????????????");
    }

    //?????????-??????????????????
    private void saveConfigInfo(int index, long applyId, long configId, String operationType, IaasApplyConfigInfoVO applyConfigInfoVO) {
        IaasApplyConfigInfo saveConfig = new IaasApplyConfigInfo();
        BeanUtils.copyProperties(applyConfigInfoVO, saveConfig);
        //?????????????????????
        if (index > 0) {
            saveConfig.setServerName(applyConfigInfoVO.getServerName() + String.valueOf(index));
        }
        //todo ??????????????????????????????
        saveConfig.setId(configId);
        saveConfig.setApplyId(applyId);
        saveConfig.setCreateTime(DateUtil.getNow());
        saveConfig.setCreateUser("0".equals(operationType) ? Sign.getUserId() : applyConfigInfoVO.getCreateUser());
        saveConfig.setDeleted(false);
        saveConfig.setPrefixName(applyConfigInfoVO.getServerName().split("_0")[0]);

        iaasApplyConfigInfoMapper.insert(saveConfig);
        logger.info("???????????????????????????");

        //???????????????
        handleMemoryDisk(configId, operationType, applyConfigInfoVO.getMemoryDiskVOList());

        //????????????????????????
        handleMapping(configId, operationType, applyConfigInfoVO.getPortMappingAskVOS());
    }

    private void handleMapping(long configId, String operationType, List<IaasApplyPortMappingAskVO> portMappingAskVOS) {
        if (CollectionUtils.isEmpty(portMappingAskVOS)) {
            logger.warn("???????????????????????????");
            return;
        }

        for (IaasApplyPortMappingAskVO vo : portMappingAskVOS) {
            saveMapping(configId, operationType, vo);
        }
    }

    private void saveMapping(long configId, String operationType, IaasApplyPortMappingAskVO vo) {
        IaasApplyPortMappingAsk mapping = new IaasApplyPortMappingAsk();
        BeanUtils.copyProperties(vo, mapping);
        mapping.setId(SnowFlakeIdGenerator.getInstance().nextId());
        mapping.setConfigId(configId);
        mapping.setCreateTime(DateUtil.getNow());
        mapping.setCreateUser("0".equals(operationType) ? Sign.getUserId() : SYS_USER);
        mapping.setDeleted(false);

        iaasApplyPortMappingAskMapper.insert(mapping);
        logger.info("???????????????????????????");
    }

    //???????????????
    private void handleMemoryDisk(long configId, String operationType, List<IaasApplyMemoryDiskVO> memoryDiskVOList) {
        if (CollectionUtils.isEmpty(memoryDiskVOList)) {
            logger.warn("????????????????????????");
            return;
        }

        for (IaasApplyMemoryDiskVO vo : memoryDiskVOList) {
            saveMemoryDisk(configId, operationType, vo);
        }
    }

    //???????????????
    private void saveMemoryDisk(long configId, String operationType, IaasApplyMemoryDiskVO vo) {
        IaasApplyMemoryDisk memoryDisk = new IaasApplyMemoryDisk();
        BeanUtils.copyProperties(vo, memoryDisk);
        memoryDisk.setId(SnowFlakeIdGenerator.getInstance().nextId());
        memoryDisk.setConfigId(configId);
        memoryDisk.setCreateTime(DateUtil.getNow());
        memoryDisk.setCreateUser("0".equals(operationType) ? Sign.getUserId() : SYS_USER);
        memoryDisk.setDeleted(false);

        iaasApplyMemoryDiskMapper.insert(memoryDisk);
        logger.info("????????????????????????");
    }

    //??????????????????
    private void handleResource(Long applyId, String operationType, IaasApplyResourceVO applyResourceVO, IaasApplyResource queryApplyInfo) {
        if (queryApplyInfo == null) {
            IaasApplyResource applyResource = new IaasApplyResource();
            BeanUtils.copyProperties(applyResourceVO, applyResource);
            applyResource.setTenantId(null);
            applyResource.setId(applyId);
            applyResource.setCreateTime(DateUtil.getNow());
            applyResource.setCreateUser("0".equals(operationType) ? Sign.getUserId() : applyResourceVO.getCreateUser());
            applyResource.setOperationType(operationType);
            applyResource.setDeleted(false);

            iaasResourceApplyMapper.insert(applyResource);
            logger.info("???????????????????????????");
        } else {
            BeanUtils.copyProperties(applyResourceVO, queryApplyInfo);
            queryApplyInfo.setUpdateTime(DateUtil.getNow());
            queryApplyInfo.setUpdateUser(Sign.getUserId());
            iaasResourceApplyMapper.updateById(queryApplyInfo);
        }
    }

    //??????????????????
    private void handleResource1(Long applyId, String operationType, IaasApplyResourceVO applyResourceVO, IaasApplyResource queryApplyInfo) {
        if (queryApplyInfo == null) {
            IaasApplyResource applyResource = new IaasApplyResource();
            BeanUtils.copyProperties(applyResourceVO, applyResource);
            applyResource.setTenantId(null);
            applyResource.setId(applyId);
            applyResource.setCreateTime(DateUtil.getNow());
            applyResource.setCreateUser("0".equals(operationType) ? Sign.getUserId() : SYS_USER);
            applyResource.setOperationType(operationType);
            applyResource.setDeleted(false);

            iaasResourceApplyMapper.insert(applyResource);
            logger.info("???????????????????????????");
        } else {
            BeanUtils.copyProperties(applyResourceVO, queryApplyInfo);
            queryApplyInfo.setUpdateTime(DateUtil.getNow());
            queryApplyInfo.setUpdateUser(SYS_USER);
            iaasResourceApplyMapper.updateById(queryApplyInfo);
        }
    }

    /**
     * pdf???base64
     * @param file
     * @return
     */
    public String PDFToBase(File file){
        FileInputStream fin=null;
        BufferedInputStream bin=null;
        ByteArrayOutputStream baos=null;
        BufferedOutputStream bout=null;
        try{
            fin=new FileInputStream(file);
            bin=new BufferedInputStream(fin);
            baos=new ByteArrayOutputStream();
            bout=new BufferedOutputStream(baos);
            byte[] buffer=new byte[1024];
            int len=bin.read(buffer);
            while(len!=-1){
                bout.write(buffer,0,len);
                len=bin.read(buffer);
            }
            //????????????
            bout.flush();
            byte[] bytes=baos.toByteArray();
            String res = Base64.toBase64String(bytes);
            return res;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }finally {
            try{
                fin.close();
                bin.close();
                bout.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}

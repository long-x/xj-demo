package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.activiti.client.ActivitiClient;
import com.ecdata.cmp.activiti.client.WebServiceClient;
import com.ecdata.cmp.activiti.client.WorkflowTaskClient;
import com.ecdata.cmp.activiti.dto.request.CompleteTaskRequest;
import com.ecdata.cmp.activiti.dto.response.WorkflowTaskListResponse;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskCandidateVO;
import com.ecdata.cmp.activiti.dto.vo.webservice.*;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.BaremetalServersFlavorsClient;
import com.ecdata.cmp.huawei.client.HostVolumesClient;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.client.VDCVirtualMachineClient;
import com.ecdata.cmp.huawei.dto.response.BaremetalListResponse;
import com.ecdata.cmp.huawei.dto.response.HostVolumesVOListResponse;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.response.VDCVirtualMachineListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenInfoVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.*;
import com.ecdata.cmp.iaas.entity.apply.*;
import com.ecdata.cmp.iaas.entity.dto.ApplyInfoVO;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.apply.ApplyAreaUpdateVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.SaveDataVO;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import com.ecdata.cmp.iaas.mapper.*;
import com.ecdata.cmp.iaas.mapper.apply.*;
import com.ecdata.cmp.iaas.mapper.file.SysFileMapper;
import com.ecdata.cmp.iaas.service.IApplyResourceService;
import com.ecdata.cmp.iaas.service.IApplyService;
import com.ecdata.cmp.iaas.service.IProjectService;
import com.ecdata.cmp.iaas.service.IaasBareMetalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * ????????????
 *
 * @author xuxiaojian
 * @date 2020/2/27 14:20
 */
@Slf4j
@Service
public class IApplyServiceImpl implements IApplyService {
    @Value("${pdf.download-url}")
    private String downloadUrl;

    @Autowired
    private ActivitiClient activitiClient;

    @Autowired
    private IaasApplyResourceMapper iaasResourceApplyMapper;

    @Autowired
    private IaasProviderMapper iaasProviderMapper;

    @Autowired
    private ProviderServiceImpl providerService;

    @Autowired
    private IaasVirtualMachineMapper iaasVirtualMachineMapper;

    @Autowired
    private IaasVirtualMachineDiskMapper virtualMachineDiskMapper;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IaasProjectMapper iaasProjectMapper;

    @Autowired
    private OauthTokenClient oauthTokenClient;

    @Autowired
    private VDCVirtualMachineClient vdcVirtualMachineClient;

    @Autowired
    private IaasAreaMapper iaasAreaMapper;

    @Autowired
    private IaasVirtualDataCenterMapper iaasVirtualDataCenterMapper;

    @Autowired
    private HostVolumesClient hostVolumesClient;

    @Autowired
    private IaasApplyConfigInfoMapper iaasApplyConfigInfoMapper;

    @Autowired
    private IaasResourcePoolMapper iaasResourcePoolMapper;

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private IaasBareMetalService iaasBareMetalService;

    @Autowired
    private WebServiceClient webServiceClient;

    @Autowired
    private WorkflowTaskClient workflowTaskClient;

    @Autowired
    private BaremetalServersFlavorsClient baremetalServersFlavorsClient;

    @Autowired
    private IApplyResourceService applyResourceService;

    @Autowired
    private IaasApplyResourceMapper iaasApplyResourceMapper;

    @Autowired
    private IaasApplyStorageMapper iaasApplyStorageMapper;

    @Autowired
    private IaasApplyRelationInfoMapper iaasApplyRelationInfoMapper;

    @Autowired
    private IaasApplyCalculateMapper iaasApplyCalculateMapper;

    @Autowired
    private IaasApplyOtherMapper iaasApplyOtherMapper;

    @Autowired
    private IaasApplyMemoryDiskMapper iaasApplyMemoryDiskMapper;


    @Override
    public BaseResponse apply(ApplyInfoVO vo) {
        BaseResponse baseResponse = new BaseResponse();

        //?????????????????????????????????
        List<ApplyAreaUpdateVO> areaVOList = vo.getAreaVOList();
        if (CollectionUtils.isNotEmpty(areaVOList)) {
            for (ApplyAreaUpdateVO areaVO : areaVOList) {
                IaasApplyConfigInfo configInfo = new IaasApplyConfigInfo();
                configInfo.setId(areaVO.getConfigId());
                configInfo.setAreaId(areaVO.getAreaId());
                if (areaVO.getAutoIssue() != null) {
                    configInfo.setAutoIssue(areaVO.getAutoIssue());
                } else {
                    configInfo.setAutoIssue("0");
                }
                configInfo.setEipId(areaVO.getEipId());
                configInfo.setIpAddress(areaVO.getIpAddress());
                iaasApplyConfigInfoMapper.updateById(configInfo);
            }
        }

        //???????????????????????????
        handleFile(vo.applyId(), vo.getApplyFileVos());

        CompleteTaskRequest completeTask = new CompleteTaskRequest();
        completeTask.setTaskId(vo.getTaskId());
        completeTask.setOutcome(vo.getOutcome());
        completeTask.setComment(vo.getComment());
        completeTask.setUserId(vo.getUserId());
        completeTask.setUserDisplayName(vo.getUserDisplayName());

        // ??????????????????
        Integer processWorkflowStep = vo.getProcessWorkflowStep();
        log.info("processWorkflowStep: " + processWorkflowStep);
        log.info("Outcome: " + vo.getOutcome());
        if (StringUtils.equalsIgnoreCase(vo.getOutcome(), "??????") && processWorkflowStep != null && processWorkflowStep == 7) {
            String message = applyResourceService.queryApplyResourceByProcessInstanceId(vo.getProcessInstanceId());
            log.info(message);
        }

//        //??????????????????????????????????????????????????????
//        if(processWorkflowStep ==2 ){
//            //1.??????applyId(????????????)
//            IaasApplyResourceVO applyResourceVO = vo.getApplyResourceVO();
//            //2.??????configId(?????????/?????????/???????????????id)
//            List<ApplyAreaUpdateVO> areaVOList1 = vo.getAreaVOList();
//            //3.??????????????????id(activiti??????id)
//            this.saveData(applyResourceVO.getId(),areaVOList1,vo.getProcessInstanceId());
//        }

        //????????????????????????????????????,???????????????????????????????????????????????????
        JSONObjectResponse jsonObjectResponse = activitiClient.completeTask(AuthContext.getAuthz(), completeTask);

        if (jsonObjectResponse == null || jsonObjectResponse.getCode() != 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????");
            return baseResponse;
        }

        //??????????????????oa
        if ("2".equals(vo.getSubmitOa()) && "??????".equals(vo.getOutcome())) {
            //??????oa???????????? todo email
            String resUserId = webServiceClient.getUserId("email", "szghygaoyang@chengtou.com");
            String userId = resUserId.replace("\"", "");

            if (StringUtils.isBlank(userId)) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("??????oa??????id?????????");
                log.info("??????oa??????id?????????", resUserId);
                return baseResponse;
            }

            QueryWrapper<IaasApplyResource> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(IaasApplyResource::getProcessInstanceId, vo.getProcessInstanceId());
            IaasApplyResource applyResource = iaasResourceApplyMapper.selectOne(queryWrapper);

            String pdfUrl = generatePdf(vo.applyId(), vo.getBase64Pdf());

            String pdf = vo.getPdfUrl() + ".pdf," + pdfUrl + ".pdf";
            String pdfName = getPdfName(vo.getPdfUrl()) + "|" + getPdfName(pdfUrl);
            String pdf2 = vo.getPdfUrl() + ".pdf|" + pdfUrl + ".pdf";
            String resGroupProcessInstanceId = null;
            //?????????????????????????????????
            if (StringUtils.isBlank(applyResource.getGroupProcessInstanceId())) {
                WorkflowRequestInfoVO workflowRequestInfoVO = assemblerWorkflowRequestInfoVO(userId, pdfName, pdf2, "??????-??????????????????", null, null, null);
                log.info("??????oa????????????????????????" + workflowRequestInfoVO.toString());
                resGroupProcessInstanceId = webServiceClient.doCreateWorkflowRequest(workflowRequestInfoVO);
                log.info("??????oa??????????????????????????????", resGroupProcessInstanceId);
                //??????????????????
            } else {
                WorkflowRequestInfoVO workflowRequestInfoVO = assemblerWorkflowRequestInfoVO(userId, pdfName, pdf2, "??????-??????????????????(??????)", Integer.valueOf(applyResource.getGroupProcessInstanceId()), "subback", "????????????");

                resGroupProcessInstanceId = webServiceClient.submitWorkflowRequest(workflowRequestInfoVO);
                log.info("??????oa???????????????????????????2???", workflowRequestInfoVO.toString());
                log.info("??????oa???????????????????????????2???", resGroupProcessInstanceId);
            }

            if (StringUtils.isBlank(resGroupProcessInstanceId)) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("??????oa???????????????");
                return baseResponse;
            }
            String groupProcessInstanceId = resGroupProcessInstanceId.replace("\"", "");

            IaasApplyResource resource = new IaasApplyResource();
            resource.setId(vo.applyId());
            resource.setGroupProcessInstanceId(groupProcessInstanceId);
            resource.setPdfUrl(pdf);
            resource.setUpdateUser(Sign.getUserId());
            resource.setUpdateTime(DateUtil.getNow());

            iaasResourceApplyMapper.updateById(resource);
        }

        if ("??????".equals(vo.getOutcome())) {
            IaasApplyResource resource = new IaasApplyResource();
            resource.setId(vo.applyId());
            resource.setState(2);
            resource.setUpdateUser(Sign.getUserId());
            resource.setUpdateTime(DateUtil.getNow());

            iaasResourceApplyMapper.updateById(resource);

            if ("1".equals(vo.getType())) {
                //???????????????????????????
                nanotubeResource(vo);
            } else {
                //????????????
                deleteNanotubeResource(vo);
            }
        }

        if ("??????".equals(vo.getOutcome())) {
            IaasApplyResource resource = new IaasApplyResource();
            resource.setId(vo.applyId());
            resource.setState(0);
            resource.setUpdateUser(vo.getUserId() == null ? Sign.getUserId() : vo.getUserId());
            resource.setUpdateTime(DateUtil.getNow());

            iaasResourceApplyMapper.updateById(resource);
        }
        return baseResponse;
    }

    private String getPdfName(String url) {
        String[] split = url.split("download/");

        return "http:" + split[1] + ".pdf;";
    }

    private boolean checkAuditResult(String processInstanceId, Integer processWorkflowStep) {
        WorkflowTaskListResponse response = workflowTaskClient.list(null, processWorkflowStep, processInstanceId, null, null, 1);
        if (response == null || response.getCode() != 0 || CollectionUtils.isEmpty(response.getData())) {
            return false;
        }

        List<WorkflowTaskCandidateVO> candidateList = response.getData().get(0).getCandidateList();

        if (CollectionUtils.isEmpty(candidateList)) {
            return false;
        }
        //???????????????????????????
        List<WorkflowTaskCandidateVO> isApproved = candidateList.stream().filter(e -> "0".equals(e.getIsApproved())).collect(Collectors.toList());

        //?????????????????????????????????
        List<WorkflowTaskCandidateVO> outcome = candidateList.stream().filter(e -> !"??????".equals(e.getOutcome())).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(isApproved) || CollectionUtils.isNotEmpty(outcome)) {
            return false;
        }

        return true;
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

    //??????????????????
    private void handleFile(Long applyId, List<FileVo> fileVos) {
        if (CollectionUtils.isNotEmpty(fileVos)) {
            for (FileVo vo : fileVos) {
                SysFile sysFile = new SysFile();
                sysFile.setId(vo.getFileId());
                sysFile.setBusinessId(applyId);
                sysFile.setBusinessType(vo.getType());
                sysFileMapper.updateById(sysFile);
            }
        }
    }

    private WorkflowRequestInfoVO assemblerWorkflowRequestInfoVO(String userId,
                                                                 String pdfName,
                                                                 String pdfUrl,
                                                                 String zysx,
                                                                 Integer requestId,
                                                                 String type,
                                                                 String remark) {
        WorkflowRequestTableFieldVO tableField1 = new WorkflowRequestTableFieldVO();
        tableField1.setFieldName("sqr");
        tableField1.setFieldValue(Sign.getUserDisplayName());
        tableField1.setEdit(true);
        tableField1.setView(true);

        WorkflowRequestTableFieldVO tableField2 = new WorkflowRequestTableFieldVO();
        tableField2.setFieldName("sqbm");
        tableField2.setFieldValue("???????????????");
        tableField2.setEdit(true);
        tableField2.setView(true);

        WorkflowRequestTableFieldVO tableField6 = new WorkflowRequestTableFieldVO();
        tableField6.setFieldName("sqrq");
        tableField6.setFieldValue(DateUtil.getNowStr("yyyy-MM-dd"));
        tableField6.setEdit(true);
        tableField6.setView(true);

        WorkflowRequestTableFieldVO tableField3 = new WorkflowRequestTableFieldVO();
        tableField3.setFieldName("zysx");
        tableField3.setFieldValue(zysx);
        tableField3.setEdit(true);
        tableField3.setView(true);

        WorkflowRequestTableFieldVO tableField4 = new WorkflowRequestTableFieldVO();
        tableField4.setFieldName("sqpdf");
        tableField4.setFieldType(pdfName);
        tableField4.setFieldValue(pdfUrl);
        tableField4.setEdit(true);
        tableField4.setView(true);

        WorkflowRequestTableFieldVO tableField5 = new WorkflowRequestTableFieldVO();
        tableField5.setFieldName("qtfj");
        tableField5.setFieldType("http:");
        tableField5.setFieldValue("sss");
        tableField5.setEdit(true);
        tableField5.setView(true);

        WorkflowRequestTableFieldVO[] workflowRequestTableFields = {tableField1, tableField2, tableField6, tableField3, tableField4, tableField5};

        WorkflowRequestTableRecordVO tableRecord = new WorkflowRequestTableRecordVO();
        tableRecord.setWorkflowRequestTableFields(workflowRequestTableFields);

        WorkflowRequestTableRecordVO[] requestRecords = {tableRecord};

        WorkflowMainTableInfoVO mainTableInfo = new WorkflowMainTableInfoVO();
        mainTableInfo.setRequestRecords(requestRecords);

        WorkflowRequestInfoVO workflowRequestInfo = new WorkflowRequestInfoVO();
        workflowRequestInfo.setWorkflowMainTableInfo(mainTableInfo);
        workflowRequestInfo.setCreatorId(userId);
        workflowRequestInfo.setRequestLevel("0");
        workflowRequestInfo.setRequestName("???????????????");
        workflowRequestInfo.setUserId(Integer.valueOf(userId));

        //???????????????????????????
        if (null != requestId) {
            workflowRequestInfo.setRequestId(requestId);
        }

        if (StringUtils.isNotBlank(type)) {
            workflowRequestInfo.setType(type);
        }

        if (StringUtils.isNotBlank(remark)) {
            workflowRequestInfo.setRemark(remark);
        }

        WorkflowBaseInfoVO workflowBaseInfo = new WorkflowBaseInfoVO();
//        workflowBaseInfo.setWorkflowId("54008");
        workflowBaseInfo.setWorkflowId("63006");
        workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);


        return workflowRequestInfo;
    }

    //????????????
    private void deleteNanotubeResource(ApplyInfoVO vo) {
        IaasApplyResourceVO applyResourceVO = vo.getApplyResourceVO();
        //???????????????
        List<IaasApplyConfigInfoVO> configInfoVOList = iaasApplyConfigInfoMapper.queryApplyConfigInfoList(applyResourceVO.getId(), null);

        if (CollectionUtils.isEmpty(configInfoVOList)) {
            return;
        }

        for (IaasApplyConfigInfoVO configInfoVO : configInfoVOList) {
            if (!"3".equals(configInfoVO.getApplyType())) {
                QueryWrapper<IaasVirtualMachine> wrapper = new QueryWrapper<>();

                IaasVirtualMachine iaasVirtualDataCenter = iaasVirtualMachineMapper.selectOne(wrapper.lambda()
                        .eq(IaasVirtualMachine::getVmName, configInfoVO.getServerName())
                        .eq(IaasVirtualMachine::isDeleted, false));

                if (iaasVirtualDataCenter != null) {
                    iaasVirtualDataCenter.setDeleted(true);
                    iaasVirtualMachineMapper.updateById(iaasVirtualDataCenter);

                    QueryWrapper<IaasVirtualMachineDisk> wrapperDisk = new QueryWrapper<>();
                    List<IaasVirtualMachineDisk> diskList = virtualMachineDiskMapper.selectList(wrapperDisk.lambda()
                            .eq(IaasVirtualMachineDisk::getVmId, iaasVirtualDataCenter.getId())
                            .eq(IaasVirtualMachineDisk::isDeleted, false));

                    if (CollectionUtils.isNotEmpty(diskList)) {
                        for (IaasVirtualMachineDisk disk : diskList) {
                            disk.setDeleted(true);
                            virtualMachineDiskMapper.updateById(disk);
                        }
                    }
                }
            }

        }
    }

    //???????????????????????????
    private void nanotubeResource(ApplyInfoVO vo) {
        IaasApplyResourceVO applyResourceVO = vo.getApplyResourceVO();

        //???????????????????????????
        String authz = AuthContext.getAuthz();

        //???????????????????????????vdc token
        IaasVirtualDataCenter ivdc = iaasVirtualDataCenterMapper.selectById(applyResourceVO.getVdcId());
        if (ivdc == null) {
            log.warn("??????vdc????????????!");
            return;
        }

        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryIaasProviderVOById(ivdc.getProviderId());
        if (iaasProviderVO == null) {
            log.warn("???????????????????????????!");
            return;
        }

        //1.????????????????????????????????????token
        TokenInfoResponse tokenResponse = providerService.getToken(authz, iaasProviderVO);

        if (tokenResponse == null) {
            log.warn("???????????????token??????!");
            return;
        }

        //????????????????????????token
        IaasProject iaasProject = projectService.queryIaasProjectById(applyResourceVO.getProjectId());
        StringResponse tokenByVdcUser = null;
        try {
            tokenByVdcUser = oauthTokenClient.getTokenByVdcUser(authz,
                    iaasProject.getProjectKey(),
                    ivdc.getDomainName(), ivdc.getUsername(), ivdc.getPassword());
        } catch (Exception e) {
            log.info("??????vdc token?????????");
            return;
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        RequestVO requestVO = new RequestVO();
        //omToken
        requestVO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());
        //vdc token
        requestVO.setOcToken(tokenByVdcUser == null ? "" : tokenByVdcUser.getData());
        requestVO.setProjectId(iaasProject.getProjectKey());

        //?????????????????????
        VDCVirtualMachineListResponse vmResponse = null;
        try {
            vmResponse = vdcVirtualMachineClient.getVirtualMachineList(authz, requestVO);
        } catch (Exception e) {
            log.error("??????????????????????????????????????????", e);
        }

        if (vmResponse == null || vmResponse.getCode() != 0 || CollectionUtils.isEmpty(vmResponse.getData())) {
            log.info("???????????????????????????!");
            return;
        }

        //??????????????????
        HostVolumesVOListResponse diskResponse = null;
        try {
            diskResponse = hostVolumesClient.getHostVolumesList(authz, requestVO);
        } catch (Exception e) {
            log.error("????????????????????????????????????????????????", e);
        }

        List<VirtualMachineVO> vmData = vmResponse.getData();
        //???????????????
        List<IaasApplyConfigInfoVO> configInfoVOList = iaasApplyConfigInfoMapper.queryApplyConfigInfoList(applyResourceVO.getId(), null);

        List<IaasApplyConfigInfoVO> vmList = configInfoVOList.stream().filter(item -> "1".equals(item.getApplyType())).collect(toList());

        //????????????????????????????????????
        List<String> serveNameList = vmList.stream().map(IaasApplyConfigInfoVO::getServerName).collect(Collectors.toList());

        List<VirtualMachineVO> screenVm = vmData.stream().filter(Objects::nonNull).filter(e ->
                serveNameList.contains(Optional.ofNullable(e.getName()).map(m -> m.get("value")).orElse(""))).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(screenVm)) {
            //???????????????????????????????????????
            String[] businessIds = {String.valueOf(applyResourceVO.getBusinessGroupId())};
            //??????????????????????????????
            List<ResourcePoolVO> poolByGroupId = iaasResourcePoolMapper.getPoolByGroupId(applyResourceVO.getBusinessGroupId());

            Long poolId = null;
            if (CollectionUtils.isNotEmpty(poolByGroupId)) {
                poolId = poolByGroupId.get(0).getId();
            }

            providerService.handleVMInfo(ivdc.getProviderId(), applyResourceVO.getProjectId(), null, businessIds, poolId, screenVm, diskResponse);

            //?????????????????????-???????????? vcpu ?????? ??????????????????????????????
            providerService.handlePool(poolId, screenVm);
        }

        //???????????????
        Long projectId = vo.projectId();
        if (projectId == null) {
            return;
        }
        BaremetalListResponse bmsListResponse = baremetalServersFlavorsClient.getBaremetalList(String.valueOf(projectId));

        if (bmsListResponse == null || bmsListResponse.getCode() != 0 || CollectionUtils.isEmpty(bmsListResponse.getData())) {
            return;
        }

        List<BareMetalVO> bareMetalVOS = bmsListResponse.getData();

        List<IaasApplyConfigInfoVO> ljsList = configInfoVOList.stream().filter(item -> "2".equals(item.getApplyType())).collect(toList());

        //????????????????????????????????????
        List<BareMetalVO> ngljs = jiaojiVM(bareMetalVOS, ljsList);

        if (CollectionUtils.isNotEmpty(ngljs)) {
            handleBareMetal(vo.businessGroupId(), ngljs);
        }
    }

    private void handleBareMetal(Long businessGroupId, List<BareMetalVO> ngljs) {
        if (CollectionUtils.isEmpty(ngljs)) {
            return;
        }

        for (BareMetalVO vo : ngljs) {
            QueryWrapper<IaasBareMetal> queryWrapperIaasBareMetal = new QueryWrapper<>();
            queryWrapperIaasBareMetal.lambda().eq(IaasBareMetal::getNativieId, vo.getNativieId());
            IaasBareMetal queryBare = iaasBareMetalService.getOne(queryWrapperIaasBareMetal);

            IaasProject project = iaasProjectMapper.queryIaasProjectByKey(vo.getTenantId());

            IaasArea area = iaasAreaMapper.queryIaasAreaByKey(vo.getRegionId());

            if (queryBare == null) {
                IaasBareMetal bareMetal = new IaasBareMetal();
                long id = SnowFlakeIdGenerator.getInstance().nextId();
                BeanUtils.copyProperties(vo, bareMetal);
                bareMetal.setId(id);
                bareMetal.setName(vo.getValue());
                bareMetal.setCreateTime(DateUtil.getNow());
                bareMetal.setCreateUser(Sign.getUserId());
                bareMetal.setBusinessGroupId(businessGroupId);

                if (project != null) {
                    bareMetal.setProjectId(project.getId());
                }
                if (area != null) {
                    bareMetal.setAreaId(area.getId());
                }
                iaasBareMetalService.save(bareMetal);
            } else {
                vo.setId(queryBare.getId());
                BeanUtils.copyProperties(vo, queryBare);
                queryBare.setBusinessGroupId(businessGroupId);
                queryBare.setCreateTime(DateUtil.getNow());
                queryBare.setCreateUser(Sign.getUserId());
                queryBare.setName(vo.getValue());
                if (project != null) {
                    queryBare.setProjectId(project.getId());
                }
                if (area != null) {
                    queryBare.setAreaId(area.getId());
                }

                iaasBareMetalService.updateById(queryBare);
            }
        }
    }

    private static List<BareMetalVO> jiaojiVM(List<BareMetalVO> vmList, List<IaasApplyConfigInfoVO> configList) {
        List<BareMetalVO> vmNewList = new ArrayList<>();
        List<String> serveNameList = configList.stream().map(IaasApplyConfigInfoVO::getServerName).collect(Collectors.toList());
        for (int i = 0; i < vmList.size(); i++) {
            BareMetalVO machineVO = vmList.get(i);

            if (StringUtils.isBlank(machineVO.getValue())) {
                continue;
            }
            String value = machineVO.getValue();
            for (int j = 0; j < serveNameList.size(); j++) {
                String s = serveNameList.get(j);
                if (value.equals(s)) {
                    vmNewList.add(machineVO);
                }
            }
        }
        return vmNewList;
    }


    //????????????
    public BaseResponse saveData(SaveDataVO saveDataVO) {
        BaseResponse baseResponse = new BaseResponse();

        //??????id??????????????????
        //2.??????iaas_apply_config_info ????????????
        for (ApplyAreaUpdateVO applyAreaUpdateVO : saveDataVO.getAreaVOList()) {
            QueryWrapper<IaasApplyConfigInfo> query2 = new QueryWrapper<>();
            query2.eq("id", applyAreaUpdateVO.getConfigId());
            query2.eq("is_deleted", 0);
            query2.eq(false,"apply_type",3);
            IaasApplyConfigInfo iaasApplyConfigInfo = iaasApplyConfigInfoMapper.selectOne(query2);

            if (iaasApplyConfigInfo.getServerName() != null && !"".equals(iaasApplyConfigInfo.getServerName()) &&  !"3".equals(iaasApplyConfigInfo.getOperationType())) {
                //?????????????????????
                QueryWrapper<IaasApplyStorage> querys = new QueryWrapper<>();
                querys.eq("apply_id", saveDataVO.getApplyId());
                querys.eq("config_id", iaasApplyConfigInfo.getId());
                querys.eq("is_deleted", 0);
                querys.eq("store_resource_type", "system");
                if (iaasApplyStorageMapper.selectCount(querys) < 1) {
                    IaasApplyStorage iaasApplyStorage = new IaasApplyStorage();
                    Long storageId = SnowFlakeIdGenerator.getInstance().nextId();
                    iaasApplyStorage.setId(storageId);
                    iaasApplyStorage.setApplyId(saveDataVO.getApplyId());
                    String s = iaasApplyConfigInfo.getSystemDisk()==null?"":iaasApplyConfigInfo.getSystemDisk().toString();
                    if (s.length() < 1) {
                        iaasApplyStorage.setResourceNum(0);
                    }else {
                        iaasApplyStorage.setResourceNum(Integer.parseInt(s.substring(0, s.indexOf("."))));
                    }
                    iaasApplyStorage.setConfigId(iaasApplyConfigInfo.getId());
                    iaasApplyStorage.setOperationType(iaasApplyConfigInfo.getOperationType());
                    iaasApplyStorage.setStoreResourceType("system");
                    iaasApplyStorageMapper.insert(iaasApplyStorage);

                    //???????????????-?????????????????????
                    IaasApplyRelationInfo relationInfo = new IaasApplyRelationInfo();
                    relationInfo.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    relationInfo.setBusinessId(storageId);
                    relationInfo.setProcessInstanceId(saveDataVO.getProcessId());
                    relationInfo.setApplyId(saveDataVO.getApplyId());
                    relationInfo.setRelationUser(Sign.getUserId());
                    iaasApplyRelationInfoMapper.insert(relationInfo);

                    //?????????-?????????????????????

                    QueryWrapper<IaasApplyMemoryDisk> query3 = new QueryWrapper<>();
                    query3.eq("config_id", applyAreaUpdateVO.getConfigId());
                    query3.eq("is_deleted", 0);
                    List<IaasApplyMemoryDisk> iaasApplyMemoryDisks = iaasApplyMemoryDiskMapper.selectList(query3);
                    for (IaasApplyMemoryDisk disk : iaasApplyMemoryDisks) {

                        IaasApplyStorage iaasApplyStorage2 = new IaasApplyStorage();
                        Long storageId2 = SnowFlakeIdGenerator.getInstance().nextId();
                        iaasApplyStorage2.setId(storageId2);
                        iaasApplyStorage2.setApplyId(saveDataVO.getApplyId());
                        String s1 = disk.getMemoryDisk() == null ? "" : disk.getMemoryDisk().toString();
                        if (s1.length() < 1) {
                            iaasApplyStorage2.setResourceNum(0);
                        } else {
                            iaasApplyStorage2.setResourceNum(Integer.parseInt(s1.substring(0, s1.indexOf("."))));
                            iaasApplyStorage2.setConfigId(iaasApplyConfigInfo.getId());
                            iaasApplyStorage2.setOperationType(iaasApplyConfigInfo.getOperationType());
                            iaasApplyStorage2.setStoreResourceType("storage");
                            iaasApplyStorageMapper.insert(iaasApplyStorage2);
                        }


                        IaasApplyRelationInfo relationInfoDisk = new IaasApplyRelationInfo();
                        relationInfoDisk.setId(SnowFlakeIdGenerator.getInstance().nextId());
                        relationInfoDisk.setBusinessId(storageId2);
                        relationInfoDisk.setProcessInstanceId(saveDataVO.getProcessId());
                        relationInfoDisk.setApplyId(saveDataVO.getApplyId());
                        relationInfoDisk.setRelationUser(Sign.getUserId());
                        iaasApplyRelationInfoMapper.insert(relationInfoDisk);
                    }

                }

                //????????????
                QueryWrapper<IaasApplyCalculate> queryc = new QueryWrapper<>();
                queryc.eq("apply_id", saveDataVO.getApplyId());
                queryc.eq("config_id", iaasApplyConfigInfo.getId());
                queryc.eq("is_deleted", 0);
                if (iaasApplyCalculateMapper.selectCount(queryc) < 1) {

                    IaasApplyCalculate iaasApplyCalculate = new IaasApplyCalculate();
                    Long calculateId = SnowFlakeIdGenerator.getInstance().nextId();
                    iaasApplyCalculate.setId(calculateId);
                    iaasApplyCalculate.setApplyId(saveDataVO.getApplyId());
                    iaasApplyCalculate.setOperationType(iaasApplyConfigInfo.getOperationType());
                    iaasApplyCalculate.setConfigId(iaasApplyConfigInfo.getId());
                    iaasApplyCalculate.setModel(iaasApplyConfigInfo.getModel());
                    iaasApplyCalculate.setMemory(iaasApplyConfigInfo.getMemory());
                    iaasApplyCalculate.setCpu(iaasApplyConfigInfo.getCpu());

                    iaasApplyCalculateMapper.insert(iaasApplyCalculate);

                    //??????-?????????????????????
                    IaasApplyRelationInfo relationInfo2 = new IaasApplyRelationInfo();
                    relationInfo2.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    relationInfo2.setBusinessId(calculateId);
                    relationInfo2.setProcessInstanceId(saveDataVO.getProcessId());
                    relationInfo2.setApplyId(saveDataVO.getApplyId());
                    relationInfo2.setRelationUser(Sign.getUserId());
                    iaasApplyRelationInfoMapper.insert(relationInfo2);
                }
                //????????????
                QueryWrapper<IaasApplyOther> queryo = new QueryWrapper<>();
                queryo.eq("apply_id", saveDataVO.getApplyId());
                queryo.eq("config_id", iaasApplyConfigInfo.getId());
                queryo.eq("is_deleted", 0);
                if (iaasApplyOtherMapper.selectCount(queryo) < 1) {
                    IaasApplyOther iaasApplyOther = new IaasApplyOther();
                    Long otherId = SnowFlakeIdGenerator.getInstance().nextId();
                    iaasApplyOther.setId(otherId);
                    iaasApplyOther.setApplyId(saveDataVO.getApplyId());
                    iaasApplyOther.setConfigId(iaasApplyConfigInfo.getId());
                    iaasApplyOther.setImageName(iaasApplyConfigInfo.getOperationSystem());
                    iaasApplyOtherMapper.insert(iaasApplyOther);

                    //??????-?????????????????????
                    IaasApplyRelationInfo relationInfo3 = new IaasApplyRelationInfo();
                    relationInfo3.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    relationInfo3.setBusinessId(otherId);
                    relationInfo3.setProcessInstanceId(saveDataVO.getProcessId());
                    relationInfo3.setApplyId(saveDataVO.getApplyId());
                    relationInfo3.setRelationUser(Sign.getUserId());
                    iaasApplyRelationInfoMapper.insert(relationInfo3);

                }
            }
        }
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("???????????????");
        return baseResponse;

    }


}

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
 * 流程相关
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

        //更新申请资源选择的区域
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

        //处理审批中上传文件
        handleFile(vo.applyId(), vo.getApplyFileVos());

        CompleteTaskRequest completeTask = new CompleteTaskRequest();
        completeTask.setTaskId(vo.getTaskId());
        completeTask.setOutcome(vo.getOutcome());
        completeTask.setComment(vo.getComment());
        completeTask.setUserId(vo.getUserId());
        completeTask.setUserDisplayName(vo.getUserDisplayName());

        // 自动回收资源
        Integer processWorkflowStep = vo.getProcessWorkflowStep();
        log.info("processWorkflowStep: " + processWorkflowStep);
        log.info("Outcome: " + vo.getOutcome());
        if (StringUtils.equalsIgnoreCase(vo.getOutcome(), "同意") && processWorkflowStep != null && processWorkflowStep == 7) {
            String message = applyResourceService.queryApplyResourceByProcessInstanceId(vo.getProcessInstanceId());
            log.info(message);
        }

//        //在第二个人审批的时候自动填充资源信息
//        if(processWorkflowStep ==2 ){
//            //1.获取applyId(工单编号)
//            IaasApplyResourceVO applyResourceVO = vo.getApplyResourceVO();
//            //2.获取configId(虚拟机/裸金属/安全资源的id)
//            List<ApplyAreaUpdateVO> areaVOList1 = vo.getAreaVOList();
//            //3.获取流程实例id(activiti回填id)
//            this.saveData(applyResourceVO.getId(),areaVOList1,vo.getProcessInstanceId());
//        }

        //调用完成任务接口，下一步,拒绝到上一步，驳回到申请人发起申请
        JSONObjectResponse jsonObjectResponse = activitiClient.completeTask(AuthContext.getAuthz(), completeTask);

        if (jsonObjectResponse == null || jsonObjectResponse.getCode() != 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("审核失败！");
            return baseResponse;
        }

        //判断是否发送oa
        if ("2".equals(vo.getSubmitOa()) && "同意".equals(vo.getOutcome())) {
            //推送oa创建流程 todo email
            String resUserId = webServiceClient.getUserId("email", "szghygaoyang@chengtou.com");
            String userId = resUserId.replace("\"", "");

            if (StringUtils.isBlank(userId)) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("获取oa用户id为空！");
                log.info("获取oa用户id为空！", resUserId);
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
            //如果为空则创建工作流程
            if (StringUtils.isBlank(applyResource.getGroupProcessInstanceId())) {
                WorkflowRequestInfoVO workflowRequestInfoVO = assemblerWorkflowRequestInfoVO(userId, pdfName, pdf2, "泛微-云管平台测试", null, null, null);
                log.info("获取oa则创建工作流程！" + workflowRequestInfoVO.toString());
                resGroupProcessInstanceId = webServiceClient.doCreateWorkflowRequest(workflowRequestInfoVO);
                log.info("获取oa则创建工作流程结果！", resGroupProcessInstanceId);
                //提交流程报文
            } else {
                WorkflowRequestInfoVO workflowRequestInfoVO = assemblerWorkflowRequestInfoVO(userId, pdfName, pdf2, "泛微-云管平台测试(修改)", Integer.valueOf(applyResource.getGroupProcessInstanceId()), "subback", "再次提交");

                resGroupProcessInstanceId = webServiceClient.submitWorkflowRequest(workflowRequestInfoVO);
                log.info("获取oa则提交工作流程结果2！", workflowRequestInfoVO.toString());
                log.info("获取oa则提交工作流程结果2！", resGroupProcessInstanceId);
            }

            if (StringUtils.isBlank(resGroupProcessInstanceId)) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("创建oa流程异常！");
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

        if ("结束".equals(vo.getOutcome())) {
            IaasApplyResource resource = new IaasApplyResource();
            resource.setId(vo.applyId());
            resource.setState(2);
            resource.setUpdateUser(Sign.getUserId());
            resource.setUpdateTime(DateUtil.getNow());

            iaasResourceApplyMapper.updateById(resource);

            if ("1".equals(vo.getType())) {
                //纳管虚拟机或裸金属
                nanotubeResource(vo);
            } else {
                //取消纳管
                deleteNanotubeResource(vo);
            }
        }

        if ("驳回".equals(vo.getOutcome())) {
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
        //是否有未审核的人员
        List<WorkflowTaskCandidateVO> isApproved = candidateList.stream().filter(e -> "0".equals(e.getIsApproved())).collect(Collectors.toList());

        //是否有审批不同意的人员
        List<WorkflowTaskCandidateVO> outcome = candidateList.stream().filter(e -> !"同意".equals(e.getOutcome())).collect(Collectors.toList());

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
                if (imageByte[i] < 0) {// 调整异常数据
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

    //处理文件上传
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
        tableField2.setFieldValue("城投研究院");
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
        workflowRequestInfo.setRequestName("云资源申请");
        workflowRequestInfo.setUserId(Integer.valueOf(userId));

        //提交工作流程报文用
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

    //取消纳管
    private void deleteNanotubeResource(ApplyInfoVO vo) {
        IaasApplyResourceVO applyResourceVO = vo.getApplyResourceVO();
        //处理虚拟机
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

    //纳管虚拟机或裸金属
    private void nanotubeResource(ApplyInfoVO vo) {
        IaasApplyResourceVO applyResourceVO = vo.getApplyResourceVO();

        //纳管虚拟机或裸金属
        String authz = AuthContext.getAuthz();

        //通过用户名密码获取vdc token
        IaasVirtualDataCenter ivdc = iaasVirtualDataCenterMapper.selectById(applyResourceVO.getVdcId());
        if (ivdc == null) {
            log.warn("获取vdc信息为空!");
            return;
        }

        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryIaasProviderVOById(ivdc.getProviderId());
        if (iaasProviderVO == null) {
            log.warn("获取供应商信息为空!");
            return;
        }

        //1.通过用户名密码获取供应商token
        TokenInfoResponse tokenResponse = providerService.getToken(authz, iaasProviderVO);

        if (tokenResponse == null) {
            log.warn("获取供应商token错误!");
            return;
        }

        //获取项目信息获取token
        IaasProject iaasProject = projectService.queryIaasProjectById(applyResourceVO.getProjectId());
        StringResponse tokenByVdcUser = null;
        try {
            tokenByVdcUser = oauthTokenClient.getTokenByVdcUser(authz,
                    iaasProject.getProjectKey(),
                    ivdc.getDomainName(), ivdc.getUsername(), ivdc.getPassword());
        } catch (Exception e) {
            log.info("获取vdc token错误！");
            return;
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        RequestVO requestVO = new RequestVO();
        //omToken
        requestVO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());
        //vdc token
        requestVO.setOcToken(tokenByVdcUser == null ? "" : tokenByVdcUser.getData());
        requestVO.setProjectId(iaasProject.getProjectKey());

        //获取虚拟机信息
        VDCVirtualMachineListResponse vmResponse = null;
        try {
            vmResponse = vdcVirtualMachineClient.getVirtualMachineList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机信息异常！", e);
        }

        if (vmResponse == null || vmResponse.getCode() != 0 || CollectionUtils.isEmpty(vmResponse.getData())) {
            log.info("获取虚拟机信息错误!");
            return;
        }

        //获取磁盘信息
        HostVolumesVOListResponse diskResponse = null;
        try {
            diskResponse = hostVolumesClient.getHostVolumesList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机磁盘信息异常！", e);
        }

        List<VirtualMachineVO> vmData = vmResponse.getData();
        //处理虚拟机
        List<IaasApplyConfigInfoVO> configInfoVOList = iaasApplyConfigInfoMapper.queryApplyConfigInfoList(applyResourceVO.getId(), null);

        List<IaasApplyConfigInfoVO> vmList = configInfoVOList.stream().filter(item -> "1".equals(item.getApplyType())).collect(toList());

        //取出虚拟机名称相同的数据
        List<String> serveNameList = vmList.stream().map(IaasApplyConfigInfoVO::getServerName).collect(Collectors.toList());

        List<VirtualMachineVO> screenVm = vmData.stream().filter(Objects::nonNull).filter(e ->
                serveNameList.contains(Optional.ofNullable(e.getName()).map(m -> m.get("value")).orElse(""))).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(screenVm)) {
            //处理虚拟机和虚拟机磁盘信息
            String[] businessIds = {String.valueOf(applyResourceVO.getBusinessGroupId())};
            //根据业务组找到资源池
            List<ResourcePoolVO> poolByGroupId = iaasResourcePoolMapper.getPoolByGroupId(applyResourceVO.getBusinessGroupId());

            Long poolId = null;
            if (CollectionUtils.isNotEmpty(poolByGroupId)) {
                poolId = poolByGroupId.get(0).getId();
            }

            providerService.handleVMInfo(ivdc.getProviderId(), applyResourceVO.getProjectId(), null, businessIds, poolId, screenVm, diskResponse);

            //自动填充资源池-计算资源 vcpu 内存 虚机数量的已分配容量
            providerService.handlePool(poolId, screenVm);
        }

        //处理裸金属
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

        //取出裸金属名称相同的数据
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


    //处理数据
    public BaseResponse saveData(SaveDataVO saveDataVO) {
        BaseResponse baseResponse = new BaseResponse();

        //根据id先查询到信息
        //2.查到iaas_apply_config_info 多条信息
        for (ApplyAreaUpdateVO applyAreaUpdateVO : saveDataVO.getAreaVOList()) {
            QueryWrapper<IaasApplyConfigInfo> query2 = new QueryWrapper<>();
            query2.eq("id", applyAreaUpdateVO.getConfigId());
            query2.eq("is_deleted", 0);
            query2.eq(false,"apply_type",3);
            IaasApplyConfigInfo iaasApplyConfigInfo = iaasApplyConfigInfoMapper.selectOne(query2);

            if (iaasApplyConfigInfo.getServerName() != null && !"".equals(iaasApplyConfigInfo.getServerName()) &&  !"3".equals(iaasApplyConfigInfo.getOperationType())) {
                //系统盘存储信息
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

                    //系统盘存储-审核关系维护表
                    IaasApplyRelationInfo relationInfo = new IaasApplyRelationInfo();
                    relationInfo.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    relationInfo.setBusinessId(storageId);
                    relationInfo.setProcessInstanceId(saveDataVO.getProcessId());
                    relationInfo.setApplyId(saveDataVO.getApplyId());
                    relationInfo.setRelationUser(Sign.getUserId());
                    iaasApplyRelationInfoMapper.insert(relationInfo);

                    //存储盘-审核关系维护表

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

                //计算信息
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

                    //计算-审核关系维护表
                    IaasApplyRelationInfo relationInfo2 = new IaasApplyRelationInfo();
                    relationInfo2.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    relationInfo2.setBusinessId(calculateId);
                    relationInfo2.setProcessInstanceId(saveDataVO.getProcessId());
                    relationInfo2.setApplyId(saveDataVO.getApplyId());
                    relationInfo2.setRelationUser(Sign.getUserId());
                    iaasApplyRelationInfoMapper.insert(relationInfo2);
                }
                //配置信息
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

                    //配置-审核关系维护表
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
        baseResponse.setMessage("填充成功！");
        return baseResponse;

    }


}

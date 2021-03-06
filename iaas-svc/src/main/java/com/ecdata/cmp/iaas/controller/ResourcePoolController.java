package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.HostVolumesClient;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.client.VDCVirtualMachineClient;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.response.VDCVirtualMachineListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenInfoVO;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.*;
import com.ecdata.cmp.iaas.entity.dto.*;
import com.ecdata.cmp.iaas.entity.dto.request.VMRequest;
import com.ecdata.cmp.iaas.entity.dto.response.*;
import com.ecdata.cmp.iaas.entity.dto.response.project.BusinessGroupCascade;
import com.ecdata.cmp.iaas.mapper.IaasClusterMapper;
import com.ecdata.cmp.iaas.mapper.IaasHostMapper;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualDataCenterMapper;
import com.ecdata.cmp.iaas.service.*;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import com.ecdata.cmp.user.dto.response.SysBusinessGroupListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @title: ResourcePool controller
 * @Author: shig
 * @description: ????????? ?????????
 * @Date: 2019/11/18 4:01 ??????
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/resourcePool")
@Api(tags = "??????????????????????API")
public class ResourcePoolController {
    /**
     * ?????????(resourcePool) service
     */
    @Autowired
    private IResourcePoolService resourcePoolService;

    /**
     * ???????????????(resourcePoolDatastore) service
     */
    @Autowired
    private IResourcePoolDatastoreService resourcePoolDatastoreService;

    /**
     * ?????????(provider) service
     */
    @Autowired
    private IIaasProviderService providerService;

    /**
     * ??????(cluster) service
     */
    @Autowired
    private IClusterService clusterService;

    /**
     * ??????(host) service
     */
    @Autowired
    private IHostService hostService;

    @Autowired
    private ProviderService syncProviderService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IaasVirtualMachineService virtualMachineService;

    @Autowired
    private OauthTokenClient oauthTokenClient;

    @Autowired
    private VDCVirtualMachineClient vdcVirtualMachineClient;

    @Autowired
    private IaasProviderMapper iaasProviderMapper;

    @Autowired
    private IaasBareMetalService iaasBareMetalService;

    @Autowired
    private IaasClusterMapper sysClusterMapper;

    @Autowired
    private  IaasHostMapper sysHostMapper;

    @Autowired
    private IaasVirtualDataCenterMapper virtualDataCenterMapper;

    /**
     * User?????????????????????id??????????????????
     */
//    @Autowired
//    private UserClient userClient;

    /**
     * User?????????????????????????????????disName+list by id???
     */
//    @Autowired
//    private SysBusinessGroupClient sysBusinessGroupClient;


    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param pageNo   ?????????
     * @param pageSize ???????????????
     * @param keyword  ?????????
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "businessIds", value = "?????????ids?????????,???: 1,2,3", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ResourcePoolPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) String businessIds) {
        Page<ResourcePoolVO> page = new Page<>(pageNo, pageSize);
        log.info("res page");
        //????????????????????????
        ResourcePoolVO resourcePoolVO = new ResourcePoolVO();
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        if (businessIds != null && businessIds != "") {
            params.put("businessIds", businessIds.split(","));
        }
        IPage<ResourcePoolVO> result = resourcePoolService.queryResourcePoolPage(page, params);
        //??????????????????
        return ResponseEntity.status(HttpStatus.OK).body(new ResourcePoolPageResponse(new PageVO<>(result)));
    }


    /**
     * ??????:/v1/resourcePool/add
     *
     * @param resourcePoolVO
     * @return BaseResponse
     * @throws Exception
     */
    @PostMapping("/add")
    @ApiOperation(value = "???????????????", notes = "???????????????")
    public ResponseEntity<ResourcePoolAsResponse> add(@RequestBody ResourcePoolVO resourcePoolVO) throws Exception {
        IaasResourcePool resourcePool = new IaasResourcePool();
        //???????????????????????? IaasResourcePool
        BeanUtils.copyProperties(resourcePoolVO, resourcePool);
        //??????ID?????????????????????id
        Long poolId = SnowFlakeIdGenerator.getInstance().nextId();
        resourcePool.setId(poolId);
        //createUser+time
        resourcePool.setCreateUser(Sign.getUserId());
        resourcePool.setCreateTime(DateUtil.getNow());
        //????????????????????????????????????
        if (resourcePoolVO.getVcpuTotalAllocate() == null) {
            resourcePoolVO.setVcpuTotalAllocate(0);
        }

        //????????????????????????
        String[] businessIds = null;
        if (resourcePoolVO.getBusinessIds() != null) {
            businessIds = resourcePoolVO.getBusinessIds().split(",");
            SysBusinessGroupResourcePoolVO businessGroupResourcePool = null;
            for (String businessId : businessIds) {
                businessGroupResourcePool = new SysBusinessGroupResourcePoolVO();
                businessGroupResourcePool.setId(SnowFlakeIdGenerator.getInstance().nextId());
                businessGroupResourcePool.setBusinessGroupId(Long.parseLong(businessId));
                //0 user??????,1 as????????? 2pa?????????
                businessGroupResourcePool.setType(1);
                businessGroupResourcePool.setPoolId(poolId);
                businessGroupResourcePool.setCreateUser(Sign.getUserId());
                businessGroupResourcePool.setCreateTime(DateUtil.getNow());
                resourcePoolService.addBusinessGroupResourcePool(businessGroupResourcePool);
            }
        }

        //??????????????????
        ResourcePoolAsResponse resourcePoolAsResponse = null;
        //????????????????????????
        if (resourcePoolService.save(resourcePool)) {
            resourcePoolAsResponse = new ResourcePoolAsResponse();
            //??????id??????
            BeanUtils.copyProperties(resourcePool, resourcePoolVO);
            resourcePoolAsResponse.setData(resourcePoolVO);
            resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            resourcePoolAsResponse.setMessage("????????????");

            //???????????????????????????
            if ("2".equals(resourcePoolVO.getType())) {

                VMRequest vmRequest = new VMRequest();
                vmRequest.setProviderId(resourcePoolVO.getProviderId());
               // vmRequest.setBusinessId(StringUtils.isBlank(resourcePoolVO.getBusinessIds()) ? null : Long.valueOf(resourcePoolVO.getBusinessIds()));
                vmRequest.setPoolId(poolId);
                vmRequest.setProjectKey(resourcePoolVO.getProjectKey());
                vmRequest.setDomainName(resourcePoolVO.getDomainName());
                vmRequest.setUsername(resourcePoolVO.getUsername());
                vmRequest.setPassword(resourcePoolVO.getPassword());
                vmRequest.setProjectId(resourcePoolVO.getProjectId());
                vmRequest.setBusinessIds(businessIds);
                vmRequest.setVmKeyList(resourcePoolVO.getVmKeyList());
                BaseResponse baseResponse = syncProviderService.addVMByVmKeyList(resourcePoolVO, null);
               // BaseResponse baseResponse = syncProviderService.syncProjectVM(vmRequest);
                log.info("????????????????????????");
                if (baseResponse != null && baseResponse.getCode() == 0) {
                    resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    resourcePoolAsResponse.setMessage("????????????????????????????????????????????????");
                } else {
                    resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    resourcePoolAsResponse.setMessage("??????????????????????????????");
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resourcePoolAsResponse);
        } else {
            resourcePoolAsResponse = new ResourcePoolAsResponse();
            resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            resourcePoolAsResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourcePoolAsResponse);
        }
    }

    /**
     * ??????IaasResourcePool
     *
     * @param resourcePoolVO
     * @param resourcePool
     */
    private void saveIaasResourcePool(@RequestBody ResourcePoolVO resourcePoolVO, IaasResourcePool resourcePool) {

    }


    /**
     * ?????????/resourcePool/update
     *
     * @param resourcePoolVO
     * @return BaseResponse
     * @throws Exception
     */
    @PutMapping("/update")
    @ApiOperation(value = "???????????????", notes = "???????????????")
    public ResponseEntity<BaseResponse> update(@RequestBody ResourcePoolVO resourcePoolVO) throws Exception {
        log.info("res update");
        //???????????????
        IaasResourcePool resourcePool = new IaasResourcePool();
        /**
         * ??????????????????IaasResourcePool
         */
        if (updateIaasResourcePool(resourcePoolVO, resourcePool))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));

        //????????????????????????(?????????
        String[] businessIds = null;
        List<String> removeBusinessIds = null;
        if (resourcePoolVO.getBusinessIds() != null && resourcePoolVO.getBusinessIds() != "") {
            businessIds = resourcePoolVO.getBusinessIds().split(",");
            log.info("update "+resourcePoolVO.toString());

            List<SysBusinessGroupResourcePoolVO> removeBusinessRela = resourcePoolService.getBusinessGroupNameByPoolId(resourcePoolVO.getId());
            if(removeBusinessRela != null && removeBusinessRela.size() >0){
                removeBusinessIds = removeBusinessRela.stream().map(SysBusinessGroupResourcePoolVO::getBusinessIds).collect(Collectors.toList());
                List<String> currentIds = Arrays.asList(businessIds);
                removeBusinessIds.removeAll(currentIds);
            }

            //??????poolId????????????????????????????????????(????????????)
            resourcePoolService.removeBusinessGroupResourcePool(resourcePoolVO.getId());

            for (String businessId : businessIds) {
                SysBusinessGroupResourcePoolVO businessGroupResourcePool = new SysBusinessGroupResourcePoolVO();
                businessGroupResourcePool.setId(SnowFlakeIdGenerator.getInstance().nextId());
                businessGroupResourcePool.setBusinessGroupId(Long.parseLong(businessId));
                //0 user??????,1 as????????? 2pa?????????
                businessGroupResourcePool.setType(1);
                businessGroupResourcePool.setPoolId(resourcePoolVO.getId());
                businessGroupResourcePool.setCreateUser(Sign.getUserId());
                businessGroupResourcePool.setCreateTime(DateUtil.getNow());
                resourcePoolService.addBusinessGroupResourcePool(businessGroupResourcePool);
            }
        }

        //??????
        BaseResponse baseResponse = null;
        if (resourcePoolService.updateById(resourcePool)) {
            //???????????????????????????
            if ("2".equals(resourcePoolVO.getType())) {
                String cloudRes = "";
                if(resourcePoolVO.getVmOrMetal()==0) {
                    baseResponse = syncProviderService.addVMByVmKeyList(resourcePoolVO, removeBusinessIds);
                    cloudRes = "?????????";
                    log.info("????????????????????????");
                }
                else if(resourcePoolVO.getVmOrMetal()==1) {
                    baseResponse = iaasBareMetalService.syncUpdate(resourcePoolVO);
                    cloudRes = "?????????";
                    log.info("????????????????????????");
                }
                //baseResponse = syncProviderService.syncProjectVM(vmRequest);
                if (baseResponse != null && baseResponse.getCode() == 0) {
                    baseResponse.setMessage("??????????????????????????????"+cloudRes+"?????????");
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                } else {
                    baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    baseResponse.setMessage("??????"+cloudRes+"???????????????");
                }
            }
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    /**
     * ??????????????????????????????????????????????????????
     * getTokenNew
     * projectId???????????????????????????????????????provider??????projectName??????
     */
    @GetMapping("/synchCheck")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "poolId", value = "?????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "projectId", value = "??????id", paramType = "query", dataType = "long")
    })
    public VMMapResponse synchCheck(@RequestParam(name = "poolId") Long id,
            @RequestParam(name = "projectId") Long pid) throws Exception {
        //???????????????
        log.info("res check");
        VMMapResponse vmMapResponse =  new VMMapResponse();
        Map<String,Object> result = new HashMap<>();
//    resPool -> ?????????  -> vdc -> ?????? -> ?????????


        VDCVirtualMachineListResponse baseResponse = null;
        String authz=AuthContext.getAuthz();
        Map<String ,Object> map = new HashMap<>();
        map.put("poolId",id);
        //?????????
        List<IaasVirtualMachineVO> vmvos = virtualMachineService.getVirtualMachineVOPage(map);
        if(CollectionUtils.isNotEmpty(vmvos)){
            vmvos.forEach((IaasVirtualMachineVO vo)->{vo.setKey(vo.getVmKey());vo.setBinded(1);vo.setTitle(vo.getVmName());});
        }else{
            vmMapResponse.setCode(201);
            vmMapResponse.setMessage("?????????????????????!");
        }

        QueryWrapper<IaasVirtualMachine> query = new QueryWrapper<>();
        query.eq("project_id", pid);
        query.eq("is_deleted",0);
        List<IaasVirtualMachine> removeVMListByPid = virtualMachineService.list(query);

//        QueryWrapper<IaasProject> queryIp = new QueryWrapper<>();
//        queryIp.lambda().eq(IaasProject::getProjectKey,pid);

//        QueryWrapper<IaasVirtualDataCenter> queryVdc = new QueryWrapper<>();


        IaasResourcePool iaasResourcePool = resourcePoolService.getById(id);
        Long vdcId=iaasResourcePool.getVdcId();
        IaasVirtualDataCenter ivdc = virtualDataCenterMapper.selectById(vdcId);

//        QueryWrapper<IaasResourcePool> queryVdc = new QueryWrapper<>();
        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryIaasProviderVOById(iaasResourcePool.getProviderId());

        IaasProject iaasProject = projectService.queryIaasProjectById(pid);
        String projectKey=iaasProject.getProjectKey();

        //???????????????????????????vdc token
        StringResponse tokenByVdcUser = null;
//        try {
//            tokenByVdcUser = oauthTokenClient.getTokenByVdcUser(authz,
//                    projectKey,
//                    ivdc.getDomainName(),ivdc.getUsername(),ivdc.getPassword());
//            //pid.toString
////                    iaasProviderVO.getDomainName(),//"vdc_JT",    //
////                    iaasProviderVO.getAuthUsername(),//"vdcjtadmin",
////                    iaasProviderVO.getAuthPassword());//"Huawei12#$");
//        } catch (Exception e) {
//            log.info("??????vdc token?????????");
//        }

//        TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO);
//
//        if (tokenResponse == null) {
//            vmMapResponse.setCode(201);
//            vmMapResponse.setMessage("???????????????token??????!");
//            return vmMapResponse;
//        }
        TokenInfoVO tokenInfoVO = null;
        RequestVO requestVO = new RequestVO();
        requestVO.setOmToken("");//omToken
        requestVO.setOcToken(tokenByVdcUser == null ? "" : tokenByVdcUser.getData());//vdc token
        requestVO.setProjectId(projectKey);


        List<IaasVirtualMachineVO> remoteVMs = new ArrayList<>();
        List<IaasVirtualMachineVO> trans = new ArrayList<>();
        try {
            baseResponse = vdcVirtualMachineClient.getVirtualMachineList(authz, requestVO);
            if(CollectionUtils.isNotEmpty(baseResponse.getData())) {
                List<VirtualMachineVO> allVMs = baseResponse.getData();

                //??????????????? ??????????????????VM
                if(CollectionUtils.isNotEmpty(removeVMListByPid)){

                    List<String> collect = removeVMListByPid.stream().map(e -> {
                        return e.getVmKey();
                    }).collect(Collectors.toList());

                    allVMs = allVMs.stream().filter(allVM -> !collect.contains(allVM.getNativeId()))
                            .collect(Collectors.toList());
                }

                for(VirtualMachineVO vo:allVMs){
                    IaasVirtualMachineVO vmvo = new IaasVirtualMachineVO();
                    String flavor = vo.getFlavor();//4U16G|4vCPU|16GB
                    String cpu = "";
                    String memory = "";
                    if (StringUtils.isNoneBlank(flavor) && flavor.split("|") != null) {
                        String split = flavor.split("\\|")[0];
                        cpu = split.substring(0, split.indexOf("U"));
                        memory = split.substring(split.indexOf("U") + 1, split.indexOf("G"));
                    }
                    double cpuUsage = StringUtils.isBlank(cpu) ? 0 : (double) (((Integer.valueOf(cpu) * vo.getCpuUsage()) / 100));
                    double memoryUsage = (double) (((Integer.valueOf(memory) * vo.getMemoryUsage()) / 100));

                    //????????????id
                    IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(vo.getAzoneName());

                    //????????????id
                    IaasHost iaasHost = sysHostMapper.queryIaasHostByKey(vo.getPhysicalHostId());
                    //.setHostId(Long.parseLong(vo.getPhysicalHostId()))
                    vmvo.setVmKey(vo.getNativeId())
                            .setVmName(vo.getName() == null ? "" : ((String) vo.getName().get("value")))
                            .setImageName(vo.getImageName())
                            .setResId(vo.resId())
                            .setOsName(vo.getOsVersion())
                            .setDiskUsage(vo.getDiskUsage())
                            .setState(vo.getStatus())
                            .setIpAddress(vo.getPrivateIps())
                            .setClusterId(iaasCluster == null ? null : iaasCluster.getId())
                            .setHostId(iaasHost == null ? null : iaasHost.getId())
                            .setPoolId(id)
                            .setProjectId(pid)
                            .setProviderId(iaasProviderVO.getId())
                            .setVcpuTotal(StringUtils.isBlank(cpu) ? 0 : Integer.valueOf(cpu))
                            .setVcpuUsed(cpuUsage)
                            .setDiskUsage(vo.getDiskUsage())
                            .setMemoryTotal(StringUtils.isBlank(memory) ? 0 : Integer.valueOf(memory))
                            .setMemoryUsed(memoryUsage)
                            .setCreateTime(DateUtil.getNow())
                            .setCreateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow())
                            .setAzoneName(vo.getAzoneName())
                            .setAzoneId(vo.getAzoneId())
                            .setUpdateUser(Sign.getUserId());
                    trans.add(vmvo);
                }
//                trans.forEach((IaasVirtualMachineVO vo)->{vo.setKey(vo.getVmKey());vo.setBinded(0);vo.setTitle(vo.getVmName());});

                vmvos.forEach((IaasVirtualMachineVO vo)->{System.out.println("local "+vo.getVmKey()+" "+vo.getBinded());});
                allVMs.forEach((VirtualMachineVO vo) ->{System.out.println("all "+vo.getNativeId());});
                trans.forEach((IaasVirtualMachineVO vo) ->{System.out.println("trans "+vo.getVmKey()+" "+vo.getBinded());});

                remoteVMs =trans.stream().filter(item -> !vmvos.stream().map(e -> e.getVmKey())
                        .collect(Collectors.toList()).contains(item.getVmKey())).collect(Collectors.toList());
                remoteVMs.forEach((IaasVirtualMachineVO vo)->{vo.setKey(vo.getVmKey());vo.setBinded(0);vo.setTitle(vo.getVmName());});

//                trans.stream().filter(item -> !vmvos.contains(item)).collect(toList());
                remoteVMs.forEach((IaasVirtualMachineVO vo) ->{System.out.println("remoteVMs "+vo.getVmKey()+" "+vo.getBinded());});
            }
            result.put("remoteVMs",remoteVMs);
            vmvos.addAll(remoteVMs);
            result.put("localVMs",vmvos);
            vmMapResponse.setData(result);
        } catch (Exception e) {
            log.error("??????????????????????????????????????????", e);
        }

        if (baseResponse == null || baseResponse.getCode() != 0 || CollectionUtils.isEmpty(baseResponse.getData())) {
            vmMapResponse.setCode(201);
            vmMapResponse.setMessage("???????????????????????????!");
            return vmMapResponse;
        }
        if (vmMapResponse == null || vmMapResponse.getCode() != 0 || MapUtils.isEmpty(vmMapResponse.getData())) {
            vmMapResponse.setCode(201);
            vmMapResponse.setMessage("???????????????????????????!");
            return vmMapResponse;
        }
//        //??????????????????
//        HostVolumesVOListResponse diskResponse = null;
//        try {
//            diskResponse = hostVolumesClient.getHostVolumesList(authz, requestVO);
//        } catch (Exception e) {
//            log.error("????????????????????????????????????????????????", e);
//        }
        return vmMapResponse;
    }

//    private TokenInfoResponse getToken(String authz, IaasProviderVO iaasProviderVO) {
//        TokenInfoResponse tokenResponse = null;
//        try {
//            tokenResponse = oauthTokenClient.getTokenAll(authz,
//                    iaasProviderVO);
//
//            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
//                return null;
//            }
//        } catch (Exception e) {
//            log.error("???????????????token??????!", e);
//            return null;
//        }
//
//        return tokenResponse;
//    }


    /**
     * ???????????????
     */
    @GetMapping("/metalSynch")
    @ApiOperation(value = "???????????????", notes = "???????????????")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "poolId", value = "?????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "projectId", value = "??????id", paramType = "query", dataType = "long")
    })
    public MetalMapResponse metalSynch( @RequestParam(name = "projectId") Long pid){ //@RequestParam(name = "poolId") Long id,
        //???????????????
        log.info("res check");
        MetalMapResponse metalMapResponse = iaasBareMetalService.nanotubeResource(pid);//id,

        if (metalMapResponse == null || metalMapResponse.getCode() != 0 || MapUtils.isEmpty(metalMapResponse.getData())) {
            metalMapResponse.setCode(201);
            metalMapResponse.setMessage("???????????????????????????!");
            return metalMapResponse;
        }
        return metalMapResponse;
    }



    /**
     * ??????????????????IaasResourcePool
     *
     * @param resourcePoolVO
     * @param resourcePool
     * @return
     */
    private boolean updateIaasResourcePool(@RequestBody ResourcePoolVO resourcePoolVO, IaasResourcePool resourcePool) {
        BeanUtils.copyProperties(resourcePoolVO, resourcePool);
        //???????????????????????????
        if (resourcePool.getId() == null) {
            return true;
        }
        //update user+time
        resourcePool.setUpdateUser(Sign.getUserId());
        resourcePool.setUpdateTime(DateUtil.getNow());
        return false;
    }

    /**
     * ????????????????????????,0 false ??????
     *
     * @param id
     * @return BaseResponse
     */
    @PutMapping("/remove")
    @ApiOperation(value = "???????????????", notes = "????????????(????????????)")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("??????????????? user id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (resourcePoolService.getById(id) != null) {
            //????????????
            resourcePoolService.updateResourcePool(id, Sign.getUserId());

            //??????poolId????????????????????????????????????
            resourcePoolService.removeBusinessGroupResourcePool(id);

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    /**
     * ????????????
     *
     * @param ids
     * @return BaseResponse
     */
    @PutMapping("/removeBatch")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????(????????????)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("????????????????????? ids???{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        //ids????????????
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //??????
            this.resourcePoolService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.resourcePoolService.updateResourcePool(Long.parseLong(id), Sign.getUserId());

                /**
                 * ???????????????????????????????????????id??????????????????ID????????????id?????????id??????????????????
                 */
//                updatePropertiesEntity(Long.parseLong(id));
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    /**
     * ???????????????????????????????????????id??????????????????ID????????????id?????????id??????????????????
     *
     * @param poolId
     */
    private void updatePropertiesEntity(Long poolId) {
        /**
         * ?????????+?????????+?????? ????????????
         */
        IaasResourcePool resourcePool1 = resourcePoolService.getById(poolId);

        //?????????

        //?????????
        if (resourcePool1 != null && resourcePool1.getProviderId() != null) {
            //?????????????????????
            if (providerService.getById(resourcePool1.getProviderId()) != null) {
                providerService.updateIaasProvider(resourcePool1.getProviderId(), Sign.getUserId());
            }
        }
        //??????
        if (resourcePool1 != null && resourcePool1.getClusterId() != null) {
            if (clusterService.getById(resourcePool1.getClusterId()) != null) {
                clusterService.updateCluster(resourcePool1.getClusterId(), Sign.getUserId());
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
    public ResponseEntity<ResourcePoolListResponse> list() {
        log.info("res list");
        List<ResourcePoolVO> resourcePoolVOList = new ArrayList<>();
        //??????
        List<IaasResourcePool> resourcePoolList = resourcePoolService.list();
        //??????????????????????????????
        if (resourcePoolList != null && resourcePoolList.size() > 0) {
            for (IaasResourcePool resourcePool : resourcePoolList) {
                ResourcePoolVO resourcePoolVO = new ResourcePoolVO();
                //?????????
                BeanUtils.copyProperties(resourcePool, resourcePoolVO);
                //????????????????????????
                resourcePoolVOList.add(resourcePoolVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResourcePoolListResponse(resourcePoolVOList));
    }


    /**
     * ??????id ?????????????????????
     *
     * @param id
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "??????id?????????????????????", notes = "??????id?????????????????????")
    @ApiImplicitParam(name = "id", value = "?????????id", paramType = "query", dataType = "long")
    public ResponseEntity<ResourcePoolAsResponse> info(@RequestParam(required = false) Long id) {
        log.info("res info");
        ResourcePoolAsResponse resourcePoolAsResponse = new ResourcePoolAsResponse();
        ResourcePoolVO resourcePoolVO = new ResourcePoolVO();
        IaasResourcePool iaasResourcePool = resourcePoolService.getById(id);

        IaasProject projects = projectService.queryIaasProjectById(iaasResourcePool.getProjectId());
        if (projects != null) {
            resourcePoolVO.setDomainName(projects.getDomainName());
            resourcePoolVO.setUsername(projects.getUsername());
            resourcePoolVO.setPassword(projects.getPassword());
            resourcePoolVO.setProjectKey(projects.getProjectKey());
        }
        if (iaasResourcePool == null) {
            return ResponseEntity.status(HttpStatus.OK).body(resourcePoolAsResponse);
        }
        BeanUtils.copyProperties(iaasResourcePool, resourcePoolVO);
        //??????????????????: ??????ClusterId????????????sum(cpuToTal)
        String cpuTotal = "0";
        if (iaasResourcePool.getClusterId() != null) {
            cpuTotal = hostService.getSumByClusterId(iaasResourcePool.getClusterId());
        }
        //??????poolid?????? ?????????ids
        List<SysBusinessGroupResourcePoolVO> businessGroupResourcePoolVOS = resourcePoolService.getBusinessGroupNameByPoolId(id);
        if (businessGroupResourcePoolVOS.size() > 0) {
            resourcePoolVO.setBusinessIds(businessGroupResourcePoolVOS.stream().map(SysBusinessGroupResourcePoolVO::getBusinessIds).collect(Collectors.joining(",")));
            resourcePoolVO.setBusinessNames(businessGroupResourcePoolVOS.stream().map(SysBusinessGroupResourcePoolVO::getBusinessGroupName).collect(Collectors.joining(",")));
        }
        resourcePoolVO.setCpuTotal(cpuTotal);
        resourcePoolAsResponse.setData(resourcePoolVO);
        return ResponseEntity.status(HttpStatus.OK).body(resourcePoolAsResponse);
    }

    @GetMapping("/getResourceSumInfoById")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????id(??????id)", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "poolId", value = "?????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "providerId", value = "???????????????id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<IaasClusterResponse> getResourceSumInfoById(@RequestParam(required = false) Long id,
                                                                      @RequestParam(required = true) Long poolId,
                                                                      @RequestParam(required = true) Long providerId) {
        log.info("res sum");
        IaasClusterResponse clusterResponse = new IaasClusterResponse();
        IaasClusterVo clusterVo = new IaasClusterVo();
        //??????id?????????
        clusterVo.setId(id);

        if (poolId != null) {
            clusterVo.setPoolId(poolId);
        }
        if (providerId != null) {
            clusterVo.setProviderId(providerId);
        }
        clusterVo = resourcePoolService.getResourceSumInfoById(clusterVo);
        if (clusterVo == null) {
            return ResponseEntity.status(HttpStatus.OK).body(clusterResponse);
        }
        clusterResponse.setData(clusterVo);
        return ResponseEntity.status(HttpStatus.OK).body(clusterResponse);
    }

    /**
     * ????????? ????????????
     *
     * @param businessGroupName
     * @return
     */
    @GetMapping("/getBusinessGroupName")
    @ApiOperation(value = "????????? ??????", notes = "????????? ??????")
    @ApiImplicitParam(name = "businessGroupName", value = "????????? ??????", paramType = "query", dataType = "string")
    public ResponseEntity<SysBusinessGroupListResponse> getBusinessGroupName(@RequestParam(required = false) String
                                                                                     businessGroupName) {
        log.info("res groupName");
        SysBusinessGroupListResponse sysBusinessGroupListResponse = new SysBusinessGroupListResponse();
        List<SysBusinessGroupVO> businessGroupVOS = resourcePoolService.getBusinessGroupName(businessGroupName);
        if (businessGroupVOS.size() > 0) {
            sysBusinessGroupListResponse.setData(businessGroupVOS);
        }
        //????????????
        return ResponseEntity.status(HttpStatus.OK).body(sysBusinessGroupListResponse);
    }

    /**
     * ????????? ????????????
     *
     * @param
     * @return
     */
    @GetMapping("/get_resource_pool")
    @ApiOperation(value = "???????????????id?????????????????????", notes = "???????????????id?????????????????????")
    @ApiImplicitParam(name = "id", value = "????????? ??????", paramType = "query", dataType = "long")
    public ResponseEntity<ResourcePoolListResponse> getResourcePoolList(@RequestParam(required = false) Long id) {

        ResourcePoolListResponse poolListResponse = new ResourcePoolListResponse();
        List<ResourcePoolVO> poolByGroupId = resourcePoolService.getPoolByGroupId(id);
        if (poolByGroupId.size() > 0) {
            poolListResponse.setData(poolByGroupId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(poolListResponse);
    }


    @GetMapping("/get_vdc_name")
    @ApiOperation(value = "????????????????????????vdc??????", notes = "????????????????????????vdc??????")
    @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "long")
    public ResponseEntity<VirtualDataCenterListResponse> getVdcName(@RequestParam(required = false) Long userId) {

        VirtualDataCenterListResponse virtualDataCenterListResponse = new VirtualDataCenterListResponse();
        List<IaasVirtualDataCenterVO> vdcList = projectService.queryVdcName(userId);
        if (vdcList.size() > 0) {
            virtualDataCenterListResponse.setData(vdcList);
        }
        return ResponseEntity.status(HttpStatus.OK).body(virtualDataCenterListResponse);
    }


    @GetMapping("/group_cascade/{userId}")
    @ApiOperation(value = "????????????id??????????????????", notes = "????????????id??????????????????")
    @ApiImplicitParam(name = "userId", value = "??????id", required = true, paramType = "path")
    public List<BusinessGroupCascade> queryProjectByGroupId(@PathVariable(name = "userId") long userId) {

        return projectService.queryProjectByGroupId(userId);
    }

    @GetMapping("/get_all_bygroup/{gropuId}")
    @ApiOperation(value = "???????????????id????????????", notes = "???????????????id????????????")
    @ApiImplicitParam(name = "gropuId", value = "?????????id", required = true, paramType = "path")
    public BppvVO queryProjectAllByGroupId(@PathVariable(name = "gropuId") long gropuId) {

        return projectService.queryAllByGroupId(gropuId);
    }

}
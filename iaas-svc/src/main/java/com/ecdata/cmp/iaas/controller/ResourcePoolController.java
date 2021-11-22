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
 * @description: 资源池 控制层
 * @Date: 2019/11/18 4:01 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/resourcePool")
@Api(tags = "🕊资源池相关的API")
public class ResourcePoolController {
    /**
     * 资源池(resourcePool) service
     */
    @Autowired
    private IResourcePoolService resourcePoolService;

    /**
     * 资源池存储(resourcePoolDatastore) service
     */
    @Autowired
    private IResourcePoolDatastoreService resourcePoolDatastoreService;

    /**
     * 供应商(provider) service
     */
    @Autowired
    private IIaasProviderService providerService;

    /**
     * 集群(cluster) service
     */
    @Autowired
    private IClusterService clusterService;

    /**
     * 主机(host) service
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
     * User模块：调用根据id查询用户信息
     */
//    @Autowired
//    private UserClient userClient;

    /**
     * User模块：调用业务组方法（disName+list by id）
     */
//    @Autowired
//    private SysBusinessGroupClient sysBusinessGroupClient;


    /**
     * 分页查询资源池管理信息（连接池分页列表）
     *
     * @param pageNo   当前页
     * @param pageSize 每页的数量
     * @param keyword  关键字
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询资源池管理信息", notes = "分页查询资源池管理信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "businessIds", value = "业务组ids字符串,如: 1,2,3", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ResourcePoolPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) String businessIds) {
        Page<ResourcePoolVO> page = new Page<>(pageNo, pageSize);
        log.info("res page");
        //调用分页查询方法
        ResourcePoolVO resourcePoolVO = new ResourcePoolVO();
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        if (businessIds != null && businessIds != "") {
            params.put("businessIds", businessIds.split(","));
        }
        IPage<ResourcePoolVO> result = resourcePoolService.queryResourcePoolPage(page, params);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new ResourcePoolPageResponse(new PageVO<>(result)));
    }


    /**
     * 新增:/v1/resourcePool/add
     *
     * @param resourcePoolVO
     * @return BaseResponse
     * @throws Exception
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增资源池", notes = "新增资源池")
    public ResponseEntity<ResourcePoolAsResponse> add(@RequestBody ResourcePoolVO resourcePoolVO) throws Exception {
        IaasResourcePool resourcePool = new IaasResourcePool();
        //拓展类属性复制给 IaasResourcePool
        BeanUtils.copyProperties(resourcePoolVO, resourcePool);
        //赋值ID，先获取封装的id
        Long poolId = SnowFlakeIdGenerator.getInstance().nextId();
        resourcePool.setId(poolId);
        //createUser+time
        resourcePool.setCreateUser(Sign.getUserId());
        resourcePool.setCreateTime(DateUtil.getNow());
        //连接池不能为空，默认赋值
        if (resourcePoolVO.getVcpuTotalAllocate() == null) {
            resourcePoolVO.setVcpuTotalAllocate(0);
        }

        //业务资源池中间表
        String[] businessIds = null;
        if (resourcePoolVO.getBusinessIds() != null) {
            businessIds = resourcePoolVO.getBusinessIds().split(",");
            SysBusinessGroupResourcePoolVO businessGroupResourcePool = null;
            for (String businessId : businessIds) {
                businessGroupResourcePool = new SysBusinessGroupResourcePoolVO();
                businessGroupResourcePool.setId(SnowFlakeIdGenerator.getInstance().nextId());
                businessGroupResourcePool.setBusinessGroupId(Long.parseLong(businessId));
                //0 user模块,1 as模块， 2pa模块，
                businessGroupResourcePool.setType(1);
                businessGroupResourcePool.setPoolId(poolId);
                businessGroupResourcePool.setCreateUser(Sign.getUserId());
                businessGroupResourcePool.setCreateTime(DateUtil.getNow());
                resourcePoolService.addBusinessGroupResourcePool(businessGroupResourcePool);
            }
        }

        //响应基础对象
        ResourcePoolAsResponse resourcePoolAsResponse = null;
        //新增：成功、失败
        if (resourcePoolService.save(resourcePool)) {
            resourcePoolAsResponse = new ResourcePoolAsResponse();
            //返回id使用
            BeanUtils.copyProperties(resourcePool, resourcePoolVO);
            resourcePoolAsResponse.setData(resourcePoolVO);
            resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            resourcePoolAsResponse.setMessage("添加成功");

            //同步项目下的虚拟机
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
                log.info("同步虚拟机完成！");
                if (baseResponse != null && baseResponse.getCode() == 0) {
                    resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    resourcePoolAsResponse.setMessage("保存资源池信息和同步虚拟机成功！");
                } else {
                    resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    resourcePoolAsResponse.setMessage("同步虚拟机信息失败！");
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resourcePoolAsResponse);
        } else {
            resourcePoolAsResponse = new ResourcePoolAsResponse();
            resourcePoolAsResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            resourcePoolAsResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourcePoolAsResponse);
        }
    }

    /**
     * 保存IaasResourcePool
     *
     * @param resourcePoolVO
     * @param resourcePool
     */
    private void saveIaasResourcePool(@RequestBody ResourcePoolVO resourcePoolVO, IaasResourcePool resourcePool) {

    }


    /**
     * 修改：/resourcePool/update
     *
     * @param resourcePoolVO
     * @return BaseResponse
     * @throws Exception
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改资源池", notes = "修改资源池")
    public ResponseEntity<BaseResponse> update(@RequestBody ResourcePoolVO resourcePoolVO) throws Exception {
        log.info("res update");
        //连接池对象
        IaasResourcePool resourcePool = new IaasResourcePool();
        /**
         * 连接池修改：IaasResourcePool
         */
        if (updateIaasResourcePool(resourcePoolVO, resourcePool))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));

        //业务资源池中间表(修改）
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

            //根据poolId先删除中间表数据，再新增(物理删除)
            resourcePoolService.removeBusinessGroupResourcePool(resourcePoolVO.getId());

            for (String businessId : businessIds) {
                SysBusinessGroupResourcePoolVO businessGroupResourcePool = new SysBusinessGroupResourcePoolVO();
                businessGroupResourcePool.setId(SnowFlakeIdGenerator.getInstance().nextId());
                businessGroupResourcePool.setBusinessGroupId(Long.parseLong(businessId));
                //0 user模块,1 as模块， 2pa模块，
                businessGroupResourcePool.setType(1);
                businessGroupResourcePool.setPoolId(resourcePoolVO.getId());
                businessGroupResourcePool.setCreateUser(Sign.getUserId());
                businessGroupResourcePool.setCreateTime(DateUtil.getNow());
                resourcePoolService.addBusinessGroupResourcePool(businessGroupResourcePool);
            }
        }

        //响应
        BaseResponse baseResponse = null;
        if (resourcePoolService.updateById(resourcePool)) {
            //同步项目下的虚拟机
            if ("2".equals(resourcePoolVO.getType())) {
                String cloudRes = "";
                if(resourcePoolVO.getVmOrMetal()==0) {
                    baseResponse = syncProviderService.addVMByVmKeyList(resourcePoolVO, removeBusinessIds);
                    cloudRes = "虚拟机";
                    log.info("同步虚拟机完成！");
                }
                else if(resourcePoolVO.getVmOrMetal()==1) {
                    baseResponse = iaasBareMetalService.syncUpdate(resourcePoolVO);
                    cloudRes = "裸金属";
                    log.info("同步裸金属完成！");
                }
                //baseResponse = syncProviderService.syncProjectVM(vmRequest);
                if (baseResponse != null && baseResponse.getCode() == 0) {
                    baseResponse.setMessage("保存资源池信息和同步"+cloudRes+"成功！");
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                } else {
                    baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    baseResponse.setMessage("同步"+cloudRes+"信息失败！");
                }
            }
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    /**
     * 拆开来的同步虚拟机，用户查看列表勾选
     * getTokenNew
     * projectId有问题，不能这样获取还是要provider中的projectName关联
     */
    @GetMapping("/synchCheck")
    @ApiOperation(value = "用户选择虚拟机同步", notes = "用户选择虚拟机同步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "poolId", value = "资源池id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "long")
    })
    public VMMapResponse synchCheck(@RequestParam(name = "poolId") Long id,
            @RequestParam(name = "projectId") Long pid) throws Exception {
        //连接池对象
        log.info("res check");
        VMMapResponse vmMapResponse =  new VMMapResponse();
        Map<String,Object> result = new HashMap<>();
//    resPool -> 供应商  -> vdc -> 项目 -> 虚拟机


        VDCVirtualMachineListResponse baseResponse = null;
        String authz=AuthContext.getAuthz();
        Map<String ,Object> map = new HashMap<>();
        map.put("poolId",id);
        //本地库
        List<IaasVirtualMachineVO> vmvos = virtualMachineService.getVirtualMachineVOPage(map);
        if(CollectionUtils.isNotEmpty(vmvos)){
            vmvos.forEach((IaasVirtualMachineVO vo)->{vo.setKey(vo.getVmKey());vo.setBinded(1);vo.setTitle(vo.getVmName());});
        }else{
            vmMapResponse.setCode(201);
            vmMapResponse.setMessage("本地暂无虚拟机!");
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

        //通过用户名密码获取vdc token
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
//            log.info("获取vdc token错误！");
//        }

//        TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO);
//
//        if (tokenResponse == null) {
//            vmMapResponse.setCode(201);
//            vmMapResponse.setMessage("获取供应商token错误!");
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

                //过滤被其他 资源池绑定的VM
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

                    //获取集群id
                    IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(vo.getAzoneName());

                    //获取主机id
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
            log.error("获取项目下的虚拟机信息异常！", e);
        }

        if (baseResponse == null || baseResponse.getCode() != 0 || CollectionUtils.isEmpty(baseResponse.getData())) {
            vmMapResponse.setCode(201);
            vmMapResponse.setMessage("远程虚拟机暂无数据!");
            return vmMapResponse;
        }
        if (vmMapResponse == null || vmMapResponse.getCode() != 0 || MapUtils.isEmpty(vmMapResponse.getData())) {
            vmMapResponse.setCode(201);
            vmMapResponse.setMessage("获取虚拟机信息失败!");
            return vmMapResponse;
        }
//        //获取磁盘信息
//        HostVolumesVOListResponse diskResponse = null;
//        try {
//            diskResponse = hostVolumesClient.getHostVolumesList(authz, requestVO);
//        } catch (Exception e) {
//            log.error("获取项目下的虚拟机磁盘信息异常！", e);
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
//            log.error("获取供应商token错误!", e);
//            return null;
//        }
//
//        return tokenResponse;
//    }


    /**
     * 裸金属纳管
     */
    @GetMapping("/metalSynch")
    @ApiOperation(value = "裸金属纳管", notes = "裸金属纳管")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "poolId", value = "资源池id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "long")
    })
    public MetalMapResponse metalSynch( @RequestParam(name = "projectId") Long pid){ //@RequestParam(name = "poolId") Long id,
        //连接池对象
        log.info("res check");
        MetalMapResponse metalMapResponse = iaasBareMetalService.nanotubeResource(pid);//id,

        if (metalMapResponse == null || metalMapResponse.getCode() != 0 || MapUtils.isEmpty(metalMapResponse.getData())) {
            metalMapResponse.setCode(201);
            metalMapResponse.setMessage("获取裸金属信息失败!");
            return metalMapResponse;
        }
        return metalMapResponse;
    }



    /**
     * 连接池修改：IaasResourcePool
     *
     * @param resourcePoolVO
     * @param resourcePool
     * @return
     */
    private boolean updateIaasResourcePool(@RequestBody ResourcePoolVO resourcePoolVO, IaasResourcePool resourcePool) {
        BeanUtils.copyProperties(resourcePoolVO, resourcePool);
        //响应失败，缺少主键
        if (resourcePool.getId() == null) {
            return true;
        }
        //update user+time
        resourcePool.setUpdateUser(Sign.getUserId());
        resourcePool.setUpdateTime(DateUtil.getNow());
        return false;
    }

    /**
     * 删除（逻辑删除）,0 false 删除
     *
     * @param id
     * @return BaseResponse
     */
    @PutMapping("/remove")
    @ApiOperation(value = "删除资源池", notes = "删除用户(逻辑删除)")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除资源池 user id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (resourcePoolService.getById(id) != null) {
            //逻辑删除
            resourcePoolService.updateResourcePool(id, Sign.getUserId());

            //根据poolId删除业务资源池中间表数据
            resourcePoolService.removeBusinessGroupResourcePool(id);

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return BaseResponse
     */
    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除资源池", notes = "批量删除资源池(逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除资源池 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        //ids参数为空
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //删除
            this.resourcePoolService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.resourcePoolService.updateResourcePool(Long.parseLong(id), Sign.getUserId());

                /**
                 * 逻辑删除其他属性对象：根据id查询：业务组ID，供应商id，集群id，再逻辑删除
                 */
//                updatePropertiesEntity(Long.parseLong(id));
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    /**
     * 逻辑删除其他属性对象：根据id查询：业务组ID，供应商id，集群id，再逻辑删除
     *
     * @param poolId
     */
    private void updatePropertiesEntity(Long poolId) {
        /**
         * 业务组+供应商+集群 逻辑删除
         */
        IaasResourcePool resourcePool1 = resourcePoolService.getById(poolId);

        //业务层

        //供应商
        if (resourcePool1 != null && resourcePool1.getProviderId() != null) {
            //判断是否存在：
            if (providerService.getById(resourcePool1.getProviderId()) != null) {
                providerService.updateIaasProvider(resourcePool1.getProviderId(), Sign.getUserId());
            }
        }
        //集群
        if (resourcePool1 != null && resourcePool1.getClusterId() != null) {
            if (clusterService.getById(resourcePool1.getClusterId()) != null) {
                clusterService.updateCluster(resourcePool1.getClusterId(), Sign.getUserId());
            }
        }
    }

    /**
     * 获取资源池列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取资源池列表", notes = "获取资源池列表")
    public ResponseEntity<ResourcePoolListResponse> list() {
        log.info("res list");
        List<ResourcePoolVO> resourcePoolVOList = new ArrayList<>();
        //查询
        List<IaasResourcePool> resourcePoolList = resourcePoolService.list();
        //不为空，复制到拓展类
        if (resourcePoolList != null && resourcePoolList.size() > 0) {
            for (IaasResourcePool resourcePool : resourcePoolList) {
                ResourcePoolVO resourcePoolVO = new ResourcePoolVO();
                //资源池
                BeanUtils.copyProperties(resourcePool, resourcePoolVO);
                //封装返回的大对象
                resourcePoolVOList.add(resourcePoolVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResourcePoolListResponse(resourcePoolVOList));
    }


    /**
     * 根据id 获取资源池信息
     *
     * @param id
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "根据id获取资源池信息", notes = "根据id获取资源池信息")
    @ApiImplicitParam(name = "id", value = "资源池id", paramType = "query", dataType = "long")
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
        //查询物理内存: 通过ClusterId查询主机sum(cpuToTal)
        String cpuTotal = "0";
        if (iaasResourcePool.getClusterId() != null) {
            cpuTotal = hostService.getSumByClusterId(iaasResourcePool.getClusterId());
        }
        //根据poolid查询 业务组ids
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
    @ApiOperation(value = "获取资源入口信息", notes = "获取资源入口信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资源入口id(集群id)", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "poolId", value = "资源池id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "providerId", value = "云平台入口id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<IaasClusterResponse> getResourceSumInfoById(@RequestParam(required = false) Long id,
                                                                      @RequestParam(required = true) Long poolId,
                                                                      @RequestParam(required = true) Long providerId) {
        log.info("res sum");
        IaasClusterResponse clusterResponse = new IaasClusterResponse();
        IaasClusterVo clusterVo = new IaasClusterVo();
        //集群id必须传
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
     * 业务组 名称查询
     *
     * @param businessGroupName
     * @return
     */
    @GetMapping("/getBusinessGroupName")
    @ApiOperation(value = "业务组 名称", notes = "业务组 名称")
    @ApiImplicitParam(name = "businessGroupName", value = "业务组 名称", paramType = "query", dataType = "string")
    public ResponseEntity<SysBusinessGroupListResponse> getBusinessGroupName(@RequestParam(required = false) String
                                                                                     businessGroupName) {
        log.info("res groupName");
        SysBusinessGroupListResponse sysBusinessGroupListResponse = new SysBusinessGroupListResponse();
        List<SysBusinessGroupVO> businessGroupVOS = resourcePoolService.getBusinessGroupName(businessGroupName);
        if (businessGroupVOS.size() > 0) {
            sysBusinessGroupListResponse.setData(businessGroupVOS);
        }
        //响应对象
        return ResponseEntity.status(HttpStatus.OK).body(sysBusinessGroupListResponse);
    }

    /**
     * 业务组 名称查询
     *
     * @param
     * @return
     */
    @GetMapping("/get_resource_pool")
    @ApiOperation(value = "根据业务组id获取资源池列表", notes = "根据业务组id获取资源池列表")
    @ApiImplicitParam(name = "id", value = "业务组 名称", paramType = "query", dataType = "long")
    public ResponseEntity<ResourcePoolListResponse> getResourcePoolList(@RequestParam(required = false) Long id) {

        ResourcePoolListResponse poolListResponse = new ResourcePoolListResponse();
        List<ResourcePoolVO> poolByGroupId = resourcePoolService.getPoolByGroupId(id);
        if (poolByGroupId.size() > 0) {
            poolListResponse.setData(poolByGroupId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(poolListResponse);
    }


    @GetMapping("/get_vdc_name")
    @ApiOperation(value = "根据当前用户查询vdc列表", notes = "根据当前用户查询vdc列表")
    @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", dataType = "long")
    public ResponseEntity<VirtualDataCenterListResponse> getVdcName(@RequestParam(required = false) Long userId) {

        VirtualDataCenterListResponse virtualDataCenterListResponse = new VirtualDataCenterListResponse();
        List<IaasVirtualDataCenterVO> vdcList = projectService.queryVdcName(userId);
        if (vdcList.size() > 0) {
            virtualDataCenterListResponse.setData(vdcList);
        }
        return ResponseEntity.status(HttpStatus.OK).body(virtualDataCenterListResponse);
    }


    @GetMapping("/group_cascade/{userId}")
    @ApiOperation(value = "根据用户id查出级联信息", notes = "根据用户id查出级联信息")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path")
    public List<BusinessGroupCascade> queryProjectByGroupId(@PathVariable(name = "userId") long userId) {

        return projectService.queryProjectByGroupId(userId);
    }

    @GetMapping("/get_all_bygroup/{gropuId}")
    @ApiOperation(value = "根据业务组id查出信息", notes = "根据业务组id查出信息")
    @ApiImplicitParam(name = "gropuId", value = "业务组id", required = true, paramType = "path")
    public BppvVO queryProjectAllByGroupId(@PathVariable(name = "gropuId") long gropuId) {

        return projectService.queryAllByGroupId(gropuId);
    }

}
package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasProvider;
import com.ecdata.cmp.iaas.entity.dto.ContainerImageResourceVO;
import com.ecdata.cmp.iaas.entity.dto.HuaWeiContainerImageResourceVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.request.VDCRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VMRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VSphereRequest;
import com.ecdata.cmp.iaas.entity.dto.response.IaasContainerResourceResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProviderListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProviderPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProviderResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ProviderResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.cascade.ProviderCascade;
import com.ecdata.cmp.iaas.schedule.SyncMonitor;
import com.ecdata.cmp.iaas.service.IIaasProviderService;
import com.ecdata.cmp.iaas.service.ProviderService;
import com.ecdata.cmp.user.client.UserClient;
import com.ecdata.cmp.user.dto.response.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: Provider controller
 * @Author: shig
 * @description: 供应商  控制层
 * @Date: 2019/11/12 5:37 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/provider")
@Api(tags = "🐬供应商 相关的API")
public class IaasProviderController {

    /**
     * 供应商 (iaasProvider) service
     */
    @Autowired
    private IIaasProviderService iaasProviderService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private SyncMonitor syncMonitor;

    @Autowired
    private UserClient userClient;


    /**
     * /iaasProvider/addIaasProvider
     *
     * @param iaasProviderVO
     * @return BaseResponse
     * @throws Exception
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增供应商 ", notes = "新增供应商 ")
    public ResponseEntity<BaseResponse> add(@RequestBody IaasProviderVO iaasProviderVO) throws Exception {
        //响应基础对象
        BaseResponse baseResponse = new BaseResponse();

        //校验数据库是否存在
        IaasProvider iaasProvider = new IaasProvider();
        //address+type校验:address存ip地址，静态模版已最新的为准。
        if (checkProByTypeAdress(iaasProviderVO, baseResponse, iaasProvider))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);

        //拓展类属性复制给 IaasProvider
        BeanUtils.copyProperties(iaasProviderVO, iaasProvider);

        //赋值ID，先获取封装的id
        iaasProvider.setId(SnowFlakeIdGenerator.getInstance().nextId());
        //createUser+time
        iaasProvider.setCreateUser(Sign.getUserId());
        iaasProvider.setCreateTime(DateUtil.getNow());
        //isDeleted:0 未删除数据，boolean false 0，默认为 0

        //新增：成功、失败
        if (iaasProviderService.save(iaasProvider)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    private boolean checkProByTypeAdress(@RequestBody IaasProviderVO iaasProviderVO, BaseResponse baseResponse, IaasProvider iaasProvider) {
        iaasProvider.setAddress(iaasProviderVO.getAddress());
        iaasProvider.setType(iaasProviderVO.getType());
        IaasProvider iaasProvider1 = iaasProviderService.getInfoByProvider(iaasProvider);
        if (iaasProvider1 != null) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加失败(该供应商下的地址已存在)!");
            return true;
        }
        return false;
    }

    /**
     * /v1/provider/check
     *
     * @param iaasProviderVO
     * @return BaseResponse
     * @throws Exception
     */
    @PostMapping("/check")
    @ApiOperation(value = "校验供应商 ", notes = "校验供应商 ")
    public ResponseEntity<BaseResponse> check(@RequestBody IaasProviderVO iaasProviderVO) throws Exception {
        //响应基础对象
        BaseResponse baseResponse = new BaseResponse();
        if (iaasProviderVO.getType() == 2) {//调用华为云供应商验证是否通过
            if (StringUtils.isBlank(iaasProviderVO.getUsername()) || StringUtils.isBlank(iaasProviderVO.getPassword())) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("用户名或密码不能为空！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
        } else if (iaasProviderVO.getType() == 3) {
            if (StringUtils.isBlank(iaasProviderVO.getUsername()) || StringUtils.isBlank(iaasProviderVO.getPassword()) || StringUtils.isBlank(iaasProviderVO.getDomainName())) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("用户名/密码/vdc域名不能为空！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
        }
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("用户名密码校验成功！");
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);

//        /**
//         * 校验接口调用： 成功返回true，失败false
//         * type：1   vcent调用python
//         *       2  华为云调用
//         */
//        boolean status = checkManyMethod(iaasProviderVO);
//
//
//        if (status) {
//            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
//            baseResponse.setMessage("用户名密码校验成功！");
//            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
//        } else {
//            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
//            baseResponse.setMessage("用户名密码校验失败！");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
//        }
    }

//    /**
//     * 校验接口调用： 成功返回true，失败false
//     *
//     * @param iaasProviderVO
//     * @return
//     */
//    private boolean checkManyMethod(@RequestBody IaasProviderVO iaasProviderVO) {
//        if (iaasProviderVO.getType() != null) {
//            //调用vcent是否通过
//            if (iaasProviderVO.getType() == 1) {
//                return false;
//            } else if (iaasProviderVO.getType() == 2) {//调用华为云供应商验证是否通过
//                return StringUtils.isBlank(providerService.verificationUsernameAndPwd(iaasProviderVO, 2)) ? false : true;
//            } else if (iaasProviderVO.getType() == 3) {//调用华为云vdc验证是否通过
//                return StringUtils.isBlank(providerService.verificationUsernameAndPwd(iaasProviderVO, 3)) ? false : true;
//            }
//        }
//        return false;
//    }

    /**
     * /iaasProvider/updateIaasProvider
     *
     * @param iaasProviderVO
     * @return BaseResponse
     * @throws Exception
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改供应商 ", notes = "修改供应商 ")
    public ResponseEntity<BaseResponse> update(@RequestBody IaasProviderVO iaasProviderVO) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        IaasProvider iaasProvider = new IaasProvider();
        BeanUtils.copyProperties(iaasProviderVO, iaasProvider);
        //响应失败，缺少主键
        if (iaasProvider.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        //update user+time
        iaasProvider.setUpdateUser(Sign.getUserId());
        iaasProvider.setUpdateTime(DateUtil.getNow());

        if (iaasProviderService.updateById(iaasProvider)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    /**
     * 删除（逻辑删除）,0 false 删除
     *
     * @param id
     * @return BaseResponse
     */
    @PutMapping("/remove")
    @ApiOperation(value = "删除供应商 ", notes = "删除用户(逻辑删除)")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除供应商  user id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (iaasProviderService.getById(id).getProviderName() != null) {
            //逻辑删除
            iaasProviderService.updateIaasProvider(id, Sign.getUserId());
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
    @ApiOperation(value = "批量删除供应商 ", notes = "批量删除供应商 (逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除供应商  ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        //ids参数为空
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //删除
            this.iaasProviderService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.iaasProviderService.updateIaasProvider(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    /**
     * 获取供应商 列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取供应商 列表", notes = "获取供应商 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型(1:vSphere,2:华为云)", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasProviderListResponse> list(@RequestParam(required = false) Long type,
                                                         @RequestParam(required = false) String keyword) {
        //查询条件type
        IaasProviderVO providerVO = new IaasProviderVO();
        Map<String, Object> map = new HashMap<>();
        if (type != null) {
            map.put("type", type);
        }
        if (keyword != null) {
            map.put("keyword", keyword);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasProviderListResponse(iaasProviderService.providerlistByMap(map)));
    }


    /**
     * 根据id 获取供应商 信息
     *
     * @param id
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取供应商 信息", notes = "获取供应商 信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "供应商 id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "type", value = "类型(1:vSphere,2:华为云)", paramType = "query", dataType = "int", defaultValue = "1")
    })
    public ResponseEntity<IaasProviderResponse> info(@RequestParam(required = true) Long id,
                                                     @RequestParam(defaultValue = "1", required = true) Long type) {
        IaasProviderResponse iaasProviderResponse = new IaasProviderResponse();
        IaasProviderVO iaasProviderVO = new IaasProviderVO();
        //查询条件
        IaasProvider provider = new IaasProvider();
        if (id == null) {
            id = Sign.getUserId();
        }
        if (type != null) {
            provider.setType(type);
        }
        provider.setId(id);
        //查询改id是否存在
        IaasProvider iaasProvider = iaasProviderService.getById(id);
        if (iaasProvider == null) {
            return ResponseEntity.status(HttpStatus.OK).body(iaasProviderResponse);
        }
        BeanUtils.copyProperties(iaasProvider, iaasProviderVO);
        iaasProviderResponse.setData(iaasProviderVO);
        return ResponseEntity.status(HttpStatus.OK).body(iaasProviderResponse);
    }

    /**
     * 分页查询供应商
     *
     * @param pageNo   当前页
     * @param pageSize 每页的数量
     * @param keyword  关键字
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查看供应商 ", notes = "分页查看供应商 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "type", value = "类型(1:vSphere,2:华为云)", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasProviderPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(defaultValue = "1", required = true) String type,
                                                         @RequestParam(required = false) String keyword) {
        Page<IaasProviderVO> page = new Page<>(pageNo, pageSize);
        //调用分页查询方法
        IPage<IaasProviderVO> result = iaasProviderService.queryIaasProviderPage(page, keyword, type);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasProviderPageResponse(new PageVO<>(result)));
    }

    /**
     * 查询供应商 区域，供应商，主机等信息
     *
     * @param id
     * @return
     */
    @GetMapping("/queryIaasProviderInfoById")
    @ApiOperation(value = "查询供应商 区域，供应商，主机等信息", notes = "查询供应商 区域，供应商，主机等信息")
    @ApiImplicitParam(name = "id", value = "供应商 id", paramType = "query", dataType = "long")
    public ResponseEntity<IaasContainerResourceResponse> queryIaasProviderInfo(@RequestParam(required = false) Long id) {
        IaasContainerResourceResponse iaasContainerResourceResponse = new IaasContainerResourceResponse();
        ContainerImageResourceVO containerImageResourceVO = new ContainerImageResourceVO();
        if (id == null) {
            id = Sign.getUserId();
        }
        IaasProviderVO iaasProviderVO = new IaasProviderVO();
        iaasProviderVO.setId(id);
        //查询改id是否存在
        ContainerImageResourceVO containerImageResourceExist = iaasProviderService.queryIaasProviderInfo(iaasProviderVO);
        if (containerImageResourceExist == null) {
            return ResponseEntity.status(HttpStatus.OK).body(iaasContainerResourceResponse);
        }
        BeanUtils.copyProperties(containerImageResourceExist, iaasContainerResourceResponse);
        iaasContainerResourceResponse.setData(containerImageResourceExist);
        return ResponseEntity.status(HttpStatus.OK).body(iaasContainerResourceResponse);
    }

    /**
     * 查询华为资源信息
     *
     * @param id
     * @return
     */
    @GetMapping("/queryHuaWeiIaasProviderInfoById")
    @ApiOperation(value = "查询华为资源信息", notes = "查询华为资源信息")
    @ApiImplicitParam(name = "id", value = "供应商id", paramType = "query", dataType = "long")
    public ResponseEntity<HuaWeiContainerImageResourceVO> queryHuaWeiResourceInfo(@RequestParam(required = false) Long id) {
        HuaWeiContainerImageResourceVO containerImageResourceExist = iaasProviderService.queryHuaWeiResourceInfo(id);
        if (containerImageResourceExist == null) {
            return ResponseEntity.status(HttpStatus.OK).body(containerImageResourceExist);
        }
        return ResponseEntity.status(HttpStatus.OK).body(containerImageResourceExist);
    }

    /**
     * 查询供华为供应商 区域，集群信息
     *
     * @param id
     * @return
     */
    @GetMapping("/query_huawei_provider_info")
    @ApiOperation(value = "查询供华为供应商 区域，集群信息", notes = "查询供华为供应商 区域，集群信息")
    @ApiImplicitParam(name = "id", value = "供应商id", paramType = "query", dataType = "long")
    public ResponseEntity<ProviderResponse> queryIaasProviderHuaWeiInfo(@RequestParam Long id) {
        IaasProviderVO iaasProviderVO = new IaasProviderVO();
        iaasProviderVO.setId(id);
        //查询改id是否存在
        ProviderResponse providerResponse = iaasProviderService.queryIaasProviderHuaWeiInfo(iaasProviderVO);
        if (providerResponse == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(providerResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(providerResponse);
    }

    /**
     * 查询VDC信息
     *
     * @param providerId
     * @return
     */
    @GetMapping("/queryIaasVDCInfoByProviderId")
    @ApiOperation(value = "查询VDC信息", notes = "查询VDC信息")
    @ApiImplicitParam(name = "providerId", value = "供应商 id", paramType = "query", dataType = "long")
    public ResponseEntity<List<IaasVirtualDataCenterVO>> queryIaasVDCInfoByProviderId(@RequestParam Long providerId) {
        //查询改id是否存在
        List<IaasVirtualDataCenterVO> vdcList = iaasProviderService.queryIaasVDCInfoByProviderId(providerId);

        return ResponseEntity.status(HttpStatus.OK).body(vdcList);
    }

    /**
     * 供应商 名称去重
     *
     * @param providerName
     * @return
     */
    @GetMapping("/disProviderName")
    @ApiOperation(value = "供应商 名称", notes = "供应商 名称")
    @ApiImplicitParam(name = "providerName", value = "供应商 名称", paramType = "query", dataType = "string")
    public ResponseEntity<IaasProviderListResponse> disProviderName(@RequestParam(required = false) String providerName) {
        //去重查询
        List<IaasProvider> iaasProviderList = iaasProviderService.disProviderName(providerName);
        //响应对象
        List<IaasProviderVO> iaasProviderVOList = new ArrayList<>();
        //不为空，复制到拓展类
        if (iaasProviderList != null && iaasProviderList.size() > 0) {
            for (IaasProvider iaasProvider : iaasProviderList) {
                IaasProviderVO iaasProviderVO = new IaasProviderVO();
                BeanUtils.copyProperties(iaasProvider, iaasProviderVO);
                iaasProviderVOList.add(iaasProviderVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasProviderListResponse(iaasProviderVOList));
    }

    @PostMapping("/syncData")
    @ApiOperation(value = "同步数据 ", notes = "同步数据 ")
    public ResponseEntity<BaseResponse> syncData(@RequestBody VSphereRequest request) throws Exception {
        BaseResponse baseResponse = providerService.syncVSphereData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/sync_huawei_data")
    @ApiOperation(value = "同步华为数据 ", notes = "同步数据 ")
    public ResponseEntity<BaseResponse> syncHuaWeiData(@RequestBody VSphereRequest request) throws Exception {
        BaseResponse baseResponse = providerService.syncHuaWeiData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/sync_vdc_data")
    @ApiOperation(value = "同步VDC和项目数据 ", notes = "同步VDC和项目数据 ")
    public ResponseEntity<BaseResponse> syncVDCData(@RequestBody VDCRequest request) throws Exception {
        BaseResponse baseResponse = providerService.syncVDCData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping("/auto_sync_physical_host_to_monitor")
    @ApiOperation(value = "定时同步主机信息到监控表 ", notes = "定时同步主机信息到监控表 ")
    public ResponseEntity<BaseResponse> autoSyncPhysicalHostToMonitor(@RequestParam String username, @RequestParam String password) throws Exception {
        providerService.autoSyncPhysicalHostToMonitor(username, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse());
    }

    @PostMapping("/save_vdc_username_and_pwd")
    @ApiOperation(value = "保存vdc用户名密码", notes = "保存vdc用户名密码 ")
    public ResponseEntity<BaseResponse> saveVDCUsernameAndPwd(@RequestBody VDCRequest request) throws Exception {
        BaseResponse baseResponse = providerService.updateVDCInfo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/sync_project_vm")
    @ApiOperation(value = "同步项目下的虚拟机", notes = "同步项目下的虚拟机 ")
    public ResponseEntity<BaseResponse> syncProjectVM(@RequestBody VMRequest vmRequest) throws Exception {
        BaseResponse baseResponse = providerService.syncProjectVM(vmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/sync_physical_host")
    @ApiOperation(value = "同步宿主机", notes = "同步宿主机 ")
    public ResponseEntity<BaseResponse> syncPhysicalHost(@RequestBody VMRequest vmRequest) throws Exception {
        BaseResponse baseResponse = providerService.syncPhysicalHost(vmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/sync_cluster_resource")
    @ApiOperation(value = "同步集群下的资源", notes = "同步集群下的资源 ")
    public ResponseEntity<BaseResponse> syncClusterResource(@RequestBody VMRequest vmRequest) throws Exception {
        BaseResponse baseResponse = providerService.syncClusterResource(vmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping("/getCloudPlatformEntrance")
    @ApiOperation(value = "云平台入口集联 列表", notes = "云平台入口集联 列表")
    public ResponseEntity<IaasProviderListResponse> list() {
        return ResponseEntity.status(HttpStatus.OK).body(new IaasProviderListResponse(iaasProviderService.getCloudPlatformEntrance()));
    }

    @GetMapping("/sync_cluster_resource")
    @ApiOperation(value = "同步集群到虚拟机监控表 每2小时同步一次", notes = "同步集群到虚拟机监控表 每2小时同步一次")
    public ResponseEntity<BaseResponse> syncClusterResource() {
        syncMonitor.syncClusterResource();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse());
    }

    @GetMapping("/auto_sync_physical_hostToMonitor")
    @ApiOperation(value = "同步主机到虚拟机监控表 每2小时同步一次", notes = "同步主机到虚拟机监控表 每2小时同步一次")
    public ResponseEntity<BaseResponse> autoSyncPhysicalHostToMonitor() {
        syncMonitor.autoSyncPhysicalHostToMonitor();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse());
    }


    @GetMapping("/auto_syn_vmtomonitor")
    @ApiOperation(value = "同步虚拟机到虚拟机监控表 每2小时同步一次", notes = "同步虚拟机到虚拟机监控表 每2小时同步一次")
    public ResponseEntity<BaseResponse> autoSynVMToMonitor() {
        syncMonitor.autoSynVMToMonitor();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse());
    }

    @GetMapping("/userInfoById")
    @ApiOperation(value = "获取用户信息 信息", notes = "获取用户信息 信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户 id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<IaasProviderResponse> userInfoById(@RequestParam(required = true) Long id) {
        IaasProviderResponse iaasProviderResponse = new IaasProviderResponse();
        IaasProviderVO iaasProviderVO = new IaasProviderVO();
        //查询条件
        IaasProvider provider = new IaasProvider();

        UserResponse userResponse = userClient.getById(AuthContext.getAuthz(), id);

        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.OK).body(iaasProviderResponse);
        }
        BeanUtils.copyProperties(userResponse, iaasProviderVO);
        iaasProviderResponse.setData(iaasProviderVO);
        return ResponseEntity.status(HttpStatus.OK).body(iaasProviderResponse);
    }

    @GetMapping("/provider_cascade")
    @ApiOperation(value = "根据供应商id查出级联信息", notes = "根据供应商id查出级联信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "providerId", value = "供应商id", paramType = "query", dataType = "long")
    })
    public ProviderCascade queryProviderCascadeByProvider(@RequestParam(required = true) Long providerId) {

        return providerService.queryProviderCascadeByProvider(providerId);
    }
}
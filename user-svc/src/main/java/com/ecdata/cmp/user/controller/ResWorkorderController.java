package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.huawei.client.ImageServiceClient;
import com.ecdata.cmp.huawei.client.SecurityGroupsClient;
import com.ecdata.cmp.huawei.client.VDCVirtualMachineClient;
import com.ecdata.cmp.huawei.dto.availablezone.WholeDimensionCapacity;
import com.ecdata.cmp.huawei.dto.response.ImagesListResponse;
import com.ecdata.cmp.huawei.dto.response.SecurityGroupsListResponse;
import com.ecdata.cmp.huawei.dto.response.VdcsListResponse;
import com.ecdata.cmp.huawei.dto.response.VmFlavorsResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VdcsVO;
import com.ecdata.cmp.iaas.client.IaasHWTokenClient;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import com.ecdata.cmp.user.dto.response.*;
import com.ecdata.cmp.user.entity.ResWorkorder;
import com.ecdata.cmp.user.service.IResWorkorderService;
import com.ecdata.cmp.user.service.IUserService;
import com.ecdata.cmp.user.service.ResDictionaryService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ZhaoYX
 * @since 2019/11/20 10:52,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/res_workorder")
@Api(tags = "?????????API")
public class ResWorkorderController {
    @Autowired
    IResWorkorderService resWorkorderService;

    @Autowired
    IUserService userService;

    @Autowired
    IaasHWTokenClient tokenClient;

    @Autowired
    ImageServiceClient imageClient;

    @Autowired
    VDCVirtualMachineClient vdcVirtualMachineClient;

    @Autowired
    SecurityGroupsClient securityGroupsClient;

    @Autowired
    ResDictionaryService dictionaryService;


    @GetMapping("/{id}")
    @ApiOperation(value = "??????id????????????", notes = "??????id????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<ResWorkorderResponse> getById(@PathVariable(name = "id") Long id) {
        ResWorkorder resWorkorder = resWorkorderService.getById(id);
        ResWorkorderVO resWorkorderVO = new ResWorkorderVO();
        BeanUtils.copyProperties(resWorkorder, resWorkorderVO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResWorkorderResponse(resWorkorderVO));
    }
//30003772117868547   ??????????????????roleID??????    ??????  ????????? (????????????)?????????   ???????????? (??????) ??????
    @GetMapping("/page")
    @ApiOperation(value = "??????????????????", notes = "??????????????????  " +
            "?????????????????????roleId??????????????????userId??????????????????userId???roleId???status=applying???" +
            "???????????????userId???roleId???status=closed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderNo", value = "????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "beginTime", value = "????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "??????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleId", value = "??????id", paramType = "query", dataType = "bigint"),
            @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "bigint")
    })
    public ResponseEntity<ResWorkorderMapResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                        @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                        @RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String orderNo,
                                                        @RequestParam(required = false) String beginTime,
                                                        @RequestParam(required = false) String endTime,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false) String roleId,
                                                        @RequestParam(required = false) String userId) throws ParseException {
        QueryWrapper<ResWorkorder> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sf.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(ResWorkorder::getBusinessGroupId, keyword).or().like(ResWorkorder::getDepartmentId, keyword)
                    .or().like(ResWorkorder::getPonderance, keyword).or().like(ResWorkorder::getWorkorderName, keyword)
                    .or().like(ResWorkorder::getTenantId, keyword).or().like(ResWorkorder::getWorkorderType, keyword)
                    .or().like(ResWorkorder::getPriority, keyword).or().like(ResWorkorder::getStatus, keyword);
        }
//        System.out.println(beginTime);
        if(StringUtils.isNotEmpty(beginTime)&&StringUtils.isNotEmpty(endTime)){
            Date beg=sf.parse(beginTime);
            Date end =sf.parse(endTime);
//            System.out.println(beg);
            queryWrapper.lambda().between(ResWorkorder::getCreateTime,beg,end);
        }

        if(StringUtils.isNotEmpty(orderNo))
            queryWrapper.lambda().like(ResWorkorder::getId,orderNo);
        if(StringUtils.isNotEmpty(status))
            queryWrapper.lambda().eq(ResWorkorder::getStatus,status);
        if(StringUtils.isNotEmpty(roleId))
            queryWrapper.lambda().eq(ResWorkorder::getRoleId,Long.parseLong(roleId));
        if(StringUtils.isNotEmpty(userId))
            queryWrapper.lambda().eq(ResWorkorder::getCreateUser,Long.parseLong(userId));
        queryWrapper.lambda().orderByDesc(ResWorkorder::getUpdateTime);
        Page<ResWorkorder> page = new Page<>(pageNo, pageSize);
        IPage<ResWorkorder> result = resWorkorderService.page(page, queryWrapper);
        List<ResWorkorderVO> resWorkorderVOList = new ArrayList<>();
        List<ResWorkorder> resWorkorderList = result.getRecords();
        if (resWorkorderList != null && resWorkorderList.size() > 0) {
            for (ResWorkorder resWorkorder : resWorkorderList) {
                ResWorkorderVO resWorkorderVo = new ResWorkorderVO();
                BeanUtils.copyProperties(resWorkorder, resWorkorderVo);
                resWorkorderVo.setUname(userService.getById(resWorkorderVo.getCreateUser()).getName());
                resWorkorderVOList.add(resWorkorderVo);
            }
        }
        resWorkorderVOList.forEach(System.out::println);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("pageData",new PageVO<>(result, resWorkorderVOList));
        map.put("roleId",30003772117868547L);


        return ResponseEntity.status(HttpStatus.OK).body(new ResWorkorderMapResponse(map));

    }

    @GetMapping("/list")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<ResWorkorderListResponse> list() {
        List<ResWorkorder> resWorkorderList = resWorkorderService.list();
        List<ResWorkorderVO> resWorkorderVOList = new ArrayList<>();
        if (resWorkorderList != null && resWorkorderList.size() > 0) {
            for (ResWorkorder resWorkorder : resWorkorderList) {
                ResWorkorderVO resWorkorderVO = new ResWorkorderVO();
                BeanUtils.copyProperties(resWorkorder, resWorkorderVO);
                resWorkorderVOList.add(resWorkorderVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResWorkorderListResponse(resWorkorderVOList));

    }


    @PostMapping("/add")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> add(@RequestBody ResWorkorderVO resWorkorderVo) {
        BaseResponse baseResponse = new BaseResponse();
        ResWorkorder resWorkorder=resWorkorderService.createWorkOrder(resWorkorderVo);
        log.info("resWorkorderVo "+resWorkorderVo);
//        ResWorkorder resWorkorder = new ResWorkorder();
//        BeanUtils.copyProperties(resWorkorderVo, resWorkorder);
//        resWorkorder.setCreateTime(DateUtil.getNow());
//        resWorkorder.setCreateUser(Sign.getUserId());
//        resWorkorder.setRoleId(30003772117868547L);//?????????????????????????????????????????????
//        resWorkorder.setId(SnowFlakeIdGenerator.getInstance().nextId());
//        resWorkorder.setUpdateTime(DateUtil.getNow());
//        resWorkorder.setUpdateUser(Sign.getUserId());
        if (resWorkorderService.save(resWorkorder)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> update(@RequestBody ResWorkorderVO resWorkorderVo) {
        BaseResponse baseResponse = new BaseResponse();
        ResWorkorder resWorkorder = new ResWorkorder();
        BeanUtils.copyProperties(resWorkorderVo, resWorkorder);
        Long id = resWorkorder.getId();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        //????????????
        QueryWrapper<ResWorkorder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ResWorkorder::getId, id);
        String proName = resWorkorder.getWorkorderName();//tenant_id  business_group_id  department_id
//        if (proName == null) {
//            queryWrapper.lambda().isNull(ResWorkorder::getWorkorderName);
//        } else {
//            queryWrapper.lambda().eq(ResWorkorder::getId, id);
//        }
        //??????????????????????????????
        int count = this.resWorkorderService.count(queryWrapper);
        if (count == 0) {
            //??????????????????????????????????????????
            System.out.println(count);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????????????????????????????????????????????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        resWorkorder.setUpdateTime(DateUtil.getNow());
        resWorkorder.setUpdateUser(Sign.getUserId());
        if (resWorkorderService.updateById(resWorkorder)) {
            if (count == 0) {
                //??????????????????
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }



    @PutMapping("/remove/{id}")
    @ApiOperation(value = "??????", notes = "????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<BaseResponse> remove(@PathVariable(name = "id") Long id) {
        log.info("???????????? resWorkorder id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        ResWorkorder worker=resWorkorderService.getById(id);
        worker.setUpdateUser(Sign.getUserId());
        worker.setUpdateTime(DateUtil.getNow());
        resWorkorderService.updateById(worker);
        if (resWorkorderService.removeById(id)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/remove_batch")
    @ApiOperation(value = "????????????", notes = "??????????????????(????????????)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("???????????? ids???{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //????????????????????????
            for (String id : idArray) {
                ResWorkorder worker=resWorkorderService.getById(id);
                worker.setUpdateTime(DateUtil.getNow());
                worker.setUpdateUser(Sign.getUserId());
                resWorkorderService.updateById(worker);
            }
            //????????????
            resWorkorderService.removeByIds(Arrays.asList(idArray));
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }


    /**
     * ???????????????????????????
     */
    @GetMapping("/into_insertion")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public ResponseEntity<ResWorkorderMapResponse> into() throws IOException {
        Map<String,Object> map = resWorkorderService.selectConditions();
        return ResponseEntity.status(HttpStatus.OK).body(new ResWorkorderMapResponse(map));
    }

    @GetMapping("/get_projects")
    @ApiOperation(value = "??????vdc??????????????????", notes = "??????vdc??????????????????")
    public ResponseEntity<MapResponse> queryProject(@RequestParam(name = "vdcId") String vdcId) throws IOException {
        Map<String,Object> result = resWorkorderService.queryProject(vdcId);
        return ResponseEntity.status(HttpStatus.OK).body(new MapResponse(result));
    }


    @GetMapping("/get_vdc_project")
    @ApiOperation(value = "????????????id?????????????????????????????????????????????????????????",
            notes = "????????????id?????????????????????????????????????????????????????????")
    public ResponseEntity<MapResponse> vdcProject(@RequestParam(name = "vdcId") String vdcId,
                                                  @RequestParam(name = "proId") String proId) throws IOException {
        Map<String,Object> map = resWorkorderService.queryVdcAndProject(vdcId,proId);
        return ResponseEntity.status(HttpStatus.OK).body(new MapResponse(map));
    }



    //==============================================================================================


    /**
     * ???????????????????????????????????????
     * @param projectKey
     * @param
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    @GetMapping("/get_vdc_flavors")
    @ApiOperation(value = "????????????id??????????????????", notes = "????????????id??????????????????")
    public ResponseEntity<VmFlavorsResponse> vdcFlavors(@RequestParam(name = "projectKey") String projectKey,
                                                      @RequestParam(name = "vdcId") String vdcId) throws IOException, IllegalAccessException {
        VmFlavorsResponse vmFlavorsResponse = new VmFlavorsResponse();
        String authz = AuthContext.getAuthz();

//        ResWorkorder resWorkorder= resWorkorderService.qryWorkOrderByPoolId(poolId);

        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVOFlavor(authz,projectKey,vdcId);//,
        RequestVO requestVO= new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        vmFlavorsResponse= vdcVirtualMachineClient.getVmFlavorsList(authz,requestVO);

//        List<ResDictionary> dicts = new ArrayList<>();
//        List<VmFlavors> flvs = vmFlavorsResponse.getData();
//        for(VmFlavors vf:flvs){
//            String groupId = vf.getId();
//            Field[] fields = vf.getClass().getDeclaredFields();
//            for(Field f:fields){
//                if(!f.isAccessible())
//                    f.setAccessible(true);
//                ResDictionary dict = new ResDictionary();
//                dict.setId(SnowFlakeIdGenerator.getInstance().nextId());
//                dict.setBusinessType(1);
//                dict.setType(2);
//                dict.setResId(resWorkorder.getId());
//                dict.setMKey(f.getName());
//                dict.setMValue((String) f.get(vf));
//                dict.setCreateTime(DateUtil.getNow());
//                dict.setCreateUser(Sign.getUserId());
//                dict.setUpdateTime(DateUtil.getNow());
//                dict.setUpdateUser(Sign.getUserId());
//                dict.setGroupId(groupId);
//                dicts.add(dict);
//            }
//        }
//        if(CollectionUtils.isNotEmpty(dicts))
//            dictionaryService.insertBatchDict(dicts);

        return ResponseEntity.status(HttpStatus.OK).body(vmFlavorsResponse);
    }

    @GetMapping("/get_security_groups")
    @ApiOperation(value = "????????????id???????????????", notes = "????????????id???????????????")
    public ResponseEntity<SecurityGroupsListResponse> securityGroups(@RequestParam(name = "projectKey") String projectKey,
                                                                     @RequestParam(name = "vdcId") String vdcId){
        String authz = AuthContext.getAuthz();
        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVOFlavor(authz,projectKey,vdcId);//
        RequestVO requestVO= new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        SecurityGroupsListResponse securityGroupsListResponse=
                securityGroupsClient.getSecurityGroups(authz,requestVO.getOcToken());
        return ResponseEntity.status(HttpStatus.OK).body(securityGroupsListResponse);

    }

    @GetMapping("/get_mirrors")
    @ApiOperation(value = "????????????id???????????????", notes = "????????????id???????????????")
    public ResponseEntity<ImagesListResponse> getMirrors(@RequestParam(name = "projectKey") String projectKey,
                                                            @RequestParam(name = "vdcId") String vdcId) throws IOException {
        String authz = AuthContext.getAuthz();
        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVOFlavor(authz,projectKey,vdcId);//
        RequestVO requestVO= new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        ImagesListResponse ImagesListResponse=imageClient.getImagesList(authz,requestVO);
        return ResponseEntity.status(HttpStatus.OK).body(ImagesListResponse);
    }

//    @GetMapping("/get_disks")
//    @ApiOperation(value = "????????????id????????????", notes = "????????????id????????????")
//    public ResponseEntity<ImagesListResponse> getDisks(@RequestParam(name = "aZoneId") String aZoneId) throws IOException {
//        List<WholeDimensionCapacity> disks=resWorkorderService.queryDisks(aZoneId);
//        return ResponseEntity.status(HttpStatus.OK).body(new ListMapResponse());
//    }



    /**
     *
     * @return
     */
    @GetMapping("/get_vdcListForMore")
    public ResponseEntity<VdcsListResponse> getVdcList(){
        List<VdcsVO> list = resWorkorderService.getVdcList();
        return ResponseEntity.status(HttpStatus.OK).body(new VdcsListResponse(list));
    }

}

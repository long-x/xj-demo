package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.dto.ProcessVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.AlarmClient;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.dto.alarm.AlarmRequestDTO;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.iaas.entity.IaasAlert;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.response.alert.IaasAlertListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.alert.IaasAlertPageResponse;
import com.ecdata.cmp.iaas.service.IIaasAlertService;
import com.ecdata.cmp.user.client.SysNotificationClient;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author ZhaoYX
 * @since 2019/12/6 13:03,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/alert")
@Api(tags = "???????????????API")
public class IaasAlertController {
    @Autowired
    IIaasAlertService iaasAlertService;

    @Autowired
    AlarmClient alarmClient;

    @Autowired
    OauthTokenClient oauthTokenClient;

    @Autowired
    private SysNotificationClient sysNotificationClient;
    /**
     * ??????????????????????????????insert ignore ?????????????????????????????????????????????
     * @param iavo
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> add(@RequestBody IaasAlertVO iavo) {
        BaseResponse baseResponse = new BaseResponse();
        IaasAlert ia = new IaasAlert();
        BeanUtils.copyProperties(iavo,ia);
        //??????csn??????
        long currentCSN = ia.getCsn();
        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasAlert::getCsn,currentCSN);
        IaasAlert duplicatedIAT = iaasAlertService.getOne(queryWrapper);
        //?????????
        if(duplicatedIAT == null){
            ia.setId(SnowFlakeIdGenerator.getInstance().nextId());
            ia.setCreateTime(DateUtil.getNow());
        }else{
            ia.setId(duplicatedIAT.getId());
            ia.setCreateTime(duplicatedIAT.getCreateTime());
//        ia.setId(SnowFlakeIdGenerator.getInstance().nextId())
        };
        ia.setVisible(1);
        ia.setUpdateTime(DateUtil.getNow());
        ia.setUpdateUser(Sign.getUserId());
//        ia.setCreateTime(DateUtil.getNow());
        ia.setCreateUser(Sign.getUserId());
        boolean saveBool = iaasAlertService.saveAlert(ia);
        log.info("alert save "+saveBool);
        boolean updateBool = iaasAlertService.updateById(ia);
        if(saveBool || updateBool) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }


    @PostMapping("/add_batch")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> addBatch(@RequestBody List<IaasAlertVO> iavos) {
        BaseResponse baseResponse = new BaseResponse();
        QueryWrapper<IaasAlert> queryWrapper ;
        try{
            Sign.setCurrentTenantId(10000L);
            String GSTIME="yyyy-MM-DD HH:mm:ss";
            SimpleDateFormat format=new SimpleDateFormat(GSTIME);

            List<IaasAlert> list = new ArrayList<>();
            List<IaasAlert> upList = new ArrayList<>();
            for(IaasAlertVO iavo:iavos){
//                if(iavo.getLatestTime()==null || iavo.getFirstTime()==null)
//                    continue;
                IaasAlert ia = new IaasAlert();
                BeanUtils.copyProperties(iavo,ia);
                //????????????unixTime????????????????????????
                log.info("batch "+iavo.getLatestTime() +" "+iavo.getFirstTime());
                long last=iavo.getLatestTime();
                Date lastTime=new Date(last);
                String lastDateString=format.format(lastTime);
                System.out.println(lastDateString);
                long start = iavo.getFirstTime();
                Date startTime=new Date(start);
                String startDateString=format.format(startTime);
                System.out.println(startDateString);
                //????????????
                ia.setLatestOccurTime(lastTime);
                ia.setFirstOccurTime(startTime);
                //??????csn??????
                long currentCSN = ia.getCsn();
                queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasAlert::getCsn,currentCSN);
                IaasAlert duplicatedIAT = iaasAlertService.getOne(queryWrapper);

                ia.setVisible(1);
                ia.setUpdateTime(DateUtil.getNow());
//                ia.setUpdateUser(Sign.getUserId());

//                ia.setCreateUser(Sign.getUserId());
                //?????????
                if(duplicatedIAT == null){
                    ia.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    ia.setCreateTime(DateUtil.getNow());
                    list.add(ia);
                }
                else {
                    ia.setId(duplicatedIAT.getId());
                    ia.setCreateTime(duplicatedIAT.getCreateTime());
                    upList.add(ia);
                }
                log.info("batch id "+ia.getId());
            }
            if(CollectionUtils.isNotEmpty(list)) {
                if (iaasAlertService.saveAlertBatch(list)) {
                    log.info("iaas ?????????????????? "+list.size());
                    iaasAlertService.addBatchToNotify(list);
                    log.info("note ?????????????????? "+list.size());
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("??????????????????");
                } else {
                    baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    baseResponse.setMessage("??????????????????");
                }
            }
            if(CollectionUtils.isNotEmpty(upList)){
                if(iaasAlertService.updateBatchById(upList)){
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("??????????????????");
                }else{
                    baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    baseResponse.setMessage("??????????????????");
                }
            }
        }catch (Exception e){
            log.debug("forgetPassword??????"+e);
            e.printStackTrace();
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
        }finally {
            Sign.removeCurrentTenantId();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }




    @GetMapping("/page")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "5"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string"),
//            @ApiImplicitParam(name = "businessIds", value = "?????????ids?????????,???: 1,2,3", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasAlertPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                         @RequestParam(required = false) String keyword) {

        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(IaasAlert::getResourceName, keyword).
                    or().like(IaasAlert::getAlertLevel, keyword)
                    .or().like(IaasAlert::getType, keyword)
                    .or().like(IaasAlert::getAlertDefinitionName, keyword)
                    .or().like(IaasAlert::getStatus, keyword)
                    .or().like(IaasAlert::getStartTime, keyword)
                    .or().like(IaasAlert::getSubType, keyword);
        }
        queryWrapper.lambda().eq(IaasAlert::getVisible,1).orderByDesc(IaasAlert::getAlarmName);
        Page<IaasAlert> page = new Page<>(pageNo, pageSize);
        IPage<IaasAlert> result = iaasAlertService.page(page, queryWrapper);
        List<IaasAlertVO> voList = new ArrayList<>();
        List<IaasAlert> list = result.getRecords();
        for(IaasAlert ia:list){
            IaasAlertVO vo = new IaasAlertVO();
            BeanUtils.copyProperties(ia,vo);
            voList.add(vo);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new IaasAlertPageResponse(new PageVO<>(result,voList)));
        
    }

    @GetMapping("/folder")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string"),
//
    })
    public ResponseEntity<IaasAlertPageResponse> folder(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                      @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                      @RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String severity) {
        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasAlert::getVisible,1).isNotNull(IaasAlert::getAlarmName)
                .groupBy(IaasAlert::getAlarmName)
                .orderByDesc(IaasAlert::getAlertLevel);
        if (StringUtils.isNotEmpty(keyword)) { //and(i->i
            queryWrapper.lambda().and(obj->obj.like(IaasAlert::getResourceName, keyword)
//                    .or().like(IaasAlert::getAlertLevel, keyword)
                    .or().like(IaasAlert::getType, keyword)
                    .or().like(IaasAlert::getAlertDefinitionName, keyword)
                    .or().like(IaasAlert::getStatus, keyword)
                    .or().like(IaasAlert::getStartTime, keyword)
                    .or().like(IaasAlert::getCsn, keyword)
                    .or().like(IaasAlert::getAlarmName, keyword)
                    .or().like(IaasAlert::getLogicalRegionName, keyword)
                    .or().like(IaasAlert::getLatestOccurTime, keyword)
                    .or().like(IaasAlert::getSubType, keyword));
        }
        if (StringUtils.isNotEmpty(severity)) {
            queryWrapper.lambda().and(obj->obj.eq(IaasAlert::getAlertLevel, severity));
        }
        Page<IaasAlert> page = new Page<>(pageNo, pageSize);
        IPage<IaasAlert> result = iaasAlertService.page(page, queryWrapper);
        List<IaasAlertVO> voList = new ArrayList<>();
        List<IaasAlert> list = result.getRecords();
        for(IaasAlert ia:list){
            IaasAlertVO vo = new IaasAlertVO();
            String resName="";
            BeanUtils.copyProperties(ia,vo);
            List<IaasAlert> addList=new ArrayList<>();
            QueryWrapper<IaasAlert> wrapper = new QueryWrapper<>();
            if(StringUtils.isNotEmpty(vo.getAlarmName())){
                resName=vo.getAlarmName();
                wrapper.lambda().eq(IaasAlert::getVisible,1).eq(IaasAlert::getAlarmName,resName)
                        .isNotNull(IaasAlert::getAlarmName);
                addList = iaasAlertService.list(wrapper);

            }
            List<IaasAlertVO> iavoList = null;
            if(CollectionUtils.isNotEmpty(addList)){
                addList.remove(0);
                iavoList=new ArrayList<>();
                for(IaasAlert tia:addList){
                    IaasAlert is = iaasAlertService.getOne(new QueryWrapper<IaasAlert>().lambda()
                            .eq(IaasAlert::getVisible,1).eq(IaasAlert::getId,tia.getId())
                            .eq(IaasAlert::getAlarmName,resName));
                    IaasAlertVO ivo = new IaasAlertVO();
                    BeanUtils.copyProperties(is,ivo);
                    iavoList.add(ivo);
                }
            }
            vo.setChildren(iavoList);
            voList.add(vo);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new IaasAlertPageResponse(new PageVO<>(result,voList)));
    }



    @GetMapping("/folder_force")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string"),
//
    })
    public ResponseEntity<IaasAlertPageResponse> folderForce(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                        @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                        @RequestParam(required = false) String keyword) {
        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasAlert::getVisible,1).isNotNull(IaasAlert::getAlarmName)
                .orderByDesc(IaasAlert::getAlertLevel);
        if (StringUtils.isNotEmpty(keyword)) { //and(i->i
            queryWrapper.lambda().and(obj->obj.like(IaasAlert::getResourceName, keyword)
                    .or().like(IaasAlert::getAlertLevel, keyword)
                    .or().like(IaasAlert::getAlertDefinitionName, keyword)
                    .or().like(IaasAlert::getStartTime, keyword)
                    .or().like(IaasAlert::getCsn, keyword)
                    .or().like(IaasAlert::getAlarmName, keyword)
                    .or().like(IaasAlert::getLogicalRegionName, keyword)
                    .or().like(IaasAlert::getLatestOccurTime, keyword));
        }
        Page<IaasAlert> page = new Page<>(pageNo, pageSize);
        IPage<IaasAlertVO> result = iaasAlertService.alertPage(page, queryWrapper);
        return ResponseEntity.status(HttpStatus.OK).body(
                new IaasAlertPageResponse(new PageVO<>(result)));

    }

    @PutMapping("/remove/{id}")
    @ApiOperation(value = "??????", notes = "????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<BaseResponse> remove(@PathVariable(name = "id") Long id) {
        log.info("???????????? component id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        //???????????????id?????????

        if (iaasAlertService.removeById(id)) {
            iaasAlertService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
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
        /*
        ????????????ids
         */
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            iaasAlertService.removeByIds(Arrays.asList(idArray));
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }



    @PutMapping("/update")
    @ApiOperation(value = "??????", notes = "????????????")
    public ResponseEntity<BaseResponse> update(@RequestParam(name = "huaweiId") String id) {
        BaseResponse baseResponse = new BaseResponse();
        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasAlert::getCsn,id);
        IaasAlert iat = iaasAlertService.getOne(queryWrapper);
        iat.setVisible(2);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    /**
     * ??????????????????????????????
     * @param ids
     * @return
     * @throws IOException
     * ????????????????????????remark?????? ????????????  ??????user   @PutMapping("/batch_deal/message")
     */
    @PutMapping("/update_batch")
    @ApiOperation(value = "????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> updateBatch(@RequestParam(name = "huaweiIds") List<Long> ids) throws IOException {
        log.info("???????????? ids???{}", ids);
        List<Long> failedIds= null;
        LongListResponse listResponse=new LongListResponse();

        TokenInfoResponse tokenResponse = getToken(AuthContext.getAuthz(), "","");
//        getToken(AuthContext.getAuthz(),"","");
        AlarmRequestDTO adto=new AlarmRequestDTO();
        if(null != tokenResponse){
            adto.setOmToken(tokenResponse.getData().getOmTokenWeb().getAccessSession());
            adto.setAwaitRemoveIds(ids);
        }
        LongListResponse response=alarmClient.removeAlarmList(AuthContext.getAuthz(),adto);//huawei adto.getOmToken()
        if(null != response) {
            log.info("code " + response.getCode());
            if (response.getCode() == ResultEnum.DEFAULT_SUCCESS.getCode()) {
                failedIds = CollectionUtils.isNotEmpty(response.getData()) ? response.getData() : new ArrayList<>();
                //
                List<Long> finalFailedIds = failedIds;
                //?????????????????????????????????2
                List<Long> sucIds = ids.stream().filter(item -> !finalFailedIds.contains(item)).collect(toList());
                for (Long id : sucIds) {
                    if (id != null) { //&& id.toString().length()==6
                        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(IaasAlert::getCsn, id);
                        IaasAlert iat = iaasAlertService.getOne(queryWrapper);
                        log.info("iat suc " + iat);
                        if (null == iat)
                            continue;
                        iat.setVisible(2);
                        iat.setUpdateTime(DateUtil.getNow());
                        iat.setUpdateUser(Sign.getUserId());
                        boolean result = iaasAlertService.updateById(iat);
                        log.info("batchUpdate " + result);
                        //????????????
                        if (!result) {
                            IaasAlertVO iavo = new IaasAlertVO();
                            BeanUtils.copyProperties(iat, iavo);
                            iavo.setVisible(1);
//                        failed.add(iavo);
                        }
                        log.info("batchUpdate " + id + " ????????????????????????");
                    }
                }
                //???????????????????????????????????????3
                for (Long id : finalFailedIds) {
                    if (id != null) { //&& id.toString().length()==6
                        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(IaasAlert::getCsn, id);
                        IaasAlert iat = iaasAlertService.getOne(queryWrapper);
                        log.info("iat fail " + iat);
                        if (null == iat)
                            continue;
                        iat.setVisible(3);
                        iat.setUpdateTime(DateUtil.getNow());
//                    iat.setUpdateUser(Sign.getUserId());
                        boolean result = iaasAlertService.updateById(iat);
                        log.info("batchUpdate 3 " + result);
                        //????????????
                        if (!result) {
                            IaasAlertVO iavo = new IaasAlertVO();
                            BeanUtils.copyProperties(iat, iavo);
                            iavo.setVisible(1);
//                        failed.add(iavo);
                        }
                        log.info("batchUpdate fail " + id + " ????????????????????????");
                    }
                }
            } else {
                listResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                listResponse.setMessage("????????????????????????");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(listResponse);
            }
        }
        listResponse.setData(failedIds);
        listResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        if(CollectionUtils.isEmpty(failedIds)){
            listResponse.setMessage("??????????????????");
        }else {
            listResponse.setMessage("??????????????????????????????");
        }
        return ResponseEntity.status(HttpStatus.OK).body(listResponse);
    }

    /**
     * ??????????????????????????????
     * @param ids
     * @return
     * @throws IOException
     */
    @PutMapping("/update_batch_alertPage")
    @ApiOperation(value = "????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> updateBatchAlertPage(@RequestParam(name = "huaweiIds") List<Long> ids) throws IOException {
        log.info("????????????Page ids???{}", ids);
        List<Long> failedIds= null;
        LongListResponse listResponse=new LongListResponse();
        List<Long> csns = new ArrayList<>();
        for(Long id : ids){
            IaasAlert ia =iaasAlertService.getById(id);
            if(ia!=null&&ia.getCsn()!=null)
                csns.add(ia.getCsn());
        }
        log.info("?????? csns???{}", csns);
        TokenInfoResponse tokenResponse = getToken(AuthContext.getAuthz(), "","");
//        getToken(AuthContext.getAuthz(),"","");
        AlarmRequestDTO adto=new AlarmRequestDTO();
        if(null != tokenResponse){
            adto.setOmToken(tokenResponse.getData().getOmTokenWeb().getAccessSession());
            adto.setAwaitRemoveIds(csns);
        }
        LongListResponse response=alarmClient.removeAlarmList(AuthContext.getAuthz(),adto);//huawei adto.getOmToken()
        if(null != response) {
            log.info("codePage " + response.getCode());
            if (response.getCode() == ResultEnum.DEFAULT_SUCCESS.getCode()) {
                failedIds = CollectionUtils.isNotEmpty(response.getData()) ? response.getData() : new ArrayList<>();
                List<Long> finalFailedIds = failedIds;
                List<Long> sucIds = csns.stream().filter(item -> !finalFailedIds.contains(item)).collect(toList());
                for (Long id : sucIds) {
                    if (id != null) {
                        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(IaasAlert::getId, id);
                        IaasAlert iat = iaasAlertService.getOne(queryWrapper);
                        log.info("iatPage suc " + iat);
                        if (null == iat)
                            continue;
                        iat.setVisible(2);
                        iat.setUpdateTime(DateUtil.getNow());
                        iat.setUpdateUser(Sign.getUserId());
                        boolean result = iaasAlertService.updateById(iat);
                        log.info("batchUpdatePage " + result);
                        //????????????
                        if (!result) {
                            IaasAlertVO iavo = new IaasAlertVO();
                            BeanUtils.copyProperties(iat, iavo);
                            iavo.setVisible(1);
//                        failed.add(iavo);
                        }
                        else{
//                            List<SysNotificationReceiverVO> voList = new ArrayList<>();
                            for(Long csn:csns){
                                SysNotificationReceiverVO vo = iaasAlertService.fetchReceiveIdByAlert(csn);
                                if(vo!=null)
//                                    voList.add(vo);
                                sysNotificationClient.oneDealMessage(vo.getId(),"");
                            }
                        }
                        log.info("batchUpdatePage " + id + " ????????????????????????");
                    }
                }
                //???????????????????????????????????????3
                for (Long id : finalFailedIds) {
                    if (id != null) { //&& id.toString().length()==6
                        QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(IaasAlert::getCsn, id);
                        IaasAlert iat = iaasAlertService.getOne(queryWrapper);
                        log.info("iatPage fail " + iat);
                        if (null == iat)
                            continue;
                        iat.setVisible(3);
                        iat.setUpdateTime(DateUtil.getNow());
//                    iat.setUpdateUser(Sign.getUserId());
                        boolean result = iaasAlertService.updateById(iat);
                        log.info("batchUpdatePage 3 " + result);
                        //????????????
                        if (!result) {
                            IaasAlertVO iavo = new IaasAlertVO();
                            BeanUtils.copyProperties(iat, iavo);
                            iavo.setVisible(1);
//                        failed.add(iavo);
                        }
                        log.info("batchUpdatePage fail " + id + " ????????????????????????");
                    }
                }
            } else {
                listResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                listResponse.setMessage("????????????????????????");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(listResponse);
            }
        }
        listResponse.setData(failedIds);
        listResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        if(CollectionUtils.isEmpty(failedIds)){
            listResponse.setMessage("??????????????????");
        }else {
            listResponse.setMessage("??????????????????????????????");
        }
        return ResponseEntity.status(HttpStatus.OK).body(listResponse);
    }


    /**
     * ?????????????????????????????????????????????????????????
     * @param processVO
     * @return
     * @throws IOException
     */
    @PutMapping("/update_process")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> updateProcess(@RequestBody ProcessVO processVO) throws IOException {
        List<Long> ids = processVO.getCsns();
        Integer status = processVO.getStatus();
        log.info("???????????? csns???{}", ids);
        LongListResponse listResponse=new LongListResponse();
        //????????????????????????csn
        List<Long> failed = new ArrayList<>();
        //????????????????????????????????????
        try{
            Sign.setCurrentTenantId(10000L);
        for(Long id:ids){
            if(id!=null){// && id.toString().length()==6
                QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasAlert::getCsn,id);
                IaasAlert iat = iaasAlertService.getOne(queryWrapper);
                log.info("processUpdate iat "+iat);
                if(null == iat)
                    continue;
                iat.setVisible(status);
                iat.setUpdateTime(DateUtil.getNow());
//                iat.setUpdateUser(Sign.getUserId());
                boolean result=iaasAlertService.updateById(iat);
                log.info("processUpdate "+result);
                //????????????
                if(!result){
                    IaasAlertVO iavo = new IaasAlertVO();
                    BeanUtils.copyProperties(iat,iavo);
                    iavo.setVisible(1);
                    failed.add(iavo.getCsn());
                }
                log.info("processUpdate "+id+" ?????? ????????????????????????");
            }
        }

        if(CollectionUtils.isNotEmpty(failed)){
            listResponse.setMessage("????????????????????????");
        }else
            listResponse.setMessage("??????????????????");
        }catch (Exception e){
            log.debug("forgetPassword??????"+e);
            e.printStackTrace();
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
        }finally {
            Sign.removeCurrentTenantId();
        }
        return ResponseEntity.status(HttpStatus.OK).body(listResponse);
    }


    private TokenInfoResponse getToken(String authz, String userName, String password) {
        TokenInfoResponse tokenResponse = null;
        try {
            tokenResponse = oauthTokenClient.getTokenByUser(authz,
                    "",
                    "domainName",
                    userName,
                    password);

            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("???????????????token??????!", e);
            return null;
        }

        return tokenResponse;
    }

    @GetMapping("/fetch_unhandled_alerts")
    @ApiOperation(value = "??????????????????????????????????????????", notes = "??????????????????????????????????????????")
    public ResponseEntity<BaseResponse> fetchUnhandled() throws IOException {
        List<IaasAlertVO> iat = new ArrayList<>();
        try{
            Sign.setCurrentTenantId(10000L);
            QueryWrapper<IaasAlert> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().ne(IaasAlert::getVisible,2);
            List<IaasAlert> iats = iaasAlertService.list(queryWrapper);
            for(IaasAlert alert :iats) {
                IaasAlertVO vo = new IaasAlertVO();
                BeanUtils.copyProperties(alert,vo);
                iat.add(vo);
            }
            log.info("fetchUnhandled iat "+iat);
        }catch (Exception e){
            log.info("fetchUnhandled??????"+e);
            e.printStackTrace();
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
        }finally {
            Sign.removeCurrentTenantId();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasAlertListResponse(iat));
    }



}

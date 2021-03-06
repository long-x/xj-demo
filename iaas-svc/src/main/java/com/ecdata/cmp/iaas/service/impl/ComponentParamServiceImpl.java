package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.component.*;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import com.ecdata.cmp.iaas.mapper.component.ComponentParamMapper;
import com.ecdata.cmp.iaas.service.IComponentParamHistoryService;
import com.ecdata.cmp.iaas.service.IComponentParamService;
import com.ecdata.cmp.iaas.utils.CompareClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Slf4j
@Service
public class ComponentParamServiceImpl extends ServiceImpl<ComponentParamMapper, IaasComponentParam>
        implements IComponentParamService {
    private String[] ignoreFields={"id","serialVersionUID","componentId","updateTime","updateUser","createUser","createTime"
            ,"modifiable","tenantId","isDeleted","valueSelect","key"};

    @Autowired
    IComponentParamHistoryService compHisParamService;

    @Override
    public void modifyUpdateRecord(Long id, Long userId) {

    }

    private IaasComponentParamVO createParam(IaasComponentParamVO pm){
        IaasComponentParamVO param = new IaasComponentParamVO();
        BeanUtils.copyProperties(pm,param);
        param.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setComponentId(pm.getComponentId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
        log.info("pmUpdate - createParam "+param);
        return param;
    }

@Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,List<IaasComponentParamVO> update) throws Exception {
        boolean result=false; //false ?????????
        log.info("??????????????????");
        List<IaasComponentParamVO> pmUpList = componentVo.getCompParams();//??????
        List<IaasComponentParamVO> pmList = testVO.getCompParams();//?????????
        log.info("????????????????????? " + testVO.getCompParams());
        if(pmUpList.size()!=pmList.size()){
            log.info("???????????? ???????????????");
            result=true;
        }
        if(pmUpList.size()>0&&pmList.size()>0&&pmUpList.size()==pmList.size()) {
            int listSize = Math.max(pmUpList.size(), pmList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(pmUpList.get(i), pmList.get(i), IaasComponentParamVO.class,
                        ignoreFields)) {
                    log.info("???????????? " + pmUpList.get(i));
                    update.add(pmUpList.get(i));
                    result = true;  //?????????
                    break;
                }
            }
        }
        if (!result)
            log.info("?????????????????? ");

        return result;
    }

/*
pmUpList  pmList ??????????????????????????????
 */
    @Override
    public BaseResponse saveComponentParamHistory(IaasComponentVO componentVo, IaasComponentVO testVO,
                                          IaasComponentHistory history,
                                          IComponentParamHistoryService compHisParamService,
                                          BaseResponse baseResponse)
            throws Exception {
        log.info("??????pm ???"+componentVo);
        List<IaasComponentParamVO> update = new ArrayList<>();//???????????????
        IaasComponent component = new IaasComponent();
        BeanUtils.copyProperties(testVO, component);

        if (componentVo.getCompParams() != null) {
            //==============================?????????=================================
            List<IaasComponentParamVO> pmList = testVO.getCompParams();//?????????
            log.info("????????????????????? size "+pmList.size());
            this.judgeAndSaveHistory(pmList,component,history);
            //???????????????
            if(!this.judge(componentVo,testVO,update)){
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("??????????????????");
                return baseResponse;
            }else {
                //============================??????===============================
                this.updateParam(component,baseResponse,componentVo,testVO,update);
            }
        }
        return baseResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateParam(IaasComponent component, BaseResponse baseResponse,
                            IaasComponentVO componentVo,
                            IaasComponentVO testVO,
                            List<IaasComponentParamVO> upList){
        log.info("??????????????????");
        List<IaasComponentParamVO> pmUpList = componentVo.getCompParams();//??????
        List<IaasComponentParam> addList = new ArrayList<>();
        List<IaasComponentParam> updateList = new ArrayList<>();
        List<IaasComponentParam> removeList = new ArrayList<>();
        QueryWrapper<IaasComponentParam> delWrapper = new QueryWrapper<>();
        delWrapper.lambda().eq(IaasComponentParam::getComponentId, componentVo.getId());
        List<IaasComponentParam> oldList = this.list(delWrapper);
        if(!CollectionUtils.isEmpty(oldList))
            removeList.addAll(oldList);

        for(IaasComponentParamVO vo : upList){
            IaasComponentParam ipm = new IaasComponentParam();
            BeanUtils.copyProperties(vo,ipm);
            ipm .setComponentId(testVO.getId())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());
            updateList.add(ipm);  //????????????
        }



        if(pmUpList.size()>0) {
            //????????????
            //?????????id???????????????
            log.info("??????????????? size "+pmUpList.size());
            for (IaasComponentParamVO pmvo : pmUpList) {

                IaasComponentParamVO pm;
                if(null==pmvo.getId())
                    pm=this.createParam(pmvo);
                else pm=pmvo;
                Long pmId=pm.getId();
                //???????????????param
                IaasComponentParam param = new IaasComponentParam();
                BeanUtils.copyProperties(pm,param);

                QueryWrapper<IaasComponentParam> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasComponentParam::getId, pmId)
                        .eq(IaasComponentParam::getComponentId, component.getId());
                int count=this.count(queryWrapper);
                log.info("pmUpdate - count "+count);
                if(count<1){ //??????id?????????????????????????????????
                    log.info("updateParam pm ???????????????????????? ");
                    param
                            .setComponentId(testVO.getId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(null==param.getId())
                        param.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    addList.add(param); //????????????
                }else{ //????????????
                    for (int i = 0; i < removeList.size(); i++) {
                        if (pmId.equals(removeList.get(i).getId())) {
                            log.info("?????????????????????"+removeList.get(i));
                            removeList.remove(i);
                            break;
                        }
                    }
                    param
                            .setComponentId(testVO.getId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
                    updateList.add(param);
                    log.info("????????? pm");
                }       //????????????vo????????????
            }//????????????
        }//???????????????
        boolean add=false;
        boolean update=false;
        boolean remove=false;
        if (addList.size() > 0) {
            if(this.saveBatch(addList)) {
                add=true;
                log.info("??????????????????????????? ");
            }
            else log.info("??????????????????????????? ");
        }else add=true;
        addList.forEach((IaasComponentParam pm)->System.out.println("????????????: "+pm));


        if (updateList.size() > 0) {
            if(this.updateBatchById(updateList)){
                update=true;
                log.info("?????????????????? ");
            }else log.info("?????????????????? ");
        }else update=true;
        updateList.forEach((IaasComponentParam pm)->System.out.println("????????????: "+pm));

        boolean up=false;
        boolean rv=false;
        for(IaasComponentParam pm :removeList){
            pm.setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
            up=this.updateById(pm);
            rv=this.removeById(pm.getId());
            if(!up || !rv){
                log.info("??????????????????????????? ");
                break;
            }
        }
        removeList.forEach((IaasComponentParam pm)->System.out.println("????????????: "+pm));
        if(rv)//up&&
            remove=true;
        if(add && update && remove){
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
        }else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("??????????????????");
        }
    }



    /**
     * ?????????????????????
     * @param pm
     * @param history
     * @param compHisParamService
     */
    private void saveParamHis(IaasComponentParamVO pm,IaasComponentHistory history
            ,IComponentParamHistoryService compHisParamService){
        log.info("?????????????????????");
        IaasComponentParamHistory param = new IaasComponentParamHistory();
        BeanUtils.copyProperties(pm, param,
                "id", "componentHistoryId", "componentId", "componentParamId");
        param.setId(SnowFlakeIdGenerator.getInstance().nextId());
        param.setComponentHistoryId(history.getId());
        param.setComponentId(history.getComponentId());
        param.setComponentParamId(pm.getId());
//        param.setParamType(pm.getParamType());
        param.setCreateUser(Sign.getUserId());
        param.setHistoryTime(DateUtil.getNow());
        param.setUpdateUser(Sign.getUserId());
        param.setUpdateTime(DateUtil.getNow());
        param.setHistoryTime(DateUtil.getNow());
        compHisParamService.save(param);
        log.info("pmHis - saveHis "+param);
    }

    @Override
    public void judgeAndSaveHistory(List<IaasComponentParamVO> pmList,
                                    IaasComponent component,IaasComponentHistory history){
        if (pmList.size() != 0) {
            for (IaasComponentParamVO pmvo : pmList) {//????????????param??????????????????
                //?????????id??????
                IaasComponentParamVO pm;
                if(null==pmvo.getId())
                    pm=this.createParam(pmvo);
                else pm=pmvo;
                Long pmId = pm.getId();
                //????????????????????????id????????????wrapper
                QueryWrapper<IaasComponentParam> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasComponentParam::getId, pmId)
                        .eq(IaasComponentParam::getComponentId, component.getId());
                int count=this.count(queryWrapper);
                log.info("pmHis - count "+count);
                log.info("pmHis - type "+pm.getParamType());
                //???????????????param
                IaasComponentParam param = new IaasComponentParam();
                if(count<1){ //??????????????????
                    //??????
                    log.info("saveParamHistory add ?????????????????????????????????????????????????????? ");
                    BeanUtils.copyProperties(pm,param);
                    param
//                            .setComponentId(testVO.getId())????????????
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(param.getId()==null)
                        param.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    this.save(param);
                    log.info("pmHis - save "+param);
                }else{ //??????????????????????????????
                    this.saveParamHis(pm,history,compHisParamService);
                }
            }
        }
    }



    @Override
    public boolean add(IaasComponentVO componentVo, IaasComponent component){
        boolean result = false;
        List<IaasComponentParam> pms = new ArrayList<>();
        if (componentVo.getCompParams() != null) {
            List<IaasComponentParamVO> pmsVO = componentVo.getCompParams();
            for (IaasComponentParamVO vo : pmsVO) {
                //????????????
                IaasComponentParam sc = new IaasComponentParam();
                this.connectedParamAdd(component, vo);
                BeanUtils.copyProperties(vo, sc);
                
                pms.add(sc);
            }
            if (this.saveBatch(pms)){
                log.info("add ????????????????????????");
                result=true;
            }
            else log.info("add ????????????????????????");
        }
        else {
            log.info("add ??????????????????");
            result=true;
        }
        return  result;
    }


    private void connectedParamAdd(IaasComponent component,IaasComponentParamVO vo){
        vo.setComponentId(component.getId());
        vo.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
    }


}

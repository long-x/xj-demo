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
        boolean result=false; //false 不更新
        log.info("参数更新判断");
        List<IaasComponentParamVO> pmUpList = componentVo.getCompParams();//更新
        List<IaasComponentParamVO> pmList = testVO.getCompParams();//入历史
        log.info("参数入历史条件 " + testVO.getCompParams());
        if(pmUpList.size()!=pmList.size()){
            log.info("需要更新 数量不匹配");
            result=true;
        }
        if(pmUpList.size()>0&&pmList.size()>0&&pmUpList.size()==pmList.size()) {
            int listSize = Math.max(pmUpList.size(), pmList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(pmUpList.get(i), pmList.get(i), IaasComponentParamVO.class,
                        ignoreFields)) {
                    log.info("需要更新 " + pmUpList.get(i));
                    update.add(pmUpList.get(i));
                    result = true;  //要更新
                    break;
                }
            }
        }
        if (!result)
            log.info("参数无需更新 ");

        return result;
    }

/*
pmUpList  pmList 可能会不同的处理方式
 */
    @Override
    public BaseResponse saveComponentParamHistory(IaasComponentVO componentVo, IaasComponentVO testVO,
                                          IaasComponentHistory history,
                                          IComponentParamHistoryService compHisParamService,
                                          BaseResponse baseResponse)
            throws Exception {
        log.info("接收pm ："+componentVo);
        List<IaasComponentParamVO> update = new ArrayList<>();//更新的集合
        IaasComponent component = new IaasComponent();
        BeanUtils.copyProperties(testVO, component);

        if (componentVo.getCompParams() != null) {
            //==============================入历史=================================
            List<IaasComponentParamVO> pmList = testVO.getCompParams();//入历史
            log.info("参数准备入历史 size "+pmList.size());
            this.judgeAndSaveHistory(pmList,component,history);
            //是否要更新
            if(!this.judge(componentVo,testVO,update)){
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("参数无需更新");
                return baseResponse;
            }else {
                //============================更新===============================
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
        log.info("参数更新判断");
        List<IaasComponentParamVO> pmUpList = componentVo.getCompParams();//更新
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
            updateList.add(ipm);  //塞入更新
        }



        if(pmUpList.size()>0) {
            //参数更新
            //更新的id可能是错的
            log.info("参数更新中 size "+pmUpList.size());
            for (IaasComponentParamVO pmvo : pmUpList) {

                IaasComponentParamVO pm;
                if(null==pmvo.getId())
                    pm=this.createParam(pmvo);
                else pm=pmvo;
                Long pmId=pm.getId();
                //预备更改的param
                IaasComponentParam param = new IaasComponentParam();
                BeanUtils.copyProperties(pm,param);

                QueryWrapper<IaasComponentParam> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasComponentParam::getId, pmId)
                        .eq(IaasComponentParam::getComponentId, component.getId());
                int count=this.count(queryWrapper);
                log.info("pmUpdate - count "+count);
                if(count<1){ //根据id和关联判断是否存在参数
                    log.info("updateParam pm 没有该参数，新增 ");
                    param
                            .setComponentId(testVO.getId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(null==param.getId())
                        param.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    addList.add(param); //等待新增
                }else{ //有该参数
                    for (int i = 0; i < removeList.size(); i++) {
                        if (pmId.equals(removeList.get(i).getId())) {
                            log.info("移出删除列表："+removeList.get(i));
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
                    log.info("更新中 pm");
                }       //数据库有vo中的参数
            }//更新过程
        }//有参数更新
        boolean add=false;
        boolean update=false;
        boolean remove=false;
        if (addList.size() > 0) {
            if(this.saveBatch(addList)) {
                add=true;
                log.info("参数更新时新增成功 ");
            }
            else log.info("参数更新时新增失败 ");
        }else add=true;
        addList.forEach((IaasComponentParam pm)->System.out.println("添加参数: "+pm));


        if (updateList.size() > 0) {
            if(this.updateBatchById(updateList)){
                update=true;
                log.info("参数更新成功 ");
            }else log.info("参数更新失败 ");
        }else update=true;
        updateList.forEach((IaasComponentParam pm)->System.out.println("更新参数: "+pm));

        boolean up=false;
        boolean rv=false;
        for(IaasComponentParam pm :removeList){
            pm.setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
            up=this.updateById(pm);
            rv=this.removeById(pm.getId());
            if(!up || !rv){
                log.info("参数更新时删除失败 ");
                break;
            }
        }
        removeList.forEach((IaasComponentParam pm)->System.out.println("删除参数: "+pm));
        if(rv)//up&&
            remove=true;
        if(add && update && remove){
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新参数成功");
        }else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新参数失败");
        }
    }



    /**
     * 参数历史表插入
     * @param pm
     * @param history
     * @param compHisParamService
     */
    private void saveParamHis(IaasComponentParamVO pm,IaasComponentHistory history
            ,IComponentParamHistoryService compHisParamService){
        log.info("参数历史表插入");
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
            for (IaasComponentParamVO pmvo : pmList) {//入历史的param应该都是对的
                //传入时id有无
                IaasComponentParamVO pm;
                if(null==pmvo.getId())
                    pm=this.createParam(pmvo);
                else pm=pmvo;
                Long pmId = pm.getId();
                //一步到位全部关联id都在一条wrapper
                QueryWrapper<IaasComponentParam> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasComponentParam::getId, pmId)
                        .eq(IaasComponentParam::getComponentId, component.getId());
                int count=this.count(queryWrapper);
                log.info("pmHis - count "+count);
                log.info("pmHis - type "+pm.getParamType());
                //预备更改的param
                IaasComponentParam param = new IaasComponentParam();
                if(count<1){ //不存在该参数
                    //新增
                    log.info("saveParamHistory add 数据库没有该组件参数，新增，不入历史 ");
                    BeanUtils.copyProperties(pm,param);
                    param
//                            .setComponentId(testVO.getId())需要更新
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(param.getId()==null)
                        param.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    this.save(param);
                    log.info("pmHis - save "+param);
                }else{ //存在该种参数，入历史
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
                //参数关联
                IaasComponentParam sc = new IaasComponentParam();
                this.connectedParamAdd(component, vo);
                BeanUtils.copyProperties(vo, sc);
                
                pms.add(sc);
            }
            if (this.saveBatch(pms)){
                log.info("add 参数集合添加成功");
                result=true;
            }
            else log.info("add 参数集合添加失败");
        }
        else {
            log.info("add 参数集合为空");
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

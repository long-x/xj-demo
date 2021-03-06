package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.component.*;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import com.ecdata.cmp.iaas.mapper.component.ComponentOperationMapper;
import com.ecdata.cmp.iaas.service.IComponentOpHistoryService;
import com.ecdata.cmp.iaas.service.IComponentOperationService;
import com.ecdata.cmp.iaas.service.IComponentScriptHistoryService;
import com.ecdata.cmp.iaas.service.IComponentScriptService;
import com.ecdata.cmp.iaas.utils.CompareClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Service
@Slf4j
public class ComponentOperationServiceImpl extends ServiceImpl<ComponentOperationMapper, IaasComponentOperation>
        implements IComponentOperationService {

    @Autowired
    IComponentScriptService scriptService;

    @Autowired
    IComponentOpHistoryService compHisOpService;


    private String[] ignoreFields = {"id","serialVersionUID","componentId","scriptId","updateTime","updateUser",
            "createUser","createTime" ,"tenantId","isDeleted","key","achieve"};

    private List<IaasComponentOperationVO> updateList;

    private List<IaasComponentOperation> rvList;


    private void setUpdateList(List<IaasComponentOperationVO> update) {
        updateList=update;
    }

    private List<IaasComponentOperationVO> getUpdateList(){
        return updateList;
    }


    public List<IaasComponentOperation> getRvList() {
        return rvList;
    }

    public void setRvList(List<IaasComponentOperation> rvList) {
        this.rvList = rvList;
    }






    @Override
    public void modifyUpdateRecord(Long id, Long userId) {
        baseMapper.modifyUpdateRecord(id,userId);
    }

    @Override
    public List<IaasComponentOperationVO> qryComponentOperationInfo(Long compId) {
        return baseMapper.queryOpById(compId);
    }

    @Override
    public boolean add(IaasComponentScriptVO vo,IaasComponentVO componentVo,IaasComponent component){
        boolean result=false;
        //??????
        if(componentVo.getCompOpVOs()!=null&&componentVo.getCompOpVOs().size()>0){
            List<IaasComponentOperationVO> opvoList = componentVo.getCompOpVOs();
            List<IaasComponentOperation> ops = new ArrayList<>();

            for (IaasComponentOperationVO opvo : opvoList) {
                IaasComponentOperation op = new IaasComponentOperation();
                //scvo id??????
                if(vo.getId()!=null&&scriptService.getById(vo.getId())!=null){
                    log.info("add ?????? ???????????????");
                    this.connectedOpAdd(component,vo.getId(),opvo);
                    BeanUtils.copyProperties(opvo,op);
                    op.setScriptId(vo.getId());
                    ops.add(op);
                }else if(StringUtils.isNotEmpty(vo.getKey())){
                    //key??????
                    log.info("add ??????key??????");
                    String key = vo.getKey();
                    if(opvo.getScriptId()!=null){
                        if(opvo.getScriptId().toString().equals(key)){
                            this.connectedOpAdd(component,opvo);
                            BeanUtils.copyProperties(opvo,op);
                            op.setScriptId(vo.getId());
                            ops.add(op);
                        }
                    }
                }
            }
            if (this.saveBatch(ops)) {
                log.info("add ????????????????????????");
                result=true;
            }
            else log.info("add ????????????????????????");
        }
        else {
            log.info("add ??????????????????");
            result=true;
        }
        return result;
    }

    @Override
    public boolean defaultAddition(IaasComponent component) {
        boolean result=false;
        boolean resSc=false;
        boolean resOp=false;
        log.info("add op defaultAddition");
        if(component.getId() == null){
            log.info(" defaultAddition ???????????????");
            return false;
        }else{ //???????????? ??????5????????????????????????????????????
            List<IaasComponentScript> scvoList = new ArrayList<>();
            IaasComponentScript createSc = new IaasComponentScript();
            createSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("????????????")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript configureSc = new IaasComponentScript();
            configureSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("????????????")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript startSc = new IaasComponentScript();
            startSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("????????????")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript stopSc = new IaasComponentScript();
            stopSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("????????????")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript deleteSc = new IaasComponentScript();
            deleteSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("????????????")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());
            scvoList.add(createSc);
            scvoList.add(configureSc);
            scvoList.add(startSc);
            scvoList.add(stopSc);
            scvoList.add(deleteSc);

            if(scriptService.saveBatch(scvoList))
                resSc=true;
            log.info("defaultAddition ?????????????????? "+resSc);
//===========================????????????????????????============================

            List<IaasComponentOperation> opvoList = new ArrayList<>();
            IaasComponentOperation create = new IaasComponentOperation();
            create.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("??????")
                    .setOperation("create")
                    .setScriptId(createSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation configure = new IaasComponentOperation();
            configure.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("??????")
                    .setOperation("configure")
                    .setScriptId(configureSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation start = new IaasComponentOperation();
            start.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("??????")
                    .setOperation("start")
                    .setScriptId(startSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation stop = new IaasComponentOperation();
            stop.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("??????")
                    .setOperation("stop")
                    .setScriptId(stopSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation delete = new IaasComponentOperation();
            delete.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("??????")
                    .setOperation("delete")
                    .setScriptId(deleteSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());
            opvoList.add(create);
            opvoList.add(configure);
            opvoList.add(start);
            opvoList.add(stop);
            opvoList.add(delete);

            if(this.saveBatch(opvoList))
                resOp=true;
            log.info("defaultAddition ?????????????????? "+resOp);
        }
        if(resOp && resSc)
            result=true;
        return result;
    }

    /**
     *
     * @param component  ????????????
     * @param scvo ????????????
     * @param vo ????????????
     */
    private void connectedOpAdd(IaasComponent component,Long scvo,IaasComponentOperationVO vo){
        vo.setComponentId(component.getId());
        vo.setScriptId(scvo);
        vo.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
    }

    /**
     *
     * @param component
     * @param vo
     */
    private void connectedOpAdd(IaasComponent component,IaasComponentOperationVO vo){
        vo.setComponentId(component.getId());
        vo.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
    }

    /**
     * result false ???????????? true????????????
     * @param componentVo
     * @param testVO
     * @param update
     * @return
     * @throws Exception
     */
    @Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,
                         List<IaasComponentOperationVO> update) throws Exception {
        boolean result=false; //false ?????????
        log.info("????????????up??????");
        List<IaasComponentOperationVO> opUpList = componentVo.getCompOpVOs();//??????
        List<IaasComponentOperationVO> opList = testVO.getCompOpVOs();//?????????
        if(opUpList.size()!=opList.size()){
            log.info("???????????????,??????up ");
            result=true;
        }

        //?????????????????????
        if(opUpList.size()>0&&opList.size()>0&&opUpList.size()==opList.size()) {
            int listSize = Math.max(opUpList.size(), opList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(opUpList.get(i), opList.get(i), IaasComponentOperationVO.class,
                        ignoreFields)) {
                    update.add(opUpList.get(i));
                    log.info("??????up?????? " + opList.get(i));
                    result = true;  //?????????
//                    this.saveComponentOpHistory(componentVo,testVO,history,
//                            compHisOpService,compHisScriptService,baseResponse)
                    break;
                }
            }
        }
        if (!result)
            log.info("??????????????????up ");
        log.info("????????????up " +update);
        this.setUpdateList(update);
        return result;
    }

    /**
     * result false ???????????? true????????????
     * @param componentVo
     * @param testVO
     * @return
     * @throws Exception
     */
    @Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO) throws Exception {
        boolean result=false; //false ?????????
        log.info("??????????????????");
        List<IaasComponentOperationVO> opUpList = componentVo.getCompOpVOs();//??????
        List<IaasComponentOperationVO> opList = testVO.getCompOpVOs();//?????????
        log.info("????????????????????? " + testVO.getCompOpVOs());
        if(opUpList.size()!=opList.size()){
            log.info("???????????????,?????? ");
            result=true;
        }

        if(opUpList.size()>0&&opList.size()>0&&opUpList.size()==opList.size()) {
            int listSize = Math.max(opUpList.size(), opList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(opUpList.get(i), opList.get(i), IaasComponentOperationVO.class,
                        ignoreFields)) {
                    log.info("???????????? ");
                    result = true;  //?????????
                    break;
                }
            }
        }
        if (!result)
            log.info("?????????????????? ");

        return result;
    }
    
    /**
     * ?????????update
     * @param componentVo
     * @param testVO
     * @param history
     * @param compHisOpService
     * @param compHisScriptService
     * @param baseResponse
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponse saveComponentOpHistory(IaasComponentVO componentVo, IaasComponentVO testVO,
                                       IaasComponentHistory history,
                                       IComponentOpHistoryService compHisOpService,
                                               IComponentScriptHistoryService compHisScriptService,
                                       BaseResponse baseResponse) throws Exception {

        IaasComponent component = new IaasComponent();
        BeanUtils.copyProperties(testVO, component);

        List<IaasComponentOperationVO> update = new ArrayList<>();//???????????????
        if (componentVo.getCompOpVOs() != null) {

            List<IaasComponentOperationVO> opList = testVO.getCompOpVOs();//?????????
            log.info("????????????????????? " + testVO.getCompOpVOs());
            log.info("?????????????????????");
            //==========================????????????===================================
            this.judgeAndSaveHistory(opList,component,history);

            if(!this.judge(componentVo,testVO,update)){
                log.info("??????opup????????????");
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("??????????????????");
                return baseResponse;
            }
        }
        return baseResponse;
    }

    /**
     * ?????????????????????
     * @param opList
     * @param component
     * @param history
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void judgeAndSaveHistory(List<IaasComponentOperationVO> opList,
                                    IaasComponent component,IaasComponentHistory history){
        log.info("op judgeAndSaveHistory opList "+opList.size());
        if (opList.size() != 0) {
            for (IaasComponentOperationVO opvo : opList) {

                IaasComponentOperationVO op;
                if(null==opvo.getId())
                    op=this.createOperation(opvo);
                else op=opvo;

                Long opId = op.getId();
                log.info("op judgeAndSaveHistory opId"+opId);
                QueryWrapper<IaasComponentOperation> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasComponentOperation::getId, opId)
                        .eq(IaasComponentOperation::getComponentId, component.getId());
//                        .eq(IaasComponentOperation::getScriptId,op.getScriptId());
                int opCount = this.count(queryWrapper);
                log.info("opHis - count "+opCount);
                IaasComponentOperation operation = new IaasComponentOperation();
                if (opCount < 1) {
                    log.info("operation judge op ???????????????????????????????????? ");
                    BeanUtils.copyProperties(op, operation);
                    operation//.setId(SnowFlakeIdGenerator.getInstance().nextId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(operation.getId()==null)
                        operation.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    log.info("opHis -judge save "+operation);
                    this.save(operation);

                } else {
                    this.saveOpHis(op, history, compHisOpService);

                }//????????????????????????
            }//????????????
        }//????????????
    }


    private IaasComponentOperationVO createOperation(IaasComponentOperationVO opvo) {
        IaasComponentOperationVO op = new IaasComponentOperationVO();
        BeanUtils.copyProperties(opvo,op);
        op.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setComponentId(opvo.getComponentId())
                .setScriptId(opvo.getScriptId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
        log.info("opUpdate - createOperation "+op);
        return op;

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOperation(
                                List<IaasComponentOperationVO> opUpList,
                                List<IaasComponentOperation> removeList,
                                IaasComponent component, BaseResponse baseResponse,
                                IaasComponentVO testVO,
                                List<Map<String, Object>> keys){

        log.info("updateOperation ");
        List<IaasComponentOperationVO> update = this.getUpdateList();
        List<IaasComponentOperation> addList = new ArrayList<>();
        List<IaasComponentOperation> updateList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(update)){
            for(IaasComponentOperationVO vo : update){
                IaasComponentOperation ico = new IaasComponentOperation();
                BeanUtils.copyProperties(vo,ico);
                ico .setComponentId(testVO.getId())
                        .setUpdateUser(Sign.getUserId())
                        .setUpdateTime(DateUtil.getNow());
                updateList.add(ico);  //????????????
            }
        }
        removeList = new ArrayList<>();
        QueryWrapper<IaasComponentOperation> delWrapper = new QueryWrapper<>();
        delWrapper.lambda().eq(IaasComponentOperation::getComponentId, component.getId());
        List<IaasComponentOperation> oldList = this.list(delWrapper);
        if (!CollectionUtils.isEmpty(oldList))
            removeList.addAll(oldList);
        //???????????????????????????????????????
        this.setRvList(removeList);
        log.info("setRvList "+removeList);

        //???????????????
        List<IaasComponentOperation> saveList = new ArrayList<>();
        //===========================================================
        //????????????
        if (opUpList.size() > 0) {
            //?????????id???????????????
            log.info("??????????????? size " + opUpList.size());
            for (IaasComponentOperationVO opvo : opUpList) {

                IaasComponentOperationVO op;
                if(null==opvo.getId())
                    op=this.createOperation(opvo);
                else op=opvo;

                Long opId = op.getId();
                log.info("id?????? op "+opId);
                QueryWrapper<IaasComponentOperation> opWrapper = new QueryWrapper<>();
                opWrapper.lambda().eq(IaasComponentOperation::getId, opId)
                        .eq(IaasComponentOperation::getComponentId, component.getId());
//                        .eq(IaasComponentOperation::getScriptId, op.getScriptId());
                int opCount = this.count(opWrapper);
                log.info("opUpdate - count "+opCount);
                IaasComponentOperation operation = new IaasComponentOperation();
                BeanUtils.copyProperties(op, operation);

                if (opCount < 1) {
                    log.info("updateOp op ???????????????????????????????????? ");
                    log.info("opupInsert "+operation.getId()+" "+
                            operation.getOperation()+" "+operation.getOperationName()+
                            " "+operation.getScriptId());
                    operation//.setId(SnowFlakeIdGenerator.getInstance().nextId())
                            .setComponentId(testVO.getId())
                            .setScriptId(op.getScriptId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(null==operation.getId())
                        operation.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    addList.add(operation);//????????????
                } else {//??????????????????????????????
                    removeList=this.getRvList();
                    removeList.forEach((IaasComponentOperation vo)->System.out.println("getRvList "+vo.getId()+" "+
                            vo.getOperation()+" "+vo.getOperationName()+" "+vo.getScriptId()));
                    for (int i = 0; i < removeList.size(); i++) {
                        if (opId.equals(removeList.get(i).getId())) {
                            log.info("op?????????????????????" + removeList.get(i));
                            removeList.remove(i);
                            break;
                        }
                    }
                    removeList.forEach((IaasComponentOperation vo)->System.out.println("afterRvList "+vo.getId()+" "+
                            vo.getOperation()+" "+vo.getOperationName()+" "+vo.getScriptId()));
                    if(!updateList.contains(operation)) {
                        saveList.add(operation);
                        log.info("????????? op");
//                        saveList.forEach(System.out::println);
                    }
                }       //????????????vo??????id
            }//????????????
        }
        //????????????
        boolean opAdd = false;
        boolean opUpdate = false;
        boolean opRemove = true;
        System.out.println("keys: "+keys);
        if (addList.size() > 0) {
            //?????????????????????
            addList.forEach((IaasComponentOperation op)->System.out.println("??????id "+op.getId()+
                    " "+op.getOperationName()+" "+op.getOperation()));

            for(IaasComponentOperation ico :addList){
                if(null!=ico.getScriptId()){
                    for(int keyIndex=0;keyIndex<keys.size();keyIndex++){
                        System.out.println("addList key "+ico.getScriptId()+" "
                                +keys.get(keyIndex).get("key"));
                        System.out.println(ico.getScriptId().toString().equals(keys.get(keyIndex).get("key")));
                        if(ico.getScriptId().toString().equals(keys.get(keyIndex).get("key").toString())){
                            ico.setScriptId((Long) keys.get(keyIndex).get("scId"));
                            break;
                        }
                    }
                }
            }
            addList.forEach((IaasComponentOperation pm)->System.out.println("????????????: "+pm));

            if (this.saveBatch(addList)) {
                opAdd = true;
                log.info("??????????????????????????? ");
            } else log.info("??????????????????????????? ");
        } else opAdd = true;

        if (updateList.size() > 0) {
            for(IaasComponentOperation ico :updateList){
                if(null!=ico.getScriptId()){
                    for(int keyIndex=0;keyIndex<keys.size();keyIndex++){
                        if(ico.getScriptId().toString().equals(keys.get(keyIndex).get("key"))){
                            System.out.println("updateList key "+ico.getScriptId()+" "+
                                    keys.get(keyIndex).get("key"));
                            ico.setScriptId((Long) keys.get(keyIndex).get("scId"));
                        }
                    }
                }
            }
            updateList.forEach((IaasComponentOperation pm)->System.out.println("????????????: "+pm));

            if (this.updateBatchById(updateList)) {
                opUpdate = true;
                log.info("?????????????????? ");
            } else log.info("?????????????????? ");
        } else opUpdate = true;

//                    boolean up = false;
        boolean opRv = false;
        for (IaasComponentOperation op : removeList) {
            op.setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
            this.updateById(op);
            opRv=this.removeById(op.getId());
            if (!opRv) {
                log.info("??????????????????????????? ");
                break;
            }
        }
        log.info("opRv "+opRv);
        removeList.forEach((IaasComponentOperation pm)->System.out.println("????????????: "+pm.getId()));
//            if (opRv)
//                opRemove = true;
        if (opAdd && opUpdate && opRemove) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            log.info("???????????????????????? ");
            baseResponse.setMessage("??????????????????");
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            log.info("???????????????????????? "+opAdd +" "+opUpdate+" "+opRemove);
            baseResponse.setMessage("??????????????????");
        }
    }


    /**
     * ?????????????????????
     * @param op
     * @param history
     * @param compHisOpService
     */
    private void saveOpHis(IaasComponentOperationVO op, IaasComponentHistory history,
                               IComponentOpHistoryService compHisOpService) {
        log.info("?????????????????????");
        IaasComponentOpHistory operation = new IaasComponentOpHistory();
        BeanUtils.copyProperties(op, operation,
                "componentHistoryId", "componentId","dcriptId", "componentOperationId");
        operation.setId(SnowFlakeIdGenerator.getInstance().nextId());
        operation.setComponentHistoryId(history.getId());
        operation.setComponentId(history.getComponentId());
        operation.setComponentOperationId(op.getId());
        operation.setScriptId(op.getScriptId());
        operation.setCreateUser(Sign.getUserId());
        operation.setHistoryTime(DateUtil.getNow());
        operation.setUpdateUser(Sign.getUserId());
        operation.setUpdateTime(DateUtil.getNow());
        operation.setHistoryTime(DateUtil.getNow());
        log.info("opHis - saveHis "+operation.getId());
        compHisOpService.save(operation);

    }

}

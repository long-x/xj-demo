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
        //操作
        if(componentVo.getCompOpVOs()!=null&&componentVo.getCompOpVOs().size()>0){
            List<IaasComponentOperationVO> opvoList = componentVo.getCompOpVOs();
            List<IaasComponentOperation> ops = new ArrayList<>();

            for (IaasComponentOperationVO opvo : opvoList) {
                IaasComponentOperation op = new IaasComponentOperation();
                //scvo id正确
                if(vo.getId()!=null&&scriptService.getById(vo.getId())!=null){
                    log.info("add 操作 有操作关联");
                    this.connectedOpAdd(component,vo.getId(),opvo);
                    BeanUtils.copyProperties(opvo,op);
                    op.setScriptId(vo.getId());
                    ops.add(op);
                }else if(StringUtils.isNotEmpty(vo.getKey())){
                    //key关联
                    log.info("add 操作key关联");
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
                log.info("add 操作集合添加成功");
                result=true;
            }
            else log.info("add 操作集合添加失败");
        }
        else {
            log.info("add 操作集合为空");
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
            log.info(" defaultAddition 找不到组件");
            return false;
        }else{ //正常情况 添加5个默认的空操作绑定空脚本
            List<IaasComponentScript> scvoList = new ArrayList<>();
            IaasComponentScript createSc = new IaasComponentScript();
            createSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("创建操作")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript configureSc = new IaasComponentScript();
            configureSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("配置操作")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript startSc = new IaasComponentScript();
            startSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("启动操作")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript stopSc = new IaasComponentScript();
            stopSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("停止操作")
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentScript deleteSc = new IaasComponentScript();
            deleteSc.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setScriptName("删除操作")
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
            log.info("defaultAddition 操作正常添加 "+resSc);
//===========================默认操作一一对应============================

            List<IaasComponentOperation> opvoList = new ArrayList<>();
            IaasComponentOperation create = new IaasComponentOperation();
            create.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("创建")
                    .setOperation("create")
                    .setScriptId(createSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation configure = new IaasComponentOperation();
            configure.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("配置")
                    .setOperation("configure")
                    .setScriptId(configureSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation start = new IaasComponentOperation();
            start.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("启动")
                    .setOperation("start")
                    .setScriptId(startSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation stop = new IaasComponentOperation();
            stop.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("停止")
                    .setOperation("stop")
                    .setScriptId(stopSc.getId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());

            IaasComponentOperation delete = new IaasComponentOperation();
            delete.setComponentId(component.getId())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setOperationName("删除")
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
            log.info("defaultAddition 操作正常添加 "+resOp);
        }
        if(resOp && resSc)
            result=true;
        return result;
    }

    /**
     *
     * @param component  关联组件
     * @param scvo 关联操作
     * @param vo 操作本身
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
     * result false 不用更新 true需要更新
     * @param componentVo
     * @param testVO
     * @param update
     * @return
     * @throws Exception
     */
    @Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,
                         List<IaasComponentOperationVO> update) throws Exception {
        boolean result=false; //false 不更新
        log.info("操作更新up判断");
        List<IaasComponentOperationVO> opUpList = componentVo.getCompOpVOs();//更新
        List<IaasComponentOperationVO> opList = testVO.getCompOpVOs();//入历史
        if(opUpList.size()!=opList.size()){
            log.info("数量不匹配,更新up ");
            result=true;
        }

        //集合中有不同项
        if(opUpList.size()>0&&opList.size()>0&&opUpList.size()==opList.size()) {
            int listSize = Math.max(opUpList.size(), opList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(opUpList.get(i), opList.get(i), IaasComponentOperationVO.class,
                        ignoreFields)) {
                    update.add(opUpList.get(i));
                    log.info("需要up更新 " + opList.get(i));
                    result = true;  //要更新
//                    this.saveComponentOpHistory(componentVo,testVO,history,
//                            compHisOpService,compHisScriptService,baseResponse)
                    break;
                }
            }
        }
        if (!result)
            log.info("操作无需更新up ");
        log.info("更新集合up " +update);
        this.setUpdateList(update);
        return result;
    }

    /**
     * result false 不用更新 true需要更新
     * @param componentVo
     * @param testVO
     * @return
     * @throws Exception
     */
    @Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO) throws Exception {
        boolean result=false; //false 不更新
        log.info("操作更新判断");
        List<IaasComponentOperationVO> opUpList = componentVo.getCompOpVOs();//更新
        List<IaasComponentOperationVO> opList = testVO.getCompOpVOs();//入历史
        log.info("操作入历史条件 " + testVO.getCompOpVOs());
        if(opUpList.size()!=opList.size()){
            log.info("数量不匹配,更新 ");
            result=true;
        }

        if(opUpList.size()>0&&opList.size()>0&&opUpList.size()==opList.size()) {
            int listSize = Math.max(opUpList.size(), opList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(opUpList.get(i), opList.get(i), IaasComponentOperationVO.class,
                        ignoreFields)) {
                    log.info("需要更新 ");
                    result = true;  //要更新
                    break;
                }
            }
        }
        if (!result)
            log.info("操作无需更新 ");

        return result;
    }
    
    /**
     * 真正的update
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

        List<IaasComponentOperationVO> update = new ArrayList<>();//更新的集合
        if (componentVo.getCompOpVOs() != null) {

            List<IaasComponentOperationVO> opList = testVO.getCompOpVOs();//入历史
            log.info("操作入历史条件 " + testVO.getCompOpVOs());
            log.info("操作准备入历史");
            //==========================先入历史===================================
            this.judgeAndSaveHistory(opList,component,history);

            if(!this.judge(componentVo,testVO,update)){
                log.info("脚本opup无需更新");
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("脚本无需更新");
                return baseResponse;
            }
        }
        return baseResponse;
    }

    /**
     * 判断并塞入历史
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
                    log.info("operation judge op 数据库中没有该操作，新增 ");
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

                }//操作时候再数据库
            }//遍历操作
        }//操作存在
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
                updateList.add(ico);  //塞入更新
            }
        }
        removeList = new ArrayList<>();
        QueryWrapper<IaasComponentOperation> delWrapper = new QueryWrapper<>();
        delWrapper.lambda().eq(IaasComponentOperation::getComponentId, component.getId());
        List<IaasComponentOperation> oldList = this.list(delWrapper);
        if (!CollectionUtils.isEmpty(oldList))
            removeList.addAll(oldList);
        //作为属性，随后可以关联删除
        this.setRvList(removeList);
        log.info("setRvList "+removeList);

        //保留不变的
        List<IaasComponentOperation> saveList = new ArrayList<>();
        //===========================================================
        //操作更新
        if (opUpList.size() > 0) {
            //更新的id可能是错的
            log.info("操作更新中 size " + opUpList.size());
            for (IaasComponentOperationVO opvo : opUpList) {

                IaasComponentOperationVO op;
                if(null==opvo.getId())
                    op=this.createOperation(opvo);
                else op=opvo;

                Long opId = op.getId();
                log.info("id更新 op "+opId);
                QueryWrapper<IaasComponentOperation> opWrapper = new QueryWrapper<>();
                opWrapper.lambda().eq(IaasComponentOperation::getId, opId)
                        .eq(IaasComponentOperation::getComponentId, component.getId());
//                        .eq(IaasComponentOperation::getScriptId, op.getScriptId());
                int opCount = this.count(opWrapper);
                log.info("opUpdate - count "+opCount);
                IaasComponentOperation operation = new IaasComponentOperation();
                BeanUtils.copyProperties(op, operation);

                if (opCount < 1) {
                    log.info("updateOp op 数据库中没有该操作，新增 ");
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
                    addList.add(operation);//等待新增
                } else {//数据库中有并且有关联
                    removeList=this.getRvList();
                    removeList.forEach((IaasComponentOperation vo)->System.out.println("getRvList "+vo.getId()+" "+
                            vo.getOperation()+" "+vo.getOperationName()+" "+vo.getScriptId()));
                    for (int i = 0; i < removeList.size(); i++) {
                        if (opId.equals(removeList.get(i).getId())) {
                            log.info("op移出删除列表：" + removeList.get(i));
                            removeList.remove(i);
                            break;
                        }
                    }
                    removeList.forEach((IaasComponentOperation vo)->System.out.println("afterRvList "+vo.getId()+" "+
                            vo.getOperation()+" "+vo.getOperationName()+" "+vo.getScriptId()));
                    if(!updateList.contains(operation)) {
                        saveList.add(operation);
                        log.info("保留的 op");
//                        saveList.forEach(System.out::println);
                    }
                }       //数据库有vo中的id
            }//确认更新
        }
        //开始更新
        boolean opAdd = false;
        boolean opUpdate = false;
        boolean opRemove = true;
        System.out.println("keys: "+keys);
        if (addList.size() > 0) {
            //操作和操作对应
            addList.forEach((IaasComponentOperation op)->System.out.println("添加id "+op.getId()+
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
            addList.forEach((IaasComponentOperation pm)->System.out.println("添加操作: "+pm));

            if (this.saveBatch(addList)) {
                opAdd = true;
                log.info("操作更新时新增成功 ");
            } else log.info("操作更新时新增失败 ");
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
            updateList.forEach((IaasComponentOperation pm)->System.out.println("更新操作: "+pm));

            if (this.updateBatchById(updateList)) {
                opUpdate = true;
                log.info("操作更新成功 ");
            } else log.info("操作更新失败 ");
        } else opUpdate = true;

//                    boolean up = false;
        boolean opRv = false;
        for (IaasComponentOperation op : removeList) {
            op.setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
            this.updateById(op);
            opRv=this.removeById(op.getId());
            if (!opRv) {
                log.info("操作更新时删除失败 ");
                break;
            }
        }
        log.info("opRv "+opRv);
        removeList.forEach((IaasComponentOperation pm)->System.out.println("操作删除: "+pm.getId()));
//            if (opRv)
//                opRemove = true;
        if (opAdd && opUpdate && opRemove) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            log.info("操作更新成功响应 ");
            baseResponse.setMessage("更新操作成功");
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            log.info("操作更新失败响应 "+opAdd +" "+opUpdate+" "+opRemove);
            baseResponse.setMessage("更新操作失败");
        }
    }


    /**
     * 操作历史表插入
     * @param op
     * @param history
     * @param compHisOpService
     */
    private void saveOpHis(IaasComponentOperationVO op, IaasComponentHistory history,
                               IComponentOpHistoryService compHisOpService) {
        log.info("操作历史表插入");
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

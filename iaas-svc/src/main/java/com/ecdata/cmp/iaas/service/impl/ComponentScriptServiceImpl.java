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
import com.ecdata.cmp.iaas.mapper.component.ComponentScriptMapper;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Slf4j
@Service
public class ComponentScriptServiceImpl extends ServiceImpl<ComponentScriptMapper, IaasComponentScript>
        implements IComponentScriptService {
    private String[] ignoreFields = {"id","serialVersionUID", "updateTime", "updateUser", "createUser",
            "createTime", "tenantId","isDeleted","key","compOps"};

    @Autowired
    IComponentOperationService operationService;


    @Autowired
    IComponentScriptHistoryService scriptHistoryService;

    @Override
    public void modifyUpdateRecord(Long id, Long userId) {

    }

    @Override
    public List<IaasComponentScriptVO> qryComponentScriptInfo(Long compId) {
        return baseMapper.queryScripsById(compId);
    }

    @Override
    public boolean add(IaasComponentVO componentVo, IaasComponent component){
        boolean result = false;
        List<IaasComponentScript> scs = new ArrayList<>();
        if (componentVo.getCompScripts() != null) {
            List<IaasComponentScriptVO> scsVO = componentVo.getCompScripts();
            for (IaasComponentScriptVO vo : scsVO) {
                //脚本关联
                IaasComponentScript sc = new IaasComponentScript();
                this.connectedScriptAdd(component, vo);
                BeanUtils.copyProperties(vo, sc);
                //5大基本空操作

                if(vo.getKey()!=null){
                    log.info("add 脚本添加操作");
                    operationService.add(vo,componentVo,component);
                }
                scs.add(sc);
            }
            if (this.saveBatch(scs)) {
                log.info("add 脚本集合添加成功");
                result=true;
            }
            else log.info("add 脚本集合添加失败");
        }
        else {
            log.info("add 脚本集合为空");
            result=true;
        }
        return  result;
    }


    private void connectedScriptAdd(IaasComponent component,IaasComponentScriptVO vo){

        vo.setComponentId(component.getId());
        vo.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
    }

    /**
     * sc中sc和op都判断
     * @param componentVo
     * @param testVO
     * @param update
     * @return
     * @throws Exception
     */
    @Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,
                         List<IaasComponentScriptVO> update) throws Exception {
        boolean result=false; //false 不更新

        List<IaasComponentOperationVO> opUpdate = new ArrayList<>();//更新的集合
        if(operationService.judge(componentVo,testVO,opUpdate))   {
            result=true;
            log.info("sc操作up更新 "+result);
        }
        if (!result)
            log.info("脚本中操作无需up更新 ");
        else{
            log.info("更新scop集合 " +opUpdate);
            return result;
        }

//        List<IaasComponentScriptVO> still = new ArrayList<>();//不动的集合
        log.info("脚本更新up判断");
        List<IaasComponentScriptVO> scUpList = componentVo.getCompScripts();//更新
        List<IaasComponentScriptVO> scList = testVO.getCompScripts();//入历
        if(scUpList.size()!=scList.size()){
            log.info("数量不匹配,更新up ");
            result=true;
        }
        //集合中有不同项
        if(scUpList.size()>0&&scList.size()>0&&scUpList.size()==scList.size()) {
            int listSize = Math.max(scUpList.size(), scList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(scUpList.get(i), scList.get(i), IaasComponentScriptVO.class,
                        ignoreFields)) {
                    log.info("需要up更新 ");
                    update.add(scUpList.get(i));
                    result = true;  //要更新
                    break;
                }
            }
        }
        if (!result)
            log.info("脚本up无需更新 ");
        log.info("更新scup集合 " +update);
        return result;
    }

    /**
     * comp中判断脚本和操作
     * @param componentVo
     * @param testVO
     * @return
     * @throws Exception
     */
    @Override
    public boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO)throws Exception {
        boolean result=false; //false 不更新

        if(operationService.judge(componentVo,testVO))   {
            result=true;
            log.info("操作sc更新 "+result);
        }
//        List<IaasComponentScriptVO> still = new ArrayList<>();//不动的集合
        log.info("脚本更新判断");
        List<IaasComponentScriptVO> scUpList = componentVo.getCompScripts();//更新
        List<IaasComponentScriptVO> scList = testVO.getCompScripts();//入历史
        log.info("脚本入历史条件 " + testVO.getCompScripts());
        if(scUpList.size()!=scList.size()){
            log.info("需要更新 数量不匹配");
            result=true;
            return result;
        }
        if(scUpList.size()>0&&scList.size()>0) {
            int listSize = Math.max(scUpList.size(), scList.size());
            for (int i = 0; i < listSize; i++) {
                if (!CompareClassUtil.compare(scUpList.get(i), scList.get(i), IaasComponentScriptVO.class,
                        ignoreFields)) {
                    log.info("需要更新 " + scUpList.get(i));
                    log.info("需要更新 " + scList.get(i));
                    result = true;  //要更新
                    break;
                }
            }
        }

        if (!result)
            log.info("脚本无需更新 ");
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    @Override
    public BaseResponse saveComponentScriptHistory(IaasComponentVO componentVo, IaasComponentVO testVO,
                                           IaasComponentHistory history,
                                           IComponentScriptHistoryService compHisScriptService,
                                           IComponentOpHistoryService compHisOpService,
                                           BaseResponse baseResponse)
            throws Exception {

        IaasComponent component = new IaasComponent();
        BeanUtils.copyProperties(testVO, component);

        List<IaasComponentScriptVO> update = new ArrayList<>();//更新的集合
        if (componentVo.getCompScripts() != null) {
            //==========================先入历史===================================
            List<IaasComponentScriptVO> scList = testVO.getCompScripts();//入历史
            log.info("先遍历脚本");
            //脚本
            if (scList.size() != 0) {
                for (IaasComponentScriptVO scvo : scList) {
                    IaasComponentScriptVO sc;
                    if (null == scvo.getId())
                        sc = this.createScript(scvo);
                    else sc = scvo;

                    Long scId = sc.getId();
                    QueryWrapper<IaasComponentScript> scWrapper = new QueryWrapper<>();
                    scWrapper.lambda().eq(IaasComponentScript::getId, scId)
                            .eq(IaasComponentScript::getComponentId, component.getId());
                    int count = this.count(scWrapper);
                    log.info("scHis - count " + count);
                    IaasComponentScript script = new IaasComponentScript();
                    if (count < 1) {
                        log.info("ScriptHistory add 数据库中没有该脚本，新增 ");
                        BeanUtils.copyProperties(sc, script);
                        script.setId(SnowFlakeIdGenerator.getInstance().nextId())
                                .setComponentId(testVO.getId())
                                .setCreateUser(Sign.getUserId())
                                .setCreateTime(DateUtil.getNow())
                                .setUpdateUser(Sign.getUserId())
                                .setUpdateTime(DateUtil.getNow());
                        this.save(script);
                        log.info("scHis - save " + script);
                    } else {
                        this.saveScriptHis(sc, history, compHisScriptService);
//                        operationService.saveComponentOpHistory(componentVo,testVO,history,
//                                compHisOpService,compHisScriptService,baseResponse);
                    }//脚本入历史
                }
            }

            if(!this.judge(componentVo,testVO,update)){
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("脚本无需更新");
                return baseResponse;
            }else {
                this.updateScript(component,baseResponse,componentVo,testVO,update);
            }
        }
        return baseResponse;
    }


    @Override
    public void judgeAndSaveHistory(IaasComponentVO testVO,//List<IaasComponentScriptVO> scList,
                                    IaasComponent component,IaasComponentHistory history){
        List<IaasComponentScriptVO> scList=testVO.getCompScripts();
        if (scList.size() != 0) {
            for (IaasComponentScriptVO scvo : scList) {
                IaasComponentScriptVO sc;
                if (null == scvo.getId())
                    sc = this.createScript(scvo);
                else sc = scvo;

                Long scId = sc.getId();
                QueryWrapper<IaasComponentScript> scWrapper = new QueryWrapper<>();
                scWrapper.lambda().eq(IaasComponentScript::getId, scId)
                        .eq(IaasComponentScript::getComponentId, component.getId());
                int count = this.count(scWrapper);
                log.info("scHis - count " + count);
                IaasComponentScript script = new IaasComponentScript();
                if (count < 1) {
                    log.info("ScriptHistory add 数据库中没有该脚本，新增 ");
                    BeanUtils.copyProperties(sc, script);
                    script
//                            .setComponentId(testVO.getId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    this.save(script);
                    log.info("scHis - save " + script);
                } else {
                    this.saveScriptHis(sc, history,scriptHistoryService);
//                    operationService.judgeAndSaveHistory(testVO.getCompOpVOs(),component,history);
                }//脚本入历史
            }
        }
    }


    public void saveScriptHis(IaasComponentScriptVO sc, IaasComponentHistory history,
                               IComponentScriptHistoryService compHisScriptService) {
        log.info("脚本历史表插入");
        IaasComponentScriptHistory script = new IaasComponentScriptHistory();
        BeanUtils.copyProperties(sc, script,
                "id", "componentHistoryId", "componentId", "componentParamId");
        script.setId(SnowFlakeIdGenerator.getInstance().nextId())
            .setComponentHistoryId(history.getId())
            .setComponentId(history.getComponentId())
            .setComponentScriptId(sc.getId())
            .setCreateUser(Sign.getUserId())
            .setHistoryTime(DateUtil.getNow())
            .setUpdateUser(Sign.getUserId())
            .setUpdateTime(DateUtil.getNow())
            .setHistoryTime(DateUtil.getNow());
        log.info("scHis - saveHis "+script);
        compHisScriptService.save(script);

    }



    private IaasComponentScriptVO createScript(IaasComponentScriptVO sc){
        IaasComponentScriptVO scriptVO = new IaasComponentScriptVO();
        BeanUtils.copyProperties(sc,scriptVO);
        scriptVO.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setComponentId(sc.getComponentId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
        log.info("scUpdate - createScript "+scriptVO);
        return scriptVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateScript(IaasComponent component, BaseResponse baseResponse,
                             IaasComponentVO componentVo,
                             IaasComponentVO testVO,
                             List<IaasComponentScriptVO> upList){

        List<IaasComponentScript> removeScList = new ArrayList<>();
        QueryWrapper<IaasComponentScript> delScWrapper = new QueryWrapper<>();
        delScWrapper.lambda().eq(IaasComponentScript::getComponentId, componentVo.getId());
        List<IaasComponentScript> oldScList = this.list(delScWrapper);
        if (!CollectionUtils.isEmpty(oldScList))
            removeScList.addAll(oldScList);
        List<IaasComponentScriptVO> scUpList = componentVo.getCompScripts();//更新
        //原先的key在scUpList里面

        List<IaasComponentScript> addScList = new ArrayList<>();
        List<IaasComponentScript> updateScList = new ArrayList<>();
        for(IaasComponentScriptVO vo : upList){
            IaasComponentScript isc = new IaasComponentScript();
            BeanUtils.copyProperties(vo,isc);
            isc .setComponentId(testVO.getId())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());
            updateScList.add(isc);  //塞入更新
        }
        //保留不变的
        List<IaasComponentScript> saveScList = new ArrayList<>();
        List<Map<String, Object>> keys = new ArrayList<>();

        //脚本判断
        if (scUpList.size() > 0) {
            //脚本更新
            //更新的id可能是错的
            log.info("脚本判断 size " + scUpList.size());
            scUpList.forEach((IaasComponentScriptVO vo) -> System.out.println("key: " + vo.getKey()));

            for (IaasComponentScriptVO scvo : scUpList) {

                IaasComponentScriptVO sc;
                if (null == scvo.getId())
                    sc = this.createScript(scvo);
                else sc = scvo;

                Long scId = sc.getId();
                log.info("更新中 sc");
                QueryWrapper<IaasComponentScript> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(IaasComponentScript::getId, scId)
                        .eq(IaasComponentScript::getComponentId, component.getId());
                int count = this.count(queryWrapper);
                log.info("scUpdate - count " + count);
                IaasComponentScript script = new IaasComponentScript();
                BeanUtils.copyProperties(sc, script);
                if (count < 1) {
                    log.info("Script sc 数据库中没有该脚本，新增 ");

                    script//.setId(SnowFlakeIdGenerator.getInstance().nextId())
                            .setComponentId(testVO.getId())
                            .setCreateUser(Sign.getUserId())
                            .setCreateTime(DateUtil.getNow())
                            .setUpdateUser(Sign.getUserId())
                            .setUpdateTime(DateUtil.getNow());
                    if(null==script.getId())
                        script.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    addScList.add(script);
                } else { //更新操作
                    for (int i = 0; i < removeScList.size(); i++) {
                        if (scId.equals(removeScList.get(i).getId())) {
                            log.info("sc移出删除列表：" + removeScList.get(i).getId());
                            removeScList.remove(i);
                            break;
                        }
                    }
                    if(!updateScList.contains(script)) {
                        saveScList.add(script);
                        log.info("保留的 sc");
                        saveScList.forEach(System.out::println);
                    }
                }
                if (StringUtils.isNotEmpty(sc.getKey())) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("key", sc.getKey());
                    map.put("scId", script.getId());
                    keys.add(map);
                    System.out.println("key add " + map);
                }
            }
        }
        //开始更新
        boolean add = false;
        boolean update = false;
        boolean remove = true;
        if (addScList.size() > 0) {
            if (this.saveBatch(addScList)) {
                add = true;
                log.info("脚本更新时新增成功 ");
            } else log.info("脚本更新时新增失败 ");
        } else add = true;
        addScList.forEach((IaasComponentScript pm) -> System.out.println("添加脚本: " + pm.getId()));

        if (updateScList.size() > 0) {
            if (this.updateBatchById(updateScList)) {
                update = true;
                log.info("脚本更新成功 ");
            } else log.info("脚本更新失败 ");
        } else update = true;
        updateScList.forEach((IaasComponentScript pm) -> System.out.println("更新脚本: " + pm.getId()));

        boolean up = false;
        boolean rv = false;
        for (IaasComponentScript sc : removeScList) {
            sc.setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
            up = this.updateById(sc);
            rv = this.removeById(sc.getId());
            QueryWrapper<IaasComponentOperation> query = new QueryWrapper<>();
            query.lambda().eq(IaasComponentOperation::getScriptId,sc.getId());
            log.info("脚本中删除操作 "+operationService.list(query));//.getId()
            operationService.remove(query);
            if ( !rv) {
                log.info("脚本更新时删除失败 ");
                break;
            }
        }
        log.info("up && rv " + (up && rv));
        removeScList.forEach((IaasComponentScript pm) -> System.out.println("删除脚本: " + pm.getId()));
//            if (up && rv)
//                remove = true;
        if (add && update && remove) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            log.info("脚本更新成功响应 ");
            baseResponse.setMessage("更新脚本成功");
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            log.info("脚本更新失败响应 " + add + " " + update + " " + remove);
            baseResponse.setMessage("更新脚本失败");
        }

        List<IaasComponentOperationVO> opUpList = componentVo.getCompOpVOs();//更新
        opUpList.forEach((IaasComponentOperationVO vo)->System.out.println("opUpList "+vo.getId()+" "+
        vo.getOperation()+" "+vo.getOperationName()+" "+vo.getScriptId()));

        List<IaasComponentOperation> removeList = new ArrayList<>();
        //操作更新
        operationService.updateOperation(opUpList,removeList,component,baseResponse,testVO,keys);


    }
    
}

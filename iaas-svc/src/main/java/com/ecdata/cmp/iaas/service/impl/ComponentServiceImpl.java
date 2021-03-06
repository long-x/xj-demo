package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.component.*;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import com.ecdata.cmp.iaas.mapper.component.ComponentMapper;
import com.ecdata.cmp.iaas.mapper.component.ComponentOperationMapper;
import com.ecdata.cmp.iaas.mapper.component.ComponentParamMapper;
import com.ecdata.cmp.iaas.mapper.component.ComponentScriptMapper;
import com.ecdata.cmp.iaas.service.*;
import com.ecdata.cmp.iaas.utils.CompareClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Slf4j
@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, IaasComponent>
        implements IComponentService {

    @Autowired
    ComponentMapper componentMapper;

    @Autowired
    ComponentParamMapper componentParamMapper;

    @Autowired
    ComponentScriptMapper componentScriptMapper;

    @Autowired
    ComponentOperationMapper componentOperationMapper;
    private String[] ignoreFields={"id","serialVersionUID","tenantId","initType","key",
            "version","uname","updateTime","updateUser","createUser","createTime"
            ,"isDeleted","key"};//,"compParams","compScripts","compOpVOs"

    @Autowired
    IComponentHistoryService compHisService;

    @Autowired
    IComponentOpHistoryService compHisOperationService;

    @Autowired
    IComponentScriptHistoryService compHisScriptService;

    @Autowired
    IComponentParamHistoryService compHisParamService;

    @Autowired
    IComponentParamService componentParamService;

    @Autowired
    IComponentScriptService componentScriptService;

    @Autowired
    IComponentOperationService componentOperationService;

    @Override
    public void modifyUpdateRecord(Long id, Long userId) {
        baseMapper.modifyUpdateRecord(id, userId);
        componentParamMapper.modifyUpdateRecord(id, userId);
        componentScriptMapper.modifyUpdateRecord(id, userId);
        componentOperationMapper.modifyUpdateRecord(id, userId);
    }



    /**
     * ???????????????
     * @param componentId
     * @return
     */
    @Override
    public List<IaasComponentVO> qryComponentInfo(Long componentId) {
        List<IaasComponentVO> iaasComponentVOS = baseMapper.qryComponentInfo(componentId);
        for (IaasComponentVO componentVO : iaasComponentVOS) {
            List<IaasComponentParamVO> compParams = componentVO.getCompParams();
            for (IaasComponentParamVO paramVO : compParams) {
                if (StringUtils.isNotBlank(paramVO.getValueList())) {
                    paramVO.setValueSelect(JSON.parseArray(paramVO.getValueList()));
                }
            }
        }
        return iaasComponentVOS;
    }


    /**
     * ??????????????????????????????vo
     * @param id
     * @return
     */
    @Override
    public IaasComponentVO qryComponentById(Long id) {
        IaasComponentVO vo = baseMapper.qryComponentById(id);
        List<IaasComponentParamVO> params;
        if(vo.getCompParams()!=null){
            params=vo.getCompParams();
            for(IaasComponentParamVO pvo : params)
            pvo.setValueSelect(JSON.parseArray(pvo.getValueList()));
        }
        return vo;
    }


    public IPage<IaasComponentVO> queryPage(Page page, Wrapper<IaasComponent> queryWrapper){
        return baseMapper.queryPage(page,queryWrapper);
    }


    @Override
    public boolean add(IaasComponentVO componentVo){
        boolean result = false;

        IaasComponent component = new IaasComponent();
        BeanUtils.copyProperties(componentVo, component);
        //??????????????????
//        String kind = component.getKind();

        component.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());

        if (componentOperationService.defaultAddition(component)) {
            if(this.save(component)){
                boolean paramRes=false;
                boolean scRes=false;
                //????????????
                if (componentVo.getCompParams() != null&&componentVo.getCompParams().size()>0) {
                    if(componentParamService.add(componentVo,component))
                        paramRes=true;
                }else{
                    log.info("???????????????");
                    paramRes=true;
                }
                //????????????
                if (componentVo.getCompScripts() != null&&componentVo.getCompScripts().size()>0) {
                    if(componentScriptService.add(componentVo,component))
                        scRes = true;
                }else{
                    log.info("???????????????");
                    scRes = true;
                }
                //add??? ?????????????????????key??????
                if(paramRes&&scRes)
                    result=true;
            }
        }
        return result;
    }


    private boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO) throws Exception {
        boolean result=false; //false ??????
        boolean reComp=false;
        boolean reParam=false;
        boolean reScript=false;
        boolean reOpVo=false;

//        String[] pmFields={"id","serialVersionUID","componentId","updateTime","updateUser","createUser","createTime"
//                ,"modifiable","tenantId","isDeleted","valueSelect","key"};
//        String[] scFields = {"id","serialVersionUID", "updateTime", "updateUser", "createUser",
//                "createTime", "tenantId","isDeleted","key","compOps"};
//        String[] opFields = {"id","serialVersionUID","componentId","scriptId","updateTime",
//                "updateUser","createUser","createTime" ,"tenantId","isDeleted","key","achieve"};

        log.info("??????????????????");
        List<IaasComponentParamVO> upParam = new ArrayList<>();
        if (!componentParamService.judge(componentVo,testVO,upParam)) {
            reParam=true;  //??????
            log.info("???????????? "+reParam);
        }
        List<IaasComponentScriptVO> upScript = new ArrayList<>();
         if(!componentScriptService.judge(componentVo,testVO,upScript))   {
             reScript=true;
             log.info("???????????? "+reScript);
         }
        List<IaasComponentOperationVO> update = new ArrayList<>();//???????????????
        if(!componentOperationService.judge(componentVo,testVO,update))   {
            reOpVo=true;
            log.info("???????????? "+reOpVo);
        }

        IaasComponent comp = new IaasComponent();
        BeanUtils.copyProperties(componentVo,comp);
        IaasComponent tcomp = new IaasComponent();
        BeanUtils.copyProperties(testVO,tcomp);
        if(CompareClassUtil.compare(comp,tcomp,IaasComponent.class,ignoreFields)){
            reComp=true;
            log.info("???????????? "+reComp);
        }

        if( !reOpVo || !reParam || !reScript || !reComp) {  //!reOpVo || !reParam || !reScript || !reComp
            result = false;  //????????????
            log.info("?????????????????? ");
        }else{
            result = true;
            log.info("????????????????????? ");
        }
        return result;
    }

    /**
     *
     * @param componentVo
     * @param baseResponse
     * @return
     * @throws Exception
     * ????????????????????????????????????????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponse updateComponent(IaasComponentVO componentVo,BaseResponse baseResponse)
            throws Exception {
        //??????????????????  id????????????
        IaasComponentVO testVO = this.qryUnionComponent(componentVo.getId());//this.qryOneComponent(componentVo.getId());
        log.info("updateComponent - testVO "+testVO);
        log.info("updateComponent - componentVo "+componentVo);

//        this.judge(componentVo,testVO);
        //???????????????id
        Long testId=testVO.getId();
        QueryWrapper<IaasComponent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasComponent::getId, testId);
        int count=this.count(queryWrapper);
        log.info("updateComponent - count "+count);
        if(count<1){  //testId?????????
            //??????
            log.info("updateComponent ??????????????????????????? ?????????????????????");
            IaasComponent component = new IaasComponent();
            BeanUtils.copyProperties(componentVo, component);
            component.setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());
           this.save(component);
        }else{ //??????  testId??????  ???????????????????????????
//            if(CompareClassUtil.compare(componentVo,testVO,IaasComponentVO.class,ignoreFields)){
            if(this.judge(componentVo,testVO)){
                //????????????????????????
                log.info("????????????????????????????????? ");
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("?????????????????????????????????");
                return baseResponse;
            }
            //??????????????????
            IaasComponent component =this.getById(testId);
            log.info("updateComponent - component "+component);
            //?????????????????????????????????
            IaasComponentHistory compHis=this.compHisInsert(component);
            log.info("updateComponent - compHis "+compHis);
            //??????????????????????????????
            BaseResponse paramResponse=componentParamService.saveComponentParamHistory(componentVo,testVO,
                    compHis,compHisParamService,baseResponse);
            BaseResponse scriptResponse=
                    componentScriptService.saveComponentScriptHistory(componentVo,testVO,
                            compHis,compHisScriptService,compHisOperationService,baseResponse);
            //??????sc????????????????????????????????????
            componentOperationService.saveComponentOpHistory(componentVo,testVO,
                            compHis,compHisOperationService,compHisScriptService,baseResponse);
            if(paramResponse.getCode()!=ResultEnum.DEFAULT_SUCCESS.getCode() ||
                    scriptResponse.getCode()!=ResultEnum.DEFAULT_SUCCESS.getCode()){
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("??????????????????");
                return baseResponse;
            }else{
                //????????????
                int version = component.getVersion();
                BeanUtils.copyProperties(componentVo, component);//new IaasComponent();
                component.setVersion(version+1);
                //??????????????????
                component.setUpdateUser(Sign.getUserId());
                component.setUpdateTime(DateUtil.getNow());
                if (this.updateById(component)) {
                    log.info("updateComponent updateById ????????????????????? ");
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("??????????????????");
                }else log.info("????????????????????? ");
            }
        }
        return baseResponse;
    }

//    @Override
//    public List<IaasComponentVO> qryComponents() {
//        return baseMapper.qryComponents();
//    }

    /**
     * ??????vo???????????????????????????id??????
     * @param id
     * @return
     */
//    @Override
//    public IaasComponentVO qryOneComponent(Long id) {
//        IaasComponentVO vo = baseMapper.qryOneComponent(id);
//        List<IaasComponentParamVO> params;
//        if(vo.getCompParams()!=null){
//            params=vo.getCompParams();
//            for(IaasComponentParamVO pvo : params)
//                pvo.setValueSelect(JSON.parseArray(pvo.getValueList()));
//        }
//        return vo;
//    }

    /**
     * ???????????????VO????????????json??????????????????
     * @param id
     * @return
     */
    @Override
    public IaasComponentVO qryUnionComponent(Long id) {
        IaasComponentVO vo = baseMapper.qryUnionComponent(id);
        List<IaasComponentParamVO> params;
        if(vo.getCompParams()!=null){
            params=vo.getCompParams();
            for(IaasComponentParamVO pvo : params)
                pvo.setValueSelect(JSON.parseArray(pvo.getValueList()));
        }
        return vo;
    }


    /**
     * ????????????????????????
     * @param component
     * @return
     */
    @Override
    public IaasComponentHistory compHisInsert(IaasComponent component){
        IaasComponentHistory compHis = new IaasComponentHistory();
        BeanUtils.copyProperties(component, compHis, "id", "componentId","tenantId");
        compHis.setId(SnowFlakeIdGenerator.getInstance().nextId());
        compHis.setComponentId(component.getId());
        compHis.setCreateUser(Sign.getUserId());
        compHis.setHistoryTime(DateUtil.getNow());
        compHis.setUpdateUser(Sign.getUserId());
        compHis.setUpdateTime(DateUtil.getNow());
        compHis.setHistoryTime(DateUtil.getNow());
        log.info("????????????????????? "+compHis);
        compHisService.save(compHis);
        return compHis;
    }


}

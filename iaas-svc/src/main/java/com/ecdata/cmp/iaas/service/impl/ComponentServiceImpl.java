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
     * 虚拟机调用
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
     * 一对多嵌套关系的单个vo
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
        //待扩展的条件
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
                //有无参数
                if (componentVo.getCompParams() != null&&componentVo.getCompParams().size()>0) {
                    if(componentParamService.add(componentVo,component))
                        paramRes=true;
                }else{
                    log.info("组件无参数");
                    paramRes=true;
                }
                //有无脚本
                if (componentVo.getCompScripts() != null&&componentVo.getCompScripts().size()>0) {
                    if(componentScriptService.add(componentVo,component))
                        scRes = true;
                }else{
                    log.info("组件无脚本");
                    scRes = true;
                }
                //add时 操作在脚本中按key对应
                if(paramRes&&scRes)
                    result=true;
            }
        }
        return result;
    }


    private boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO) throws Exception {
        boolean result=false; //false 不同
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

        log.info("组件更新判断");
        List<IaasComponentParamVO> upParam = new ArrayList<>();
        if (!componentParamService.judge(componentVo,testVO,upParam)) {
            reParam=true;  //相同
            log.info("参数更新 "+reParam);
        }
        List<IaasComponentScriptVO> upScript = new ArrayList<>();
         if(!componentScriptService.judge(componentVo,testVO,upScript))   {
             reScript=true;
             log.info("脚本更新 "+reScript);
         }
        List<IaasComponentOperationVO> update = new ArrayList<>();//更新的集合
        if(!componentOperationService.judge(componentVo,testVO,update))   {
            reOpVo=true;
            log.info("操作更新 "+reOpVo);
        }

        IaasComponent comp = new IaasComponent();
        BeanUtils.copyProperties(componentVo,comp);
        IaasComponent tcomp = new IaasComponent();
        BeanUtils.copyProperties(testVO,tcomp);
        if(CompareClassUtil.compare(comp,tcomp,IaasComponent.class,ignoreFields)){
            reComp=true;
            log.info("组件更新 "+reComp);
        }

        if( !reOpVo || !reParam || !reScript || !reComp) {  //!reOpVo || !reParam || !reScript || !reComp
            result = false;  //有不相同
            log.info("组件需要更新 ");
        }else{
            result = true;
            log.info("组件不需要更新 ");
        }
        return result;
    }

    /**
     *
     * @param componentVo
     * @param baseResponse
     * @return
     * @throws Exception
     * 生命周期和脚本文件如果删除了的话要关联空
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponse updateComponent(IaasComponentVO componentVo,BaseResponse baseResponse)
            throws Exception {
        //主表需要更新  id确定有了
        IaasComponentVO testVO = this.qryUnionComponent(componentVo.getId());//this.qryOneComponent(componentVo.getId());
        log.info("updateComponent - testVO "+testVO);
        log.info("updateComponent - componentVo "+componentVo);

//        this.judge(componentVo,testVO);
        //传入组件的id
        Long testId=testVO.getId();
        QueryWrapper<IaasComponent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasComponent::getId, testId);
        int count=this.count(queryWrapper);
        log.info("updateComponent - count "+count);
        if(count<1){  //testId无意义
            //新增
            log.info("updateComponent 数据库中没有该组件 新增，不入历史");
            IaasComponent component = new IaasComponent();
            BeanUtils.copyProperties(componentVo, component);
            component.setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow())
                    .setUpdateUser(Sign.getUserId())
                    .setUpdateTime(DateUtil.getNow());
           this.save(component);
        }else{ //更新  testId存在  可能删除，可能编辑
//            if(CompareClassUtil.compare(componentVo,testVO,IaasComponentVO.class,ignoreFields)){
            if(this.judge(componentVo,testVO)){
                //内容相同无需更新
                log.info("内容相同，无需更新组件 ");
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("内容相同，无需更新组件");
                return baseResponse;
            }
            //确实需要更新
            IaasComponent component =this.getById(testId);
            log.info("updateComponent - component "+component);
            //塞入历史，添加关联记录
            IaasComponentHistory compHis=this.compHisInsert(component);
            log.info("updateComponent - compHis "+compHis);
            //历史入了，更新没执行
            BaseResponse paramResponse=componentParamService.saveComponentParamHistory(componentVo,testVO,
                    compHis,compHisParamService,baseResponse);
            BaseResponse scriptResponse=
                    componentScriptService.saveComponentScriptHistory(componentVo,testVO,
                            compHis,compHisScriptService,compHisOperationService,baseResponse);
            //放在sc中循环塞入历史会变成平方
            componentOperationService.saveComponentOpHistory(componentVo,testVO,
                            compHis,compHisOperationService,compHisScriptService,baseResponse);
            if(paramResponse.getCode()!=ResultEnum.DEFAULT_SUCCESS.getCode() ||
                    scriptResponse.getCode()!=ResultEnum.DEFAULT_SUCCESS.getCode()){
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("更新组件失败");
                return baseResponse;
            }else{
                //本身更新
                int version = component.getVersion();
                BeanUtils.copyProperties(componentVo, component);//new IaasComponent();
                component.setVersion(version+1);
                //修改人和时间
                component.setUpdateUser(Sign.getUserId());
                component.setUpdateTime(DateUtil.getNow());
                if (this.updateById(component)) {
                    log.info("updateComponent updateById 组件表更新成功 ");
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("更新组件成功");
                }else log.info("组件表更新失败 ");
            }
        }
        return baseResponse;
    }

//    @Override
//    public List<IaasComponentVO> qryComponents() {
//        return baseMapper.qryComponents();
//    }

    /**
     * 组件vo非嵌套一对多关系的id查询
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
     * 一对多完整VO，参数是json数组下拉列表
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
     * 插入组件主表历史
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
        log.info("组件历史表插入 "+compHis);
        compHisService.save(compHis);
        return compHis;
    }


}

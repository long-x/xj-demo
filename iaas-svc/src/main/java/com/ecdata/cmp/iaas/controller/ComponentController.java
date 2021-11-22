package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.component.*;
import com.ecdata.cmp.iaas.entity.dto.component.*;
import com.ecdata.cmp.iaas.entity.dto.response.component.*;
import com.ecdata.cmp.iaas.mapper.component.ComponentMapper;
import com.ecdata.cmp.iaas.service.IComponentHistoryService;
import com.ecdata.cmp.iaas.service.IComponentOpHistoryService;
import com.ecdata.cmp.iaas.service.IComponentOperationService;
import com.ecdata.cmp.iaas.service.IComponentParamHistoryService;
import com.ecdata.cmp.iaas.service.IComponentParamService;
import com.ecdata.cmp.iaas.service.IComponentScriptHistoryService;
import com.ecdata.cmp.iaas.service.IComponentScriptService;
import com.ecdata.cmp.iaas.service.IComponentService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:00,
 */

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/component")
@Api(tags = "组件相关的API")
public class ComponentController {
//op依赖于sc，先script，script可能被多个op共用
    /*
     *组件Service
     */

    private String[] ignoreFields = {"updateTime","updateUser","createUser","createTime","tenantId"};

//    @Autowired
//    private IUserService userService;

    @Autowired
    ComponentMapper componentMapper;

    @Autowired
    IComponentService componentService;

    @Autowired
    IComponentOperationService componentOperationService;

    @Autowired
    IComponentScriptService componentScriptService;

    @Autowired
    IComponentParamService componentParamService;


/*
历史service
 */

    @Autowired
    IComponentHistoryService compHisService;

    @Autowired
    IComponentOpHistoryService compHisOperationService;

    @Autowired
    IComponentScriptHistoryService compHisScriptService;

    @Autowired
    IComponentParamHistoryService compHisParamService;


    /*
    组件单表id查询
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询组件", notes = "根据id查询组件")
    @ApiImplicitParam(name = "id", value = "组件id", required = true, paramType = "path")
    public ResponseEntity<ComponentResponse> getById(@PathVariable(name = "id") Long id) {
        IaasComponent component = componentService.getById(id);
        IaasComponentVO iaasComponentVO = new IaasComponentVO();
        BeanUtils.copyProperties(component, iaasComponentVO);
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentResponse(iaasComponentVO));
    }

    /**
     * 分页vo+下拉框选项
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查看组件", notes = "分页查看组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ComponentMapResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                     @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                     @RequestParam(required = false) String keyword) {
        QueryWrapper<IaasComponent> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(obj->(
                    obj.like(IaasComponent::getDisplayName, keyword)
                            .or().like(IaasComponent::getKind, keyword))
            );
        }
        queryWrapper.lambda().orderByDesc(IaasComponent::getUpdateTime);

        Page<IaasComponent> page = new Page<>(pageNo, pageSize);
//        IPage<IaasComponent> result = componentService.page(page, queryWrapper);
        IPage<IaasComponentVO> result=componentService.queryPage(page,queryWrapper);
        List<IaasComponentVO> componentVOList = result.getRecords();//new ArrayList<>()
//        List<IaasComponent> componentList = result.getRecords();
//        if (componentList != null && componentList.size() > 0) {
//            for (IaasComponent component : componentList) {
//                IaasComponentVO componentVo = new IaasComponentVO();
//                BeanUtils.copyProperties(component, componentVo);
//                if(component.getCreateUser()!= null){
//                    User user = new User();
//                    user.set(component.getCreateUser());
//                }
//                    componentVo.setUname(.getById(component.getCreateUser()).getName());
//                componentVOList.add(componentVo);
//            }
//        }

        for(IaasComponentVO vo :componentVOList){
            if(null!=vo.getCompParams()) {
                for(IaasComponentParamVO pv:vo.getCompParams()){
                    pv.setKey(vo.getId().toString());
                }
            }
//            else log.info("page no params");
            if(null!=vo.getCompScripts()){
                for(IaasComponentScriptVO sv:vo.getCompScripts()){
                    sv.setKey(vo.getId().toString());
                }
            }
//            else log.info("page no scripts");
        }

        System.out.println(componentVOList.size());
        componentVOList.forEach(System.out::println);

        Map<String, Object> map = this.selection(result,componentVOList);
        //new ComponentPageResponse(new PageVO<>(result, componentVOList))
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentMapResponse(map));

    }

    /**
     * 仅仅是实体类对应集合
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取组件列表", notes = "获取组件列表")
    public ResponseEntity<ComponentListResponse> list() {
        List<IaasComponentVO> compVOList = new ArrayList<>();
        List<IaasComponent> compList = componentService.list();
        System.out.println(compList.size());
        compList.forEach(System.out::println);
        if (compList != null && compList.size() > 0) {
            for (IaasComponent comp : compList) {
                IaasComponentVO compVO = new IaasComponentVO();
                BeanUtils.copyProperties(comp, compVO);
                compVOList.add(compVO);
            }
        }
//        单表list
//=============================================================================
        //关联查询
//        List<IaasComponentVO> compVOList = componentService.qryComponentInfo();
//        compVOList.forEach(System.out::println);
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentListResponse(compVOList));
    }

    /**
     * 一对多嵌套关系的单个vo
     * @param id
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取组件列表", notes = "获取组件列表")
    public ResponseEntity<ComponentResponse> info(@RequestParam Long id) {
        IaasComponentVO compVOList = componentService.qryComponentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentResponse(compVOList));
    }

    /**
     * 组件vo非嵌套一对多关系的id查询
     * @param id
     * @return
     */
    @GetMapping("/vo_info")
    @ApiOperation(value = "获取组件vo关联列表", notes = "获取组件vo关联列表")
    public ResponseEntity<ComponentResponse> voInfo(@RequestParam Long id) {
        IaasComponentVO compVO =componentService.qryUnionComponent(id); //componentService.qryOneComponent(id);
        if(null!=compVO.getCompParams()) {
            for(IaasComponentParamVO pv:compVO.getCompParams()){
                pv.setKey(compVO.getId().toString());
            }
        }
        if(null!=compVO.getCompScripts()){
            compVO.getCompScripts().forEach((IaasComponentScriptVO sv) ->
                    sv.setKey(sv.getId().toString()));
        }

        if(null!=compVO.getCompOpVOs()){
            compVO.getCompOpVOs().forEach((IaasComponentOperationVO op) ->
                    op.setKey(op.getId().toString()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ComponentResponse(compVO));
    }



    @PostMapping("/add")
    @ApiOperation(value = "新增组件", notes = "新增组件")
    public ResponseEntity<BaseResponse> add(@RequestBody IaasComponentVO componentVo,
                                            HttpServletRequest request) {


        BaseResponse baseResponse = new BaseResponse();
        if(componentService.add(componentVo)){
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加组件成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加组件失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }
//只要调用  默认一定有更新，别管没更新的
    @PutMapping("/update")
    @ApiOperation(value = "修改组件", notes = "修改组件")
    public ResponseEntity<BaseResponse> update(@RequestBody IaasComponentVO componentVo)
            throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Long id = componentVo.getId();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                componentService.updateComponent(componentVo,baseResponse));

    }




    @PutMapping("/remove/{id}")
    @ApiOperation(value = "删除", notes = "删除组件")
    @ApiImplicitParam(name = "id", value = "组件id", required = true, paramType = "path")
    public ResponseEntity<BaseResponse> remove(@PathVariable(name = "id") Long id) {
        log.info("删除组件 component id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        //判断有无该id的记录
         /*
            内部关联删除
             */
        QueryWrapper<IaasComponentParam> pmQuery = new QueryWrapper<>();
        QueryWrapper<IaasComponentOperation> opQuery = new QueryWrapper<>();
        QueryWrapper<IaasComponentScript> scQuery = new QueryWrapper<>();
        pmQuery.lambda().eq(IaasComponentParam::getComponentId, id);
        opQuery.lambda().eq(IaasComponentOperation::getComponentId, id);
        scQuery.lambda().eq(IaasComponentScript::getComponentId, id);
        componentOperationService.remove(opQuery);
        componentScriptService.remove(scQuery);
        componentParamService.remove(pmQuery);

        if (componentService.removeById(id)) {
            componentService.modifyUpdateRecord(id, Sign.getUserId());
            componentParamService.modifyUpdateRecord(id, Sign.getUserId());
            componentScriptService.modifyUpdateRecord(id, Sign.getUserId());
            componentOperationService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除组件(逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        /*
        判断有无ids
         */
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            /*
            关联删除
             */
            for (String id : idArray) {
                QueryWrapper<IaasComponentParam> pmQuery = new QueryWrapper<>();
                QueryWrapper<IaasComponentOperation> opQuery = new QueryWrapper<>();
                QueryWrapper<IaasComponentScript> scQuery = new QueryWrapper<>();
                pmQuery.lambda().eq(IaasComponentParam::getComponentId, id);
                opQuery.lambda().eq(IaasComponentOperation::getComponentId, id);
                scQuery.lambda().eq(IaasComponentScript::getComponentId, id);
                componentOperationService.remove(opQuery);
                componentScriptService.remove(scQuery);
                componentParamService.remove(pmQuery);
            }
            if (componentService.removeByIds(Arrays.asList(idArray))) {
                for (String id : idArray) {
                    componentService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    componentParamService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    componentScriptService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    componentOperationService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                }

                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("删除成功");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            } else {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("删除失败");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
        }
    }


    /**
     * 虚拟机调用
     * @param componentId
     * @return
     */
    @GetMapping("/query_components")
    @ApiOperation(value = "获取组、组件参数及脚本表", notes = "获取组、组件参数及脚本表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "componentId", value = "组件id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<ComponentListResponse> queryComponentList(@RequestParam(value = "componentId",
             required = false) Long componentId) {
        List<IaasComponentVO> compList = componentService.qryComponentInfo(componentId);
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentListResponse(compList));
    }
    //==============================================================================

    /**
     * 分页中添加的信息
     * @param result    IPage<IaasComponent> || IPage<IaasComponentVO>
     * @param componentVOList
     * @return
     */
    public Map<String, Object> selection(IPage<IaasComponentVO> result,
                                         List<IaasComponentVO> componentVOList){
//组件详情
        List<Map<String, Object>> osType = new ArrayList<>();
        Map<String, Object> windows = new LinkedHashMap<>();
        windows.put("key","Windows");
        windows.put("value",1);
        Map<String, Object> linux = new LinkedHashMap<>();
        linux.put("key","Linux");
        linux.put("value",2);
        osType.add(windows);
        osType.add(linux);

        List<Map<String, Object>> initType = new ArrayList<>();
        Map<String, Object> uninit = new LinkedHashMap<>();
        uninit.put("key","非初始组件");
        uninit.put("value","0");
        Map<String, Object> sup = new LinkedHashMap<>();
        sup.put("key","顶级租户初始组件");
        sup.put("value","1");
        Map<String, Object> sub = new LinkedHashMap<>();
        sub.put("key","子租户租户初始组件");
        sub.put("value","2");
        initType.add(uninit);
        initType.add(sup);
        initType.add(sub);

        //参数详情
        String[] paramType={"int","double","string","boolean","text"};//,"password"

        List<Map<String, Object>> require = new ArrayList<>();
        Map<String, Object> ness = new LinkedHashMap<>();
        ness.put("key","非必须");
        ness.put("value","0");
        Map<String, Object> uness = new LinkedHashMap<>();
        uness.put("key","必须");
        uness.put("value","1");
        require.add(ness);
        require.add(uness);

        //脚本详情
        String[] scriptType={"sh","powershell","python","ruby","groovy","batchfile",
                "text","xml"};
        //操作详情
        String[] opType={"create","configure","start","stop","restart","delete",
                "other"};
        //总map
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("pageData",new PageVO<>(result, componentVOList));
        //组件详细
        map.put("osType",osType); //系统类型
        map.put("type",initType); //类别
        //参数
        map.put("paramType",paramType);
        map.put("require",require);
        //脚本
        map.put("scriptType",scriptType);
        //操作
        map.put("opType",opType);

        return map;
    }


    @GetMapping("/version_page")
    @ApiOperation(value = "历史版本", notes = "历史版本")
    public ResponseEntity<ComponentHisListResponse> versionPage(@RequestParam(value = "componentId",
            required = false) Long componentId) {
        List<IaasComponentHistoryVO> hisList = compHisService.qryVersion(componentId);
        log.info("versionPage "+componentId +" "+hisList);
//        List<IaasComponentHistoryVO> list = new ArrayList<>();
//        for(IaasComponentHistory ich:hisList){
//            IaasComponentHistoryVO vo = new IaasComponentHistoryVO();
//            BeanUtils.copyProperties(ich,vo);
//            list.add(vo);
//        }
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentHisListResponse(hisList));
    }

    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/del_version_patch")
    @ApiOperation(value = "删除历史", notes = "删除历史")
    public ResponseEntity<BaseResponse> delVersion(@RequestParam(name = "ids") String ids) {//相当于componentIds+version
        log.info("批量删除 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");

        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            for (String id : idArray) {
                IaasComponentHistory ich = compHisService.getById(id);
                Long delId = ich.getComponentId();
                Integer version = ich.getVersion();
                log.info("正在删除 " + delId + "组件 " + version + "版本");

                QueryWrapper<IaasComponentParamHistory> pmQuery = new QueryWrapper<>();
                QueryWrapper<IaasComponentOpHistory> opQuery = new QueryWrapper<>();
                QueryWrapper<IaasComponentScriptHistory> scQuery = new QueryWrapper<>();

                pmQuery.lambda().eq(IaasComponentParamHistory::getComponentHistoryId, id);
                compHisParamService.remove(pmQuery);

                scQuery.lambda().eq(IaasComponentScriptHistory::getComponentHistoryId, id);
                compHisScriptService.remove(scQuery);

                opQuery.lambda().eq(IaasComponentOpHistory::getComponentHistoryId, id);
                compHisOperationService.remove(opQuery);

            }
            if (compHisService.removeByIds(Arrays.asList(idArray))) {
                for (String id : idArray) {
                    compHisService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    compHisParamService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    compHisScriptService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    compHisOperationService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                }
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("删除成功");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

            } else {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("删除失败");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/roll_back")
    @ApiOperation(value = "恢复版本", notes = "恢复版本")
    public ResponseEntity<BaseResponse> rollBack(@RequestParam(value = "compHisId") Long id
                                                 ) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        IaasComponentHistoryVO hisVO = compHisService.qryUnionHistory(id);//qryCompHisInfo(id);//qryUnionHistory

        log.info("回滚到 "+hisVO.getVersion()+"版本");
        IaasComponentVO icvo= new IaasComponentVO();
        BeanUtils.copyProperties(hisVO,icvo);
        List<IaasComponentParamVO> pvos = new ArrayList<>();
        List<IaasComponentScriptVO> scvos = new ArrayList<>();
        List<IaasComponentOperationVO> opvos = new ArrayList<>();
        if(hisVO.getCompHisParams()!=null){
            List<IaasComponentParamHistoryVO> phvos = hisVO.getCompHisParams();
            phvos.forEach((IaasComponentParamHistoryVO phvo)->{
                IaasComponentParamVO  pvo = new IaasComponentParamVO();
//                phvo.setId(phvo.getComponentParamId());
                phvo.setId(SnowFlakeIdGenerator.getInstance().nextId());
                BeanUtils.copyProperties(phvo,pvo);
                pvos.add(pvo);
            });

            phvos.forEach((IaasComponentParamHistoryVO vo)->System.out.println("回滚的pmvoid "+vo.getId()));
//log.info("回滚的pmvo "+)
        }
        if(hisVO.getCompHisScripts()!=null){
            List<IaasComponentScriptHistoryVO> schvos = hisVO.getCompHisScripts();
            schvos.forEach((IaasComponentScriptHistoryVO schvo)->{
                IaasComponentScriptVO  scvo = new IaasComponentScriptVO();
//                schvo.setId(schvo.getComponentScriptId());
                schvo.setId(SnowFlakeIdGenerator.getInstance().nextId());
                BeanUtils.copyProperties(schvo,scvo);
                scvo.setKey(String.valueOf(schvo.getComponentScriptId()));
                scvos.add(scvo);
            });


//            log.info("回滚的scvo "+scvos);
        }
        if(hisVO.getCompHisOps()!=null){
            List<IaasComponentOpHistoryVO> ophvos = hisVO.getCompHisOps();
            ophvos.forEach((IaasComponentOpHistoryVO ophvo)->{
                IaasComponentOperationVO  opvo = new IaasComponentOperationVO();
//                ophvo.setId(ophvo.getComponentOperationId());
                ophvo.setId(SnowFlakeIdGenerator.getInstance().nextId());
                BeanUtils.copyProperties(ophvo,opvo);
                opvos.add(opvo);
            });
        }

        for(IaasComponentOperationVO opvo:opvos){
            for(IaasComponentScriptVO scvo : scvos){
                if(null!=opvo.getScriptId()){
                    if(opvo.getScriptId().toString().equals(scvo.getKey())){
                        opvo.setScriptId(scvo.getId());
//                    log.info("opvo set sc");
                    }
                }else  opvo.setScriptId(null);


            }
        }
        scvos.forEach((IaasComponentScriptVO vo)->System.out.println("回滚的scvoid "+vo.getId()+" "+
                vo.getScriptName()+" "+vo.getKey()));
        opvos.forEach((IaasComponentOperationVO vo)->System.out.println("回滚的opvoid "+vo.getId()+" "+
                vo.getScriptId()+" "+vo.getOperationName()));

        icvo.setId(hisVO.getComponentId());
        icvo.setCompParams(pvos);
        icvo.setCompScripts(scvos);
        icvo.setCompOpVOs(opvos);
        compHisService.rollBack(hisVO,icvo);

        log.info("回滚的目标vo "+icvo);//.setMessage("回退成功")
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
                //componentService.updateComponent(icvo,baseResponse)
    }

    @GetMapping("/history")
    @ApiOperation(value = "历史版本", notes = "历史版本")
    public ResponseEntity<ComponentHisResponse> history(@RequestParam(value = "componentId",
            required = false) Long componentId) {
        //不能是ophis.id应该是ophis.opid
        IaasComponentHistoryVO hvo = compHisService.qryUnionHistory(componentId);
        return ResponseEntity.status(HttpStatus.OK).body(new ComponentHisResponse(hvo));
    }

}

package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.BaremetalServersFlavorsClient;
import com.ecdata.cmp.huawei.dto.response.BaremetalListResponse;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.*;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecdata.cmp.iaas.entity.dto.response.MetalMapResponse;
import com.ecdata.cmp.iaas.mapper.*;
import com.ecdata.cmp.iaas.service.IProjectService;
import com.ecdata.cmp.iaas.service.IResourcePoolService;
import com.ecdata.cmp.iaas.service.IaasBareMetalService;
import com.ecdata.cmp.iaas.entity.IaasBareMetal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/4/24 14:35
 */
@Slf4j
@Service
public class IaasBareMetalServiceImpl extends ServiceImpl<IaasBareMetalMapper, IaasBareMetal> implements IaasBareMetalService {
    @Autowired
    private IaasVirtualDataCenterMapper iaasVirtualDataCenterMapper;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IaasProviderMapper iaasProviderMapper;
    @Autowired
    private ProviderServiceImpl providerService;
    @Autowired
    private IResourcePoolService resourcePoolService;
    @Autowired
    private IaasProjectMapper iaasProjectMapper;
    @Autowired
    private IaasAreaMapper iaasAreaMapper;
    @Autowired
    private IaasVirtualDataCenterMapper virtualDataCenterMapper;
    @Autowired
    private BaremetalServersFlavorsClient baremetalServersFlavorsClient;


    @Override
    public IPage<BareMetalVO> getBareMetalVOPage(Page<BareMetalVO> page, Map map) {
        return baseMapper.getBareMetalVOPage(page,map);
    }


    //纳管虚拟机或裸金属
    @Override
    public MetalMapResponse nanotubeResource(Long projectId) {//Long id,
        MetalMapResponse metalMapResponse = new MetalMapResponse();
        Map<String,Object> result = new HashMap<>();
        List<BareMetalVO> remoteBMs = new ArrayList<>();
        List<BareMetalVO> localVOs = new ArrayList<>();

        IaasProject iaasProject = projectService.queryIaasProjectById(projectId);
        String projectKey = iaasProject.getProjectKey();
        if (projectKey == null) {
            return metalMapResponse;
        }
        //远程裸金属
        BaremetalListResponse bmsListResponse = baremetalServersFlavorsClient.getBaremetalList(String.valueOf(projectId));
        if (bmsListResponse == null || bmsListResponse.getCode() != 0 || CollectionUtils.isEmpty(bmsListResponse.getData())) {
            metalMapResponse.setCode(201);
            metalMapResponse.setMessage("远程裸金属暂无数据!");
            return metalMapResponse;
        }
        List<BareMetalVO> remoteMetals = bmsListResponse.getData();

//        List<IaasApplyConfigInfoVO> ljsList = configInfoVOList.stream().filter(item -> "2".equals(item.getApplyType())).collect(toList());

        //查库 本地裸金属
        QueryWrapper<IaasBareMetal> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasBareMetal::getProjectId,projectId);
        List<IaasBareMetal> localMetals = this.list(queryWrapper);


        //nativeId是远程裸金属唯一id

        if(CollectionUtils.isNotEmpty(localMetals)){
            List<String> collect = localMetals.stream().map(e -> {
                return e.getNativieId();
            }).collect(Collectors.toList());

            remoteMetals = remoteMetals.stream().filter(allBM -> !collect.contains(allBM.getNativieId()))
                    .collect(Collectors.toList());
        }

        for(IaasBareMetal ibm:localMetals){
            BareMetalVO vo = new BareMetalVO();
            BeanUtils.copyProperties(ibm,vo);
            localVOs.add(vo);
        }
        //vo.setTitle(vo.getVmName());  vo.setKey(vo.getVmKey());

        remoteBMs =remoteMetals.stream().filter(item -> !localMetals.stream().map(e -> e.getNativieId())
                .collect(Collectors.toList()).contains(item.getNativieId())).collect(Collectors.toList());
        remoteBMs.forEach((BareMetalVO vo)->{vo.setKey(vo.getNativieId());vo.setBinded(0);vo.setTitle(vo.getName());});//vo.setKey(vo.getVmKey()); vo.setTitle(vo.getVmName());

//        remoteBMs.forEach((BareMetalVO vo)->vo.setKey(vo.getNativieId()));

//        List<BareMetalVO> ngljs = jiaojiVM(bareMetalVOS, LocalMetals);

        result.put("remoteBMs",remoteMetals);
        if(CollectionUtils.isNotEmpty(localVOs)) {
            localVOs.forEach((BareMetalVO vo) -> {
                vo.setKey(vo.getNativieId());
                vo.setTitle(vo.getName());
                vo.setBinded(1);
            });
        }
        localVOs.addAll(remoteBMs);

        result.put("localBMs",localVOs);
        metalMapResponse.setData(result);
//        if (CollectionUtils.isNotEmpty(ngljs)) {
////            handleBareMetal(vo.businessGroupId(), ngljs);
//        }
        return metalMapResponse;
    }

//    @Transactional(rollbackFor = Exception.class)
    public BaseResponse removeLocalBM(List<String> vmKeyList){
    BaseResponse baseResponse = new BaseResponse();
    List<Long> bmIds = new ArrayList<>();
    for(String ids :vmKeyList){
        QueryWrapper<IaasBareMetal> qkeys =new QueryWrapper<>();
        qkeys.eq("nativie_id", ids.trim());
        qkeys.eq("is_deleted", 0);
        IaasBareMetal one =baseMapper.selectOne(qkeys);
        if(one!=null&&one.getId()!=null){
            bmIds.add(one.getId());
        }
    }

//    vmKeyList.forEach((String s)->{
//        IaasBareMetal ibm = this.getOne(qkeys.lambda().eq(IaasBareMetal::getNativieId,s));
//        log.info("ibm "+ibm);
//        if(ibm!=null&&ibm.getId()!=null){
//            bmIds.add(ibm.getId());
//        }
//    });
    log.info("bmids "+bmIds);
    if (bmIds.size()>0) {
        if(this.removeByIds(bmIds)){
            baseResponse.setCode(200);
            baseResponse.setMessage("裸金属解绑成功");
        }else{
            baseResponse.setCode(201);
            baseResponse.setMessage("裸金属解绑失败");
        }
    }
    return baseResponse;
}



    //纳管保存裸金属
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncUpdate(ResourcePoolVO resourcePoolVO) {
        BaseResponse baseResponse = new BaseResponse();
        List<String> nativeKeyList = new ArrayList<>(resourcePoolVO.getVmKeyList());
        log.info("nativeKeyList ",nativeKeyList);


        //纳管虚拟机或裸金属
        String authz = AuthContext.getAuthz();

        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryIaasProviderVOById(resourcePoolVO.getProviderId());
        if (iaasProviderVO == null) {
            log.warn("获取供应商信息为空!");
            return baseResponse;
        }

        //1.通过用户名密码获取供应商token
//        TokenInfoResponse tokenResponse = providerService.getToken(authz, iaasProviderVO);

//        if (tokenResponse == null) {
//            log.warn("获取供应商token错误!");
//            return baseResponse;
//        }

        //获取项目信息获取token
        IaasProject iaasProject = projectService.queryIaasProjectById(resourcePoolVO.getProjectId());
        log.info("iaasProject "+iaasProject);
        Long projectId = iaasProject.getId();
        if (projectId == null) {
            return baseResponse;
        }
        BaremetalListResponse bmsListResponse = baremetalServersFlavorsClient.getBaremetalList(String.valueOf(projectId));
        log.info("bmsListResponse "+bmsListResponse);
        if (bmsListResponse == null || bmsListResponse.getCode() != 0 || CollectionUtils.isEmpty(bmsListResponse.getData())) {
            return baseResponse;
        }

        List<BareMetalVO> BareMetalVOS = bmsListResponse.getData();
        log.info("BareMetalVOS "+BareMetalVOS);
        String[] businessIds = null;
        businessIds = resourcePoolVO.getBusinessIds().split(",");
        log.info("businessIds "+businessIds);
        Long bid = Long.valueOf(businessIds[0]);
        if (CollectionUtils.isNotEmpty(BareMetalVOS)) {
            handleBareMetal(bid, BareMetalVOS);
        }

        //存在事务问题,应该先执行上面 再清空本地
        //清空裸金属
        QueryWrapper<IaasBareMetal> query = new QueryWrapper<>();
        query.eq("project_id", resourcePoolVO.getProjectId());
        query.eq("is_deleted", 0);
        List<IaasBareMetal> iaasBareMetals = this.list(query);
        List<String> localNativeList = iaasBareMetals.stream()
                .map(e -> {
                    return e.getNativieId();
                }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(localNativeList)) {
            List<String> needRemoveList = new ArrayList<>(localNativeList);
            //vmKeyList为空 只移除本地纳管虚拟机
            if (CollectionUtils.isEmpty(nativeKeyList)) {
                removeLocalBM(needRemoveList);
                return baseResponse;
            }

            //解除本地 解绑的纳管的 nativeList
            needRemoveList.removeAll(nativeKeyList);
            removeLocalBM(needRemoveList);
        } else if (CollectionUtils.isEmpty(localNativeList) && CollectionUtils.isEmpty(nativeKeyList)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("没有可以纳管的裸金属");
            return baseResponse;
        }


        int code = ResultEnum.DEFAULT_SUCCESS.getCode();
        baseResponse.setCode(code);
        return baseResponse;
    }

    private void handleBareMetal(Long businessGroupId, List<BareMetalVO> ngljs) {
        //也有未绑定的传入，需要设成bind0
        QueryWrapper<IaasBareMetal> local = new QueryWrapper<>();
        local.lambda().eq(IaasBareMetal::getBusinessGroupId,businessGroupId);
        List<IaasBareMetal> nanoList = new ArrayList<>();
        List<IaasBareMetal> localList = this.list(local);

        if (CollectionUtils.isEmpty(ngljs)) {
            this.removeByIds(localList);
            return;
        }

        for (BareMetalVO vo : ngljs) {
            QueryWrapper<IaasBareMetal> queryWrapperIaasBareMetal = new QueryWrapper<>();
            queryWrapperIaasBareMetal.lambda()
                    .eq(IaasBareMetal::getNativieId, vo.getNativieId());
            IaasBareMetal queryBare = this.getOne(queryWrapperIaasBareMetal);
            log.info("queryBare "+queryBare);
            IaasProject project = iaasProjectMapper.queryIaasProjectByKey(vo.getTenantId());
            log.info("project "+project);
            IaasArea area = iaasAreaMapper.queryIaasAreaByKey(vo.getRegionId());
            log.info("area "+area);

            IaasBareMetal bareMetal = new IaasBareMetal();
            if (queryBare == null) {
                long id = SnowFlakeIdGenerator.getInstance().nextId();
                BeanUtils.copyProperties(vo, bareMetal);
                bareMetal.setId(id);
                bareMetal.setName(vo.getName());
                bareMetal.setCreateTime(DateUtil.getNow());
                bareMetal.setCreateUser(Sign.getUserId());
                bareMetal.setBusinessGroupId(businessGroupId);

                if (project != null) {
                    bareMetal.setProjectId(project.getId());
                }
                if (area != null) {
                    bareMetal.setAreaId(area.getId());
                }
                log.info("bareMetal "+bareMetal);
                this.save(bareMetal);
            } else {
                vo.setId(queryBare.getId());
                BeanUtils.copyProperties(vo, queryBare);
                queryBare.setBusinessGroupId(businessGroupId);
                queryBare.setCreateTime(DateUtil.getNow());
                queryBare.setCreateUser(Sign.getUserId());
                queryBare.setName(vo.getName());
                if (project != null) {
                    queryBare.setProjectId(project.getId());
                }
                if (area != null) {
                    queryBare.setAreaId(area.getId());
                }
                log.info("update queryBare "+queryBare);
                this.updateById(queryBare);
            }
            nanoList.add(queryBare == null?bareMetal:queryBare);
        }
        log.info("nanoList "+nanoList);
        List<IaasBareMetal> removeList = localList.stream().filter(item -> !nanoList.stream().map(e -> e.getNativieId())
                .collect(Collectors.toList()).contains(item.getNativieId())).collect(Collectors.toList());
        log.info("removeList "+removeList);
        if(CollectionUtils.isNotEmpty(removeList)) {
            this.removeByIds(removeList);
//            for (IaasBareMetal iaasBareMetal:removeList) {
//                this.removeById(iaasBareMetal.getId());
//            }
            
        }
    }

}

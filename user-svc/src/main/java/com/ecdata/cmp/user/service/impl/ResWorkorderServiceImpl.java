package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.*;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZone;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneResource;
import com.ecdata.cmp.huawei.dto.availablezone.WholeDimensionCapacity;
import com.ecdata.cmp.huawei.dto.response.*;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import com.ecdata.cmp.huawei.dto.vm.VmFlavors;
import com.ecdata.cmp.huawei.dto.vo.*;
import com.ecdata.cmp.iaas.client.IaasHWTokenClient;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import com.ecdata.cmp.user.entity.ResWorkorder;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.mapper.ResWorkorderMapper;
import com.ecdata.cmp.user.service.IResWorkorderService;
import com.ecdata.cmp.user.service.ResDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * @author ZhaoYX
 * @since 2019/11/20 10:50,
 */
@Slf4j
@Service
public class ResWorkorderServiceImpl extends ServiceImpl<ResWorkorderMapper, ResWorkorder>
        implements IResWorkorderService {



    private List<VdcsVO> vdcList;

    @Override
    public List<VdcsVO> getVdcList(){
        return vdcList;
    }

    @Autowired
    IaasHWTokenClient tokenClient;

    @Autowired
    VdcsClient vdcsClient;

    @Autowired
    AvailableZoneClient availableZoneClient;

    @Autowired
    ProjectsClient projectsClient;

    @Autowired
    ImageServiceClient imageClient;

    @Autowired
    VDCVirtualMachineClient vdcVirtualMachineClient;

    @Autowired
    SecurityGroupsClient securityGroupsClient;

    @Autowired
    ResDictionaryService dictionaryService;


    /**
     * 进入页面获取下拉框数据，包括vdcs
     * @return
     */
        @Override
        public Map<String, Object> selectConditions() throws IOException {
            String authz = AuthContext.getAuthz();
            List<Map<String,Object>> depts= baseMapper.queryDepartments(Sign.getUserId());
            List<Map<String,Object>> bgroups= baseMapper.queryBusinessGroups(Sign.getUserId());
//            List<Map<String,Object>> vdcs = baseMapper.qryAllVDCs();
//            vdcs.forEach((Map<String,Object> map)->{map.put("key",map.get("id"));});
            //获取华为vdcs
            com.ecdata.cmp.iaas.entity.dto.TokenDTO tokenDTO= tokenClient.listTokenDTO(authz);
            TokenDTO dto = new TokenDTO();
            BeanUtils.copyProperties(tokenDTO,dto);
            VdcsListResponse vdcsListResponse = vdcsClient.getTokenInfoByUser(authz,dto);
            List<VdcsVO> vdcs = vdcsListResponse.getData();
            vdcs.forEach((VdcsVO vo)->{
                    vo.setKey(vo.getId());
            });
            vdcList=vdcs;
            //用户
            Long userId=Sign.getUserId();
            User user = new User();
            user.selectById(userId);
            String name = user.getDisplayName();
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("departments",depts);
            map.put("businessGroups",bgroups);
            map.put("userId",userId );
            map.put("userName",name );
            map.put("vdcs",vdcs);
            return map;
        }

    /**
     * 创建工单
     * @param resWorkorderVo
     * @return
     */
    @Override
    public ResWorkorder createWorkOrder(ResWorkorderVO resWorkorderVo) {
        ResWorkorder resWorkorder = new ResWorkorder();
        BeanUtils.copyProperties(resWorkorderVo, resWorkorder);
        List<Map<String,Object>> flavors = resWorkorderVo.getFlavors();
        String flavor ="";
        for(Map<String,Object> fl:flavors){

            flavor +=fl.get("name")+",";
            flavor +=fl.get("ram")+",";
            flavor +=fl.get("vcpus")+",";
            flavor +=fl.get("disk")+".";
        }
        resWorkorder.setCurrentSpecs(flavor);
        resWorkorder.setCreateTime(DateUtil.getNow());
        resWorkorder.setCreateUser(Sign.getUserId());
        resWorkorder.setRoleId(30003772117868547L);//目前是写死的，之后按登录角色来
        resWorkorder.setId(SnowFlakeIdGenerator.getInstance().nextId());
        resWorkorder.setUpdateTime(DateUtil.getNow());
        resWorkorder.setStatus("applying");
        resWorkorder.setUpdateUser(Sign.getUserId());
        log.info("resWorkorder "+resWorkorder);
        return resWorkorder;
    }

    /**
     * 根据vdc获取项目集合 projects
     * @param vdcId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> queryProject(String vdcId) throws IOException {
        Map<String,Object> result = new HashMap<>();
        String authz = AuthContext.getAuthz();
//        Map<String, Object> vdc = baseMapper.qryVDCByKey(vdcId);
//        String vmKey = (String) vdc.get("vdc_key");
        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVO(authz, vdcId);
        RequestVO requestVO = new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        ProjectsListResponse projectList = projectsClient.getProjectList(authz,requestVO);
        List<ProjectsVO> projects = projectList.getData();
        projects.forEach((ProjectsVO pvo)->{pvo.setKey(pvo.getId());});
        result.put("projects",projects);

//        VdcsVO vo =   new VdcsVO();
//        for(VdcsVO svo:vdcList){
//            String key = svo.getId();
//            log.info("key "+key);
//            Map<String, Object> vdcEntity = baseMapper.qryVDCByKey(key);
//            log.info("vdcEntity "+vdcEntity);
//            if(vdcEntity==null || vdcEntity.get("vdc_key")== null )
//                continue;
//            if(svo.getKey()==String.valueOf(vdcId))
//                vo=svo;
//        }
        return result;
    }

    /**
     * 根据项目id获取各种关联下拉框
     * @param
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> queryVdcAndProject(String vdcId,String proId) throws IOException {
        Map<String,Object> maps = new HashMap<>();
        //规格参数
        VmFlavorsResponse vmFlavorsResponse =this.getVdcFlavors(vdcId,proId);
        //安全组
        SecurityGroupsListResponse sgResponse = this.getSecurityGroup(vdcId,proId);
        //镜像
        ImagesListResponse imageResponse  = this.getMirrors(vdcId,proId);
        //可用分区
        AvailableZoneResponse zoneResponse = this.getAvailableZones(proId);

        List<VmFlavors> flavors = vmFlavorsResponse.getData();
        List<SecurityGroupsVO> sgVOs = sgResponse.getData();
        List<ImagesVO> imgVOs = imageResponse.getData();
        List<AvailableZone> zoneList = zoneResponse.getData();

        maps.put("aZone",zoneList);//可用分区----（磁盘）  az_id需要前端传入
        maps.put("flavors",flavors);//规格
        maps.put("security",sgVOs);//安全组
        maps.put("mirrors",imgVOs);//镜像

        return maps;
    }

    /**
     * 根据区域Id获取磁盘信息
     * @param azId
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WholeDimensionCapacity> queryDisks(String azId) throws IOException {
        AvailableZoneResourceResponse availableZoneResourceResponse = this.getAvailableDisks(azId);
        AvailableZoneResource azr = availableZoneResourceResponse.getData();
        //磁盘
        List<WholeDimensionCapacity> disks = azr.getCapacityList();
        return disks;
    }



    @Override
    public ResWorkorder qryWorkOrderByPoolId(Long poolId) {
        return baseMapper.qryWorkOrderByPoolId(poolId);
    }

    /**
     * 规格
     * @param vdcId
     * @param projectKey
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public VmFlavorsResponse getVdcFlavors(String vdcId, String projectKey) throws IOException {
        String authz = AuthContext.getAuthz();
        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVOFlavor(authz,projectKey,vdcId);//authz,
        RequestVO requestVO= new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        VmFlavorsResponse vmFlavorsResponse= vdcVirtualMachineClient.getVmFlavorsList(authz,requestVO);
        return vmFlavorsResponse;
    }


    /**
     * 安全组
     * @param vdcId
     * @param projectKey
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public SecurityGroupsListResponse getSecurityGroup(String vdcId, String projectKey) throws IOException {
        String authz = AuthContext.getAuthz();
        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVOFlavor(authz,projectKey,vdcId);//authz,
        RequestVO requestVO= new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        SecurityGroupsListResponse securityGroupsListResponse=
                securityGroupsClient.getSecurityGroups(authz,requestVO.getOcToken());
        return securityGroupsListResponse;
    }

    /**
     * 镜像
     * @param vdcId
     * @param projectKey
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ImagesListResponse getMirrors(String vdcId, String projectKey) throws IOException {
        String authz = AuthContext.getAuthz();
        com.ecdata.cmp.iaas.entity.dto.RequestVO entity= tokenClient.getRequestVOFlavor(authz,projectKey,vdcId);//authz,
        RequestVO requestVO= new RequestVO();
        BeanUtils.copyProperties(entity,requestVO);
        ImagesListResponse ImagesListResponse=imageClient.getImagesList(authz,requestVO);
        return ImagesListResponse;
    }

    /**
     * 项目下可用分区 zoneResponse
     * @param proId
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public AvailableZoneResponse getAvailableZones(String proId) throws IOException {
        String authz = AuthContext.getAuthz();
        AvailbleZoneReqVO availbleZoneReqVO = new AvailbleZoneReqVO();
        com.ecdata.cmp.iaas.entity.dto.AvailbleZoneReqVO zoneReqVO =
                tokenClient.getAvailbleZoneId(authz,proId);
        BeanUtils.copyProperties(zoneReqVO,availbleZoneReqVO);
        AvailableZoneResponse zoneResponse =
                availableZoneClient.getAvailableZoneByProjectId(authz,availbleZoneReqVO);
        return zoneResponse;
    }

    /**
     * 分区下磁盘 zoneResourceResponse
     * @param azId
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public AvailableZoneResourceResponse getAvailableDisks(String azId) throws IOException {
        String authz = AuthContext.getAuthz();
        AvailbleZoneReqVO availbleZoneReqVO = new AvailbleZoneReqVO();
        com.ecdata.cmp.iaas.entity.dto.AvailbleZoneReqVO zoneReqVO =
                tokenClient.listAvailbleZoneReqVO(authz,azId);
        BeanUtils.copyProperties(zoneReqVO,availbleZoneReqVO);
        AvailableZoneResourceResponse zoneResourceResponse =
                availableZoneClient.getAvailableZoneResource(authz,availbleZoneReqVO);
        return zoneResourceResponse;
    }


}

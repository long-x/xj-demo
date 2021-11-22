package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.dto.response.AvailableZoneResourceResponse;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import com.ecdata.cmp.huawei.dto.token.TokenInfoVO;
import com.ecdata.cmp.huawei.dto.vo.AvailbleZoneReqVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualDataCenterMapper;
import com.ecdata.cmp.iaas.service.IResourcePoolService;
import com.ecdata.cmp.user.client.ResWorkClient;
import com.ecdata.cmp.user.dto.VdcsVO;
import com.ecdata.cmp.user.dto.response.VdcsListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2020/1/14 10:20,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/iaas_hw_token")
@Api(tags = "user获取iaas中的service的API")
public class IaasHWTokenController {
    @Autowired
    private IResourcePoolService resourcePoolService;

    @Autowired
    private IaasProviderMapper iaasProviderMapper;

    @Autowired
    private IaasVirtualDataCenterMapper virtualDataCenterMapper;

    @Autowired
    private OauthTokenClient oauthTokenClient;

    @Autowired
    ResWorkClient resWorkClient;

    private String authz = AuthContext.getAuthz();


    @GetMapping("/get_requestVOFlavor")
    @ApiOperation(value = "获取requestVO", notes = "获取requestVO")
    public ResponseEntity<RequestVO> listRequestVOFlavor(@RequestParam(name = "projectKey",required = false) String projectKey,
                                                         @RequestParam(name = "vdcId",required = false) String vdcId) {
        RequestVO requestVO = new RequestVO();
        BaseResponse baseResponse = new BaseResponse();

        StringResponse tokenByVdcUser =this.getTokenVdcUser(projectKey,vdcId);
        IaasProviderVO iaasProviderVO =iaasProviderMapper.queryIaasProviderVOById(iaasProviderMapper.queryProviderId());

        TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO);
        if (tokenResponse == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(requestVO);
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        requestVO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());//omToken
        requestVO.setOcToken(tokenByVdcUser == null ? "" : tokenByVdcUser.getData());//vdc token
        requestVO.setProjectId(projectKey);
        return ResponseEntity.status(HttpStatus.OK).body(requestVO);

    }

    @GetMapping("/get_requestVO")
    public ResponseEntity<RequestVO> listRequestVO(@RequestParam(name = "vdcId",required = false) String vdcId){
        RequestVO requestVO = new RequestVO();
        BaseResponse baseResponse = new BaseResponse();

        TokenInfoResponse tokenResponse = getToken(authz, "","");
        if (tokenResponse == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(requestVO);
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        //调用vdc接口
        requestVO.setOcToken(tokenInfoVO.getOcToken());
        requestVO.setVdcId(vdcId);
        return ResponseEntity.status(HttpStatus.OK).body(requestVO);
    }


    @GetMapping("/get_TokenDTO")
    @ApiOperation(value = "获取TokenDTO", notes = "获取TokenDTO")
    public ResponseEntity<TokenDTO> listTokenDTO() {
        TokenDTO tokenDTO = new TokenDTO();
        BaseResponse baseResponse = new BaseResponse();

        TokenInfoResponse tokenResponse = getToken(authz, "","");
        if (tokenResponse == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tokenDTO);
        }
        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        tokenDTO.setOcToken(tokenInfoVO.getOcToken());
        tokenDTO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());

        return ResponseEntity.status(HttpStatus.OK).body(tokenDTO);
    }


    @GetMapping("/get_AvailbleZoneId")
    @ApiOperation(value = "获取AvailbleZoneId", notes = "获取AvailbleZoneId")
    public ResponseEntity<AvailbleZoneReqVO> getAvailableZoneByProjectId(@RequestParam(name = "proId") String proId) {
        AvailbleZoneReqVO availbleZoneReqVO = new AvailbleZoneReqVO();
        BaseResponse baseResponse = new BaseResponse();

//        IaasProviderVO iaasProviderVO =iaasProviderMapper.queryIaasProviderVOById(iaasProviderMapper.queryProviderId());

        TokenInfoResponse tokenResponse = getToken(authz, "","");
        if (tokenResponse == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(availbleZoneReqVO);
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        availbleZoneReqVO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());//omToken
        availbleZoneReqVO.setOcToken(tokenInfoVO.getOcToken());
        availbleZoneReqVO.setProjectId(proId);
//        availbleZoneReqVO.setAzoneId(azoneKey);
        return ResponseEntity.status(HttpStatus.OK).body(availbleZoneReqVO);
    }


    @GetMapping("/get_AvailbleZoneReqVO")
    @ApiOperation(value = "获取AvailbleZoneReqVO", notes = "获取AvailbleZoneReqVO")
    public ResponseEntity<AvailbleZoneReqVO> listAvailbleZoneReqVO(@RequestParam(name = "azId",required = false) String azId) {
        AvailbleZoneReqVO availbleZoneReqVO = new AvailbleZoneReqVO();
        BaseResponse baseResponse = new BaseResponse();

//        IaasProviderVO iaasProviderVO =iaasProviderMapper.queryIaasProviderVOById(iaasProviderMapper.queryProviderId());

        TokenInfoResponse tokenResponse = getToken(authz, "","");
        if (tokenResponse == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(availbleZoneReqVO);
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        availbleZoneReqVO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());//omToken
        availbleZoneReqVO.setAzoneId(azId);//vdc token
        availbleZoneReqVO.setOcToken(tokenInfoVO.getOcToken());
//        availbleZoneReqVO.setProjectId();
//        availbleZoneReqVO.setAzoneId(azoneKey);
        return ResponseEntity.status(HttpStatus.OK).body(availbleZoneReqVO);
    }

    /**
     * 虚拟机规格和镜像所需
     * @param projectKey
     * @param vdcId
     * @return
     */
    private StringResponse getTokenVdcUser(String projectKey,String vdcId){
        QueryWrapper<IaasVirtualDataCenter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasVirtualDataCenter::getVdcKey,vdcId);
        String authz = AuthContext.getAuthz();
//        VdcsListResponse vdcs = resWorkClient.listVdcs(authz);
//        List<VdcsVO> vdcsVOS =vdcs.getData();
        IaasVirtualDataCenter ivdc = virtualDataCenterMapper.selectOne(queryWrapper);
//        for(VdcsVO vdcvo :vdcsVOS){
//            log.info("vdcvo.getId() "+vdcvo.getId());
//            if(vdcvo.getId().equals(vdcId))
//                BeanUtils.copyProperties(vdcvo,ivdc);
//        }
//
        StringResponse tokenByVdcUser = null;
        try {
            tokenByVdcUser = oauthTokenClient.getTokenByVdcUser(authz,
                    projectKey,
                    ivdc.getDomainName(),ivdc.getUsername(),ivdc.getPassword());
        } catch (Exception e) {
            log.info("获取vdc token错误！");
        }
        return tokenByVdcUser;
    }




    private TokenInfoResponse getToken(String authz, IaasProviderVO iaasProviderVO) {
        TokenInfoResponse tokenResponse ;
        try {
            tokenResponse = oauthTokenClient.getTokenAll(authz,
                    iaasProviderVO);

            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("获取供应商token错误!", e);
            return null;
        }

        return tokenResponse;
    }

    private TokenInfoResponse getToken(String authz, String userName, String password) {
        TokenInfoResponse tokenResponse = null;
        try {
            tokenResponse = oauthTokenClient.getTokenByUser(authz,
                    "",
                    "domainName",
                    userName,
                    password);

            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("获取供应商token错误!", e);
            return null;
        }

        return tokenResponse;
    }
}

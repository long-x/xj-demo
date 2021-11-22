package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.AttachmentsVO;
import com.ecdata.cmp.huawei.dto.vo.HostVolumesVO;
import com.ecdata.cmp.huawei.dto.vo.MetadataVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.HostVolumesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 11:04
 * @modified By：
 */
@Service
@Slf4j
public class HostVolumesServiceImpl implements HostVolumesService {

    @Value("${huawei.ManageOne.ecs_url}")
    private String ecsUrl;

    @Override
    public List<HostVolumesVO> getHostVolumesList(RequestVO requestVO) throws Exception {
        String url = ecsUrl + "/v2/" + requestVO.getProjectId() + "/os-volumes";
        List<HostVolumesVO> list = new ArrayList<>();
        Map param = new HashMap();
        param.put("X-Auth-Token", requestVO.getOcToken());
        String result = BaseOkHttpClientUtil.get(url, param);
        JSONObject object = JSON.parseObject(result);
        JSONArray jsonArray = object.getJSONArray("volumes");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            HostVolumesVO hostVolumesVO = new HostVolumesVO();
            hostVolumesVO.setId(jsonArray.getJSONObject(i).getString("id"));
            hostVolumesVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
            hostVolumesVO.setAvailabilityZone(jsonArray.getJSONObject(i).getString("availabilityZone"));
            hostVolumesVO.setDisplayName(jsonArray.getJSONObject(i).getString("displayName"));
            hostVolumesVO.setSize(jsonArray.getJSONObject(i).getString("size"));
            hostVolumesVO.setSnapshotId(jsonArray.getJSONObject(i).getString("snapshotId"));
            hostVolumesVO.setDisplayDescription(jsonArray.getJSONObject(i).getString("displayDescription"));
            hostVolumesVO.setVolumeType(jsonArray.getJSONObject(i).getString("volumeType"));
            hostVolumesVO.setCreatedAt(jsonArray.getJSONObject(i).getString("createdAt"));
            //获取attachments数组对象
            String attachments = jsonArray.getJSONObject(i).getString("attachments");
            JSONArray attachmentsArray = JSONObject.parseArray(attachments);
            for (int j = 0; j < attachmentsArray.size(); j++) {
                AttachmentsVO attachmentsVO = new AttachmentsVO();
                attachmentsVO.setId(attachmentsArray.getJSONObject(j).getString("id"));
                attachmentsVO.setDevice(attachmentsArray.getJSONObject(j).getString("device"));
                attachmentsVO.setServerId(attachmentsArray.getJSONObject(j).getString("serverId"));
                attachmentsVO.setVolumeId(attachmentsArray.getJSONObject(j).getString("volumeId"));
                hostVolumesVO.setAttachments(attachmentsVO);
            }
            //获取MetadataVO对象
            String metadata = jsonArray.getJSONObject(i).getString("metadata");
            JSONObject metadataJson = JSONObject.parseObject(metadata);
            MetadataVO metadataVO = new MetadataVO();
            metadataVO.setAttachedMode(metadataJson.get("attached_mode") == null ? "" : metadataJson.get("attached_mode").toString());
            metadataVO.setLunWwn(metadataJson.get("lun_wwn") == null ? "" : metadataJson.get("lun_wwn").toString());
            metadataVO.setReadonly(metadataJson.get("readonly") == null ? "" : metadataJson.get("readonly").toString());
            metadataVO.setStorageType(metadataJson.get("StorageType") == null ? "" : metadataJson.get("StorageType").toString());
            metadataVO.setSysIsServerVol(metadataJson.get("__sys_is_server_vol__") == null ? "" : metadataJson.get("__sys_is_server_vol__").toString());
            metadataVO.setTenancy(metadataJson.get("tenancy") == null ? "" : metadataJson.get("tenancy").toString());
            hostVolumesVO.setMetadata(metadataVO);
            list.add(hostVolumesVO);
        }

        return list;
    }
}

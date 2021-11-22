package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.ImagesVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.ImagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/3 16:14
 * @modified By：
 */
@Service
@Slf4j
public class ImagesServiceImpl implements ImagesService {

    @Value("${huawei.ManageOne.ims_url}")
    private String imsUrl;


    @Override
    public List<ImagesVO> getImages(RequestVO requestVO) throws IOException {
        String url = imsUrl + "/v2/images?limit=100";
        List<ImagesVO> list = new ArrayList<>();
        Map param = new HashMap();
        param.put("X-Auth-Token",requestVO.getOcToken());
        String result = BaseOkHttpClientUtil.get(url, param);
        JSONObject object = JSON.parseObject(result);
        JSONArray jsonArray = object.getJSONArray("images");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            String visibility = jsonArray.getJSONObject(i).getString("visibility");
            ImagesVO imagesVO = new ImagesVO();
            //过滤条件 只要public的
            if ("public".equals(visibility)) {
                imagesVO.setId(jsonArray.getJSONObject(i).getString("id"));
                imagesVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
                imagesVO.setName(jsonArray.getJSONObject(i).getString("name"));
                imagesVO.setContainerFormat(jsonArray.getJSONObject(i).getString("container_format"));
                imagesVO.setDiskFormat(jsonArray.getJSONObject(i).getString("disk_format"));
                imagesVO.setMinRam(jsonArray.getJSONObject(i).getString("min_ram"));
                imagesVO.setMinDisk(jsonArray.getJSONObject(i).getString("min_disk"));
                imagesVO.setOsBit(jsonArray.getJSONObject(i).getString("__os_bit"));
                imagesVO.setPlatform(jsonArray.getJSONObject(i).getString("__platform"));
                imagesVO.setOsType(jsonArray.getJSONObject(i).getString("__os_type"));
                imagesVO.setTag(jsonArray.getJSONObject(i).getString("tag"));
                imagesVO.setCreatedAt(jsonArray.getJSONObject(i).getString("created_at"));
                imagesVO.setUpdatedAt(jsonArray.getJSONObject(i).getString("updated_at"));
                imagesVO.setChecksum(jsonArray.getJSONObject(i).getString("checksum"));
                imagesVO.setSupportStaticIp(jsonArray.getJSONObject(i).getString("__support_static_ip"));
                imagesVO.setIsregistered(jsonArray.getJSONObject(i).getString("__isregistered"));
                imagesVO.setArchitecture(jsonArray.getJSONObject(i).getString("architecture"));
                imagesVO.setOwner(jsonArray.getJSONObject(i).getString("owner"));
                imagesVO.setVirtualSize(jsonArray.getJSONObject(i).getString("__virtual_size"));
                imagesVO.setHwFirmwareType(jsonArray.getJSONObject(i).getString("hw_firmware_type"));
                imagesVO.setImagetype(jsonArray.getJSONObject(i).getString("__imagetype"));
                imagesVO.setVisibility("0");
                imagesVO.setCloudinit(jsonArray.getJSONObject(i).getString("cloudinit"));
                imagesVO.setVirtualEnvType(jsonArray.getJSONObject(i).getString("virtual_env_type"));
                imagesVO.setSupportKvmNvmeSsd(jsonArray.getJSONObject(i).getString("__support_kvm_nvme_ssd"));
                imagesVO.setHwDiskBus(jsonArray.getJSONObject(i).getString("hw_disk_bus"));
                imagesVO.setSize(jsonArray.getJSONObject(i).getString("size"));
                imagesVO.setOsVersion(jsonArray.getJSONObject(i).getString("__os_version"));
                imagesVO.setSelf(jsonArray.getJSONObject(i).getString("self"));
            }
            list.add(imagesVO);
        }
        log.info("获取的images数量:",list.size());
        return list;

    }
}

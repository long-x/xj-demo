package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VmHistoryDataVO;
import com.ecdata.cmp.huawei.service.VmHistoryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 18:19
 * @modified By：
 */

@Service
@Slf4j
public class VmHistoryDataServiceImpl implements VmHistoryDataService {





    @Override
    public VmHistoryDataVO getVmHistoryData(RequestVO requestVO) throws Exception {
        VmHistoryDataVO vmHistoryDataVO = new VmHistoryDataVO();

        //读取本地文件
        String path = "/json/"+requestVO.getIndicatorIds()+".json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取历史性能文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }


        String data = object.getString("data");
        JSONObject object2 = JSON.parseObject(data);
        String objIds = object2.getString("A7A4F5BBB2693432BB925297BA3BE89B");
        JSONObject object3 = JSON.parseObject(objIds);
        String objTypeId = object3.getString(requestVO.getIndicatorIds());
        JSONObject parseObject = JSON.parseObject(objTypeId);
        //最小值
        vmHistoryDataVO.setMin(parseObject.getString("min"));
        //最大值
        vmHistoryDataVO.setMax(parseObject.getString("max"));
        //平均值
        vmHistoryDataVO.setAvg(parseObject.getString("avg"));
        //历史数据 数组
        vmHistoryDataVO.setHistory(parseObject.getString("series"));

        return vmHistoryDataVO;
    }
}

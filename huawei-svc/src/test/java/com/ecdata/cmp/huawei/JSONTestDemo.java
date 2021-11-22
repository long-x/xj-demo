package com.ecdata.cmp.huawei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ：xuj
 * @date ：Created in 2020/12/21 14:09
 * @modified By：
 */
@SpringBootTest
public class JSONTestDemo {


    @Test
    public void test01() throws IOException {

        String path = "/json/vm.json";
        InputStream config = getClass().getResourceAsStream(path);

        if (config == null) {
            throw new RuntimeException("读取文件失败");
        } else {
            JSONObject json = JSON.parseObject(config, JSONObject.class);
            System.out.println(json);
        }

//        File file = ResourceUtils.getFile("classpath:json/vm.json");
//        InputStream inputStream = new FileInputStream(file);
    }


}

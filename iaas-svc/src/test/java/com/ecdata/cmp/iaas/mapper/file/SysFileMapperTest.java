package com.ecdata.cmp.iaas.mapper.file;

import com.ecdata.cmp.iaas.entity.SysFile;
import com.ecdata.cmp.iaas.utils.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xuxiaojian
 * @date 2020/3/31 15:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysFileMapperTest {

    @Autowired
    private SysFileMapper sysFileMapper;

    @Test
    public void test() throws Exception {
        SysFile sysFile = sysFileMapper.selectById(77064970433052676L);
        FileUtil util = new FileUtil();

        util.getFile(sysFile.getImg(), "C:\\Users\\xxj\\Desktop\\Yuntou\\部署\\", sysFile.getFileName());
    }

}
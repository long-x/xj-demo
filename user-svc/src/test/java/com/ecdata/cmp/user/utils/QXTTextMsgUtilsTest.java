package com.ecdata.cmp.user.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QXTTextMsgUtilsTest {

    @Autowired
    private QXTTextMsgUtils qxtTextMsgUtils;

    @Test
    public void sendTextMsgTest() {
        qxtTextMsgUtils.sendTextMsg("ecdata test", Collections.singletonList("13671880843"));
    }

}
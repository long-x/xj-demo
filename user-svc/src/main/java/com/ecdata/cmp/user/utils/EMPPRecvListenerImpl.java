package com.ecdata.cmp.user.utils;

import com.wondertek.esmp.esms.empp.EMPPObject;
import com.wondertek.esmp.esms.empp.EMPPRecvListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EMPPRecvListenerImpl implements EMPPRecvListener {

    @Override
    public void onMessage(EMPPObject emppObject) {
        log.info("onMessage={}", emppObject);
    }

    @Override
    public void OnClosed(Object o) {
        log.info("OnClosed={}", o);
    }

    @Override
    public void OnError(Exception e) {
        log.error("OnError=" + e);
    }
}

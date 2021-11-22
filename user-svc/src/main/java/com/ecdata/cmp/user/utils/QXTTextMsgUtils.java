package com.ecdata.cmp.user.utils;

import com.wondertek.esmp.esms.empp.EmppApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class QXTTextMsgUtils {

    @Value("${qxt.text.message.host}")
    private String host;

    @Value("${qxt.text.message.port}")
    private int port;

    @Value("${qxt.text.message.accountId}")
    private String accountId;

    @Value("${qxt.text.message.password}")
    private String password;

    @Value("${qxt.text.message.serviceId}")
    private String serviceId;

    @Autowired
    private EMPPRecvListenerImpl emppRecvListener;

    private static final EmppApi emppApi = new EmppApi();

    public void sendTextMsg(String content, List<String> mobileList) {
        log.info("sendTextMsg, content={}, mobileList={}", content, mobileList);
        if (StringUtils.isNotBlank(content)
                && CollectionUtils.isNotEmpty(mobileList)) {
            // 去处重复的手机号
            Set<String> mobileSet = new HashSet<>(mobileList);

            log.info("sendTextMsg ready");
            try {
                if (!emppApi.isConnected()) {
                    log.info("sendTextMsg ready connect");
                    emppApi.connect(this.host, this.port, this.accountId, this.password, emppRecvListener);
                    log.info("sendTextMsg connected");
                }
                log.info("sendTextMsg 1 connected={}, submit={}", emppApi.isConnected(), emppApi.isSubmitable());
                if (emppApi.isConnected() && emppApi.isSubmitable()) {
                    emppApi.submitMsgAsync(content, mobileSet.toArray(new String[]{}), serviceId);
                    log.info("sendTextMsg success");
                } else {
                    log.info("sendTextMsg 2 connected={}, submit={}", emppApi.isConnected(), emppApi.isSubmitable());
                }
            } catch (Exception e) {
                log.error("sendTextMsg error=" + e);
            }
        }
    }
}

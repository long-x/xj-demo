package com.ecdata.cmp.huawei.service.impl;

import com.ecdata.cmp.huawei.service.AvailableZoneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AvailableZoneServiceImplTest {
    @Autowired
    private AvailableZoneService availableZoneService;
    @Test
    public void test1(){
        try {
            availableZoneService.getBmsList("49f1b96e3fd34860a70de9cb85607ebe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*@Test
    public void getAvailableZone() throws Exception {
        List<AvailableZone> availableZoneByInfraId = availableZoneService.getAvailableZoneByInfraId("MIIEDgYJKoZIhvcNAQcCoIID-zCCA-sCAQExDTALBglghkgBZQMEAgEwggJvBgkqhkiG9w0BBwGgggJgBIICXHsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMTktMTItMTJUMDg6MzQ6MTcuNzA1MDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZyI6W10sImRvbWFpbiI6eyJuYW1lIjoibW9fYnNzX2FkbWluIiwiaWQiOiI4YjZiNGNkOWY1MGY0OTdhYjhlYzhhYzQyYzg2OWY3MyJ9LCJyb2xlcyI6W3sibmFtZSI6InNlY3VfYWRtaW4iLCJpZCI6IjA3ODc2OWQxNjlkMTRhNDA4M2IwNjU3YzY5OWY2YTcxIn0seyJuYW1lIjoibW9fYnNzX2FkbSIsImlkIjoiMDcyMjM3MmFkOTdiNGMwNzk2YjE4NDEyNTUyMzFkYjQifSx7Im5hbWUiOiJhcHByb3ZfYWRtIiwiaWQiOiIxMDlkYzM4YWQwZTE0NmY3OWM2MDc5MTU2YzkwNTM0YSJ9LHsibmFtZSI6InRhZ19hZG0iLCJpZCI6IjRhMzFlNjNjYjIyYTRjN2U4NmIzYTI5MTNiMjA1MjNlIn1dLCJpc3N1ZWRfYXQiOiIyMDE5LTEyLTExVDA4OjM0OjE3LjcwNTAwMFoiLCJ1c2VyIjp7ImRvbWFpbiI6eyJuYW1lIjoibW9fYnNzX2FkbWluIiwiaWQiOiI4YjZiNGNkOWY1MGY0OTdhYjhlYzhhYzQyYzg2OWY3MyJ9LCJuYW1lIjoiZWNkYXRhX2FkbWluIiwiaWQiOiJmMzI2OTViMmM4NjY0OGZkYTg0OTYxZDNhOTI2YjlkMCJ9fX0xggFyMIIBbgIBATBJMD0xCzAJBgNVBAYTAkNOMQ8wDQYDVQQKEwZIdWF3ZWkxHTAbBgNVBAMTFEh1YXdlaSBJVCBQcm9kdWN0IENBAggVqZEhukGatzALBglghkgBZQMEAgEwDQYJKoZIhvcNAQEBBQAEggEAIEyrz4clcXjy1lIfKDhaxY66NZ2fwTEl6qzM8eHNymzatz6pQpLBEbpxnhFwMWLnUgqXncvXx0zM6k3nJRt2PorQhBGLOE3L906C4x5w-LedtkBoLDnWGDwv9QW1Gh+SNSEUBGi2ApPBaNeJopdMNN53rqUMW31PNn3561tlbmQSoRFNViKhzYmgFAfiBoejdpkUZaGMXMlU6zlIQNm-o4Mhmv98fxB5+tN6tEHk8B5hdEWLYtTODvbl6q6OAZgNTGOs7LQJZSTJgg0YTUsKP6zK3OR7OMBmU5XOzXR-iVeeHMWVfPR1SymPX2ybM9O3JzcAo6U1xw47xzjravywBA==", "");
        System.out.println(availableZoneByInfraId);
    }*/

}

package com.ecdata.cmp.huawei;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/3 14:44
 * @modified By：
 */
@SpringBootTest
public class GetImageTest {
//
//    private static final String DEFAULT_CHARSET = "UTF-8";
//
//    @Test
//    public void getListTest(){
//     Map param = new HashMap();
//     String token = "MIIEcwYJKoZIhvcNAQcCoIIEZDCCBGACAQExDTALBglghkgBZQMEAgEwggLUBgkqhkiG9w0BBwGgggLFBIICwXsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMTktMTItMDRUMDY6MTc6MTIuNDk5MDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZyI6W10sInJvbGVzIjpbeyJuYW1lIjoidGVfYWRtaW4iLCJpZCI6IjIwMzliY2EzNDRmZTQ4YzY4NGRlNzY4NTcxNThhYzI3In0seyJuYW1lIjoidmRjX2FkbSIsImlkIjoiNDMyMjY4MGQzZGJkNGQ4YzgyNzRjZDAyMmQ3ZjY0MjkifSx7Im5hbWUiOiJ0YWdfYWRtIiwiaWQiOiI5ODJiOTZlNWNkNGQ0MTUxOGI5OGQ3N2E2YjI1N2Y4YyJ9LHsibmFtZSI6ImFwcHJvdl9hZG0iLCJpZCI6ImM4ZTZlN2FiMzg0ZDRiMmJhYmNhNzAyZjdkNTY2YWQ2In0seyJuYW1lIjoidmRjX293bmVyIiwiaWQiOiJiNTgzMGU0YzRkYTc0ZWQ5YTU0OGE2ZDc1MmJlOWY1MyJ9XSwicHJvamVjdCI6eyJkb21haW4iOnsibmFtZSI6ImxhaiIsImlkIjoiY2U0ODZhYWM3ZWE4NGIwOThkYjg5MWFiZjY0MThlZDUifSwibmFtZSI6InRlc3QiLCJpZCI6IjcyOTJhYWY0MDJhNjQ5NzVhZTEwYzlmZjc1ZTEwNWUxIn0sImlzc3VlZF9hdCI6IjIwMTktMTItMDNUMDY6MTc6MTIuNDk5MDAwWiIsInVzZXIiOnsiZG9tYWluIjp7Im5hbWUiOiJsYWoiLCJpZCI6ImNlNDg2YWFjN2VhODRiMDk4ZGI4OTFhYmY2NDE4ZWQ1In0sIm5hbWUiOiJsYWpsYWpsYWoiLCJpZCI6ImIxYzYzMDM4YTczNDRlNjJiY2ZiMzM1Y2ZkNTgzOTdkIn19fTGCAXIwggFuAgEBMEkwPTELMAkGA1UEBhMCQ04xDzANBgNVBAoTBkh1YXdlaTEdMBsGA1UEAxMUSHVhd2VpIElUIFByb2R1Y3QgQ0ECCBWpkSG6QZq3MAsGCWCGSAFlAwQCATANBgkqhkiG9w0BAQEFAASCAQAFS6TCMYPc3JxeOA-Sw5auFMDtXf6XmXx0QLBrkefXsQxWhosicZw4Z1mWaPvUSdMAuQkosKO95E50BuWUrf4ZZ22LkqIQWq6-KZKJlxq-YJ9QVYy-RF6ksIw5rEbK9vdAl0qng9txnAUQY7oQ8nf9VTz04l4iWkhqM9YjyrFtxDnjEQI2Zkvyw5AO0YbdB618d3bnxGT2wcp9uSoLutgRRJHwo3S28UB-yFFiuXMWFNDit2KVxoyV9e6y40Jr0kflQmj-RW+II104UEgqHriIJNDQm0UUe+MrM3jy34pTMxuxf+FEEn4D2dFnRLjbZsxU7H8duOe5PqQyBSQ-gSWu";
//     param.put("X-Auth-Token", token);
////     String result = httpGetWithJSON("https://ims.sa-fb-1.chinaopenlab1.com/v2/images", param);
////     System.out.println("result:"+result);
////        try {
////            String s = httpPostWithJson(token);
////            System.out.println(s);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        String url = "https://ims.sa-fb-1.chinaopenlab1.com/v2/images";
//        try {
//            String s = BaseOkHttpClientUtil.get(url,param);
////            System.out.println(s);
//            JSONObject object = JSON.parseObject(s);
//            JSONArray jsonArray = object.getJSONArray("images");//获取数组
//            for(int i=0;i<jsonArray.size();i++){
//                String visibility = jsonArray.getJSONObject(i).getString("visibility");
//
//                System.out.println(visibility+",");
//            }
//
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//
//    /**
//     * 网上copy的方法 亲测可用（get）
//     * @param token
//     * @return
//     * @throws Exception
//     */
//    public static String httpPostWithJson(String token) throws Exception {
////        HttpPost httpPost = new HttpPost("https://ims.sa-fb-1.chinaopenlab1.com/v2/images");
//        HttpGet httpGet = new HttpGet("https://ims.sa-fb-1.chinaopenlab1.com/v2/images");
//
//        CloseableHttpClient client = null;
//        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
//                SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
//                NoopHostnameVerifier.INSTANCE
//        );
//        client = HttpClients.custom().setSSLSocketFactory(scsf).build();
//
//
//        String respContent = null;
//        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
////        HashMap<String, Object> map = JSON.parseObject(json.toJSONString(), HashMap.class);
////        for (String key : map.keySet())
////        {
////            pairList.add(new BasicNameValuePair(key, String.valueOf(map.get(key))));
////        }
////        httpGet.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
//        httpGet.setHeader("X-Auth-Token", token);
//
//        // jdk1.7及以下添加这句话，否则报错Could not generate DH keypair
//        Security.addProvider(new BouncyCastleProvider());
//
//        HttpResponse resp = client.execute(httpGet);
//        if (resp.getStatusLine().getStatusCode() == 200) {
//            HttpEntity he = resp.getEntity();
//            respContent = EntityUtils.toString(he, "UTF-8");
//        }
//        return respContent;
//    }
//


}

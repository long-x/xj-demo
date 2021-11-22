package com.ecdata.cmp.user;

import com.ecdata.cmp.user.utils.RSACoder;
import com.ecdata.cmp.user.utils.RSAUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class EncryptTest {
    public static void main(String[] args) throws Exception {
        Map<String, Object> map = RSACoder.initKey();
        String source = "Ecdata!@#123";

        //公钥加密
        PublicKey publicKey = RSAUtil.getPublicKey(RSACoder.getPublicKey(map));
//        PublicKey publicKey = RSAUtil.getPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDcwjySP4T61/8neKRDzjXw7qU1Q1Pt7MrOSz5uu1GmIGT0sbxsc1ZY0YX7LBvSlB9EdOjVkVfvgNM/ssmBKQw/cVRhK5YzEdUGQwvvvbYl97diYfTfr52DVoeRdKlodSnuKmSBpO4jOOoEewRqPBly9HUHclmQJo0TUig9DzPbwIDAQAB");
        String encript = RSAUtil.encryptString(publicKey, source);
        System.out.println("加密后数据：" + encript);

        //私钥解密
        PrivateKey privateKey = RSAUtil.getPrivateKey(RSACoder.getPrivateKey(map));
//        PrivateKey privateKey = RSAUtil.getPrivateKey("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAINzCPJI/hPrX/yd4pEPONfDupTVDU+3sys5LPm67UaYgZPSxvGxzVljRhfssG9KUH0R06NWRV++A0z+yyYEpDD9xVGErljMR1QZDC++9tiX3t2Jh9N+vnYNWh5F0qWh1Ke4qZIGk7iM46gR7BGo8GXL0dQdyWZAmjRNSKD0PM9vAgMBAAECgYAzIEDwi3dXJAs3Y+lFZlhDg3tEfAEralWjkB9wGkZDWPm9FxQN2Yv3ImeW0pZlEtBvdMmOE/XzoSIDhm5ZISEC5LhB+t8LR4mkFN4bfxTu8HKqoKtMHfXDz2EoPIPRulkLTJ9tgF8KEf9La/47oUntSeNWtWd+BNETim0Y7WbxUQJBANWHQkRxPTpQBMniFKRTgzEFd/tUDxSPe/Cmw2Xw28wuP2A4xlGFZa8VoNcJmn+I0G7uo/xA9Xd3CWq6CB5qYDMCQQCdmFxPjeSwRF1dUswNb99M6/c6Yf9+/n2vn0rownOKx9Wy3KNLrYDVZR+Pyv0FjhVGOUrCCmefjDcfXzhLoSfVAkEAmjh07kXzePhuXPmC+ySuLmvKuqV9ttXjKG7p1ejed1w3veGDq0Fzrb8rSeTPx6kjEdweaITqRXyeOo1ea8lc7QJAMtUZOWPoVt7GSrrRLKhgG3ylMwS3F6xYuBQmYmuOPz5z9Ixsc5WUT8CdbJEqCeepfwwty+b1Q6ZDhW/+RY7GvQJBALfrdzOegdiTNcuSZNPX6PqVUPeTBg67RG7ofEd+zs9OILTZU5ldUA583zBNxwCHBjwx3VlPOqzM1CngQZZ1prw=");
        String oldSource = RSAUtil.decryptString(privateKey, encript);
        System.err.println("解密后数据:" + oldSource);
    }
}

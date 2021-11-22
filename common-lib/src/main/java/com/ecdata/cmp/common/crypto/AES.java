package com.ecdata.cmp.common.crypto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.crypto.codec.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author laj
 * @since 2019-11-22
 */
public final class AES {

//    /**
//     * 生成密钥
//     */
//    private static byte[] initKey() throws Exception{
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(256);
//        SecretKey secretKey = keyGen.generateKey();
//        return secretKey.getEncoded();
//    }

    /**
     * AES 加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception{
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * AES 解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception{
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    public static License decryptLicense(String data) throws Exception {
        byte[] licenseByte = decrypt(Hex.decode(data), getKey());
        return JSONObject.parseObject(new String(licenseByte), License.class);
    }

    private static byte[] getKey() throws Exception {
        List<Byte> list = new ArrayList<>();

        try (
                InputStream is = AES.class.getClassLoader().getResourceAsStream("initKey")
        ) {
            Optional.ofNullable(is).ifPresent(e -> {
                int result = -1;

                while (true) {
                    try {
                        if ((result = e.read()) == -1) break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    list.add ((byte)result);
                }
            });
        }

        byte[] kkk = new byte[list.size()];
        for (int i=0; i<list.size(); i++) {
            kkk[i] = list.get(i);
        }

        return kkk;
    }

    public static void main(String[] args) throws Exception {
//        byte[] desKey = initKey();

//        String folder=System.getProperty("java.io.tmpdir");
//        File file = new File(folder + File.separator + "initKey");
//
//        if (file.exists()) {
//            System.out.println(file.delete());
//        }
//        System.out.println(file.createNewFile());

//        try (
//                FileOutputStream fos = new FileOutputStream(file);
//                BufferedOutputStream bos = new BufferedOutputStream(fos);
//        ) {
//            bos.write(desKey);
//        }


        byte[] kkk = getKey();
        License license = new License();
        license.setProd(1).setExpire(new Date()).setVersion("v1.0 Build000001");

        String jsonString = JSON.toJSONString(license);

        byte[] desResult = encrypt(jsonString.getBytes(), kkk);
        System.out.println(new String(Hex.encode(desResult)));

//        byte[] desPlain = decrypt(Hex.decode(new String(Hex.encode(desResult))), kkk);
        System.out.println(decryptLicense(new String(Hex.encode(desResult))));
//        System.out.println(decryptLicense(new String(Hex.encode(desResult))), kkk);
    }

}

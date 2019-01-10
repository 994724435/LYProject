package com.liaoyin.lyproject.common.util;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by good on 2017/9/11.
 * AES 生成密钥，加密，解密
 */
public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
//        public static String key = "AD42F6697B035B7580E4FEF93BE20BAD";
    public final static String key = "CZqkPj4iu29DE&oWFbjdG^s1IRv%A0ivHJLpB4'ehcvMgXV'5Nr8-enO#QxtSyThfKU@Y_";
    public final static int[] position = {3, 12, 57, 65, 19, 43, 46, 40, 52, 9, 26, 21, 44, 16, 18, 19};

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encrypt(String content){
        try {
            return encrypt(content,password());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content){
        try {
            return decrypt(content,password());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password){
            try {
                Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

                byte[] byteContent = content.getBytes("utf-8");

                cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器

                byte[] result = cipher.doFinal(byteContent);// 加密
                return  Base64.getEncoder().encodeToString(result);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

//            return Base64.encodeBase64String(result);//通过Base64转码返回
//        return org.apache.commons.codec.binary.Base64.encodeBase64String(result);//通过Base64转码返回

    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) throws Exception {

            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            //执行操作
//            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
//        byte[] result = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(content));
            byte[] result= cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result, "utf-8");

    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {

        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        SecureRandom random=null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, random );
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public static String password() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < position.length; i++) {
            builder.append(key.charAt(position[i]));
        }
        return builder.toString();
    }


    private Key initKeyForAES(String key) throws NoSuchAlgorithmException {
        if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key2 = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key2 = new SecretKeySpec(enCodeFormat, "AES");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException();
        }
        return key2;

    }


    public static void main(String[] args)throws Exception {
        String s = "hello,您好";

        System.out.println("s:" + s);

        String s1 = AESUtils.encrypt(s, "1234");
        System.out.println("s1:" + s1);

        System.out.println("s2:"+ AESUtils.decrypt("ofIdvaVy11Q0IiBsxM/53w==", "1234"));
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        String x= AESUtils.encrypt(s);
        System.out.println(x);
        String x2= AESUtils.decrypt(x);
        System.out.println(x2);

    }




}

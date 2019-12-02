package cn.blinkdagger.androidLab.utils.encode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * @Author ls
 * @Date 2019-07-30
 * @Description
 * @Version
 */
public class Md5Util {

    /**
     * 获取字符串的MD5 值
     * @param content  字符串
     */
    public static String getMD5Value(String content) {

        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("String to get Md5 value cannot be null or zero length");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            byte[] hash = digest.digest();
            return bytesToHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字符串的MD5 值
     * @param content  字符串
     * @param salt     加盐
     */
    public static String getMD5Value(String content, byte[] salt) {

        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("String to get Md5 value cannot be null or zero length");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(salt);
            byte[] hash = digest.digest(content.getBytes());
            return bytesToHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件的MD5 值
     * @param file  文件
     */
    public static String getFileMD5Value(File file) {

        if(file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            fis = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while(true) {
                len = fis.read(buff, 0, buff.length);
                if(len == -1){
                    break;
                }
                //每次循环读取一定的字节都更新
                digest.update(buff,0,len);
            }

            //返回md5字符串
            return bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取随机salt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }

    /**
     * byte数组转16进制字符串
     */
    public static String bytesToHexString(byte[] bytes){
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((0xff & bytes[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & bytes[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return hexString.toString();
    }

}

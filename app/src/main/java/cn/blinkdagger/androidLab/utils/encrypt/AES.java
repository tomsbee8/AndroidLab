package cn.blinkdagger.androidLab.utils.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.blinkdagger.androidLab.utils.encode.Base64;


/**
 * @Author ls
 * @Date 2019-06-18
 * @Description AES加解密
 * @Version
 */
public class AES {

    // 默认初始化向量
    private static final String DEFAULT_IV = "0123456789abcdef";

    // 默认CBC模式使用PKCS7填充
    private static final String CBC_MODE = "AES/CBC/PKCS7Padding";

    static {
        // 若使用PKCS7填充需向VM添加Provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * AES加密
     *
     * @param content     明文
     * @param password    密钥
     * @param ivParameter 初始化向量
     * @return 加密后的密文
     */
    public static String encrypt(String content, String password, String ivParameter) {
        try {
            Key key = getKey(password);
            // 加密
            Cipher cipher = Cipher.getInstance(CBC_MODE);
            IvParameterSpec ivParameterSpec = getIVParameter(ivParameter);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件AES加密
     *
     * @param sourceFilePath    源文件路径
     * @param encryptedFilePath 加密后文件路径
     * @param password          密钥
     * @param ivParameter       初始化向量
     * @return 加密后的密文
     */
    public static boolean encryptFile(String sourceFilePath, String encryptedFilePath, String password, String ivParameter) {

        File sourceFile = new File(sourceFilePath);
        File destFile = new File(encryptedFilePath);
        if (sourceFile.exists() && sourceFile.isFile()) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(sourceFile);
                fos = new FileOutputStream(destFile);
                // 加密
                Cipher cipher = Cipher.getInstance(CBC_MODE);
                Key key = getKey(password);
                IvParameterSpec ivParameterSpec = getIVParameter(ivParameter);
                cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

                CipherInputStream cis = new CipherInputStream(fis, cipher);

                byte[] buffer = new byte[1024];
                int n;
                while ((n = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, n);
                    fos.flush();
                }
                cis.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * AES解密
     *
     * @param encryptedContent 密文
     * @param password         密钥
     * @param ivParameter      初始化向量
     * @return 解密后的明文
     */
    public static String decrypt(String encryptedContent, String password, String ivParameter) {
        try {
            Key key = getKey(password);
            // 解密
            Cipher cipher = Cipher.getInstance(CBC_MODE);
            IvParameterSpec ivParameterSpec = getIVParameter(ivParameter);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            cipher.getBlockSize();
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(encryptedContent));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件AES解密
     *
     * @param sourceFilePath    加密文件路径
     * @param decryptedFilePath 解密后文件路径
     * @param password          密钥
     * @param ivParameter       初始化向量
     * @return 解密后的明文
     */
    public static boolean decryptFile(String sourceFilePath, String decryptedFilePath, String password, String ivParameter) {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(decryptedFilePath);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destFile);
            // 加密
            Cipher cipher = Cipher.getInstance(CBC_MODE);
            Key key = getKey(password);
            IvParameterSpec ivParameterSpec = getIVParameter(ivParameter);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

            CipherInputStream cis = new CipherInputStream(fis, cipher);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            cis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 生成密钥
     *
     * @param keyStr
     * @return
     */
    private static Key getKey(String keyStr) {
        MessageDigest sha = null;
        try {
            byte[] key = keyStr.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit

            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成初始化向量
     *
     * @param ivString
     * @return
     */
    private static IvParameterSpec getIVParameter(String ivString) {
        MessageDigest sha;
        try {
            byte[] key = ivString.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            return new IvParameterSpec(Arrays.copyOf(key, 16));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
    }


    public static void main(String[] args) {
        String content = "{\"电风扇i\": \"飞蛾\"}";
        String password = "dT1NBohfXJCg9Nzh";


        System.out.println("待加密的内容: " + content);
        String encryptedContent = encrypt(content, password, "456");
        System.out.println("加密后的密文: " + encryptedContent);
        String decryptedContent = decrypt(encryptedContent, password, "456");
        System.out.println("解密后的内容：" + decryptedContent);
    }

}

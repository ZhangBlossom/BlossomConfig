package blossom.project.config.common.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 19:47
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * MD5Util类
 */

public class MD5Util {

    public static String toMD5(String original) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(original.getBytes());
            byte[] digest = md.digest();
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to find MD5 algorithm", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //public static void main(String[] args) {
    //    String originalString = "Hello World";
    //    String md5String = toMD5(originalString);
    //    System.out.println("Original: " + originalString);
    //    System.out.println("MD5 Hash: " + md5String);
    //}
}
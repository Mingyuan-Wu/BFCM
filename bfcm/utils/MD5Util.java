package com.seanco.bfcm.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {

    private static final String SALT = "123abc";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPasswordToFormPassword(String inputPassword) {
        String saltedInputPassword = SALT + inputPassword;
        return md5(saltedInputPassword);
    }

    public static String formPasswordToDBPassword(String formPassword, String salt) {
        String saltedFormPassword = salt + formPassword;
        return md5(saltedFormPassword);
    }

    public static String inputPasswordToDBPassword(String inputPassword, String salt) {
        String formPassword = inputPasswordToFormPassword(inputPassword);
        String dbPassword = formPasswordToDBPassword(formPassword, salt);
        return dbPassword;
    }

    public static void main(String[] args) {
        System.out.println(inputPasswordToDBPassword("123456", SALT));
        // 5d684fdb8aabd9ac2e49c0207d131a3e
    }
}

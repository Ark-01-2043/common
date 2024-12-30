package com.dnpa.common.util;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncodingUtil {

    @Value("${app.encoding.secret-key:#{'dnpa'}}")
    static String secretKey;
    static byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    @Value("${app.encoding.iterationCount:#{19}}")
    static int iterationCount = 19;
    static Cipher encryptCipher;
    static Cipher decryptCipher;

    public static final String ENCRYPT_ALGORITHM = "PBEWithMD5AndDES";
    public static SecretKey getSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        return SecretKeyFactory.getInstance(ENCRYPT_ALGORITHM).generateSecret(keySpec);
    }
    public static String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        SecretKey key = getSecretKey();
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        encryptCipher = Cipher.getInstance(key.getAlgorithm());
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        String charSet = "UTF-8";
        byte[] in = plainText.getBytes(charSet);
        byte[] out = encryptCipher.doFinal(in);
        String encStr = new String(Base64.getEncoder().encode(out));
        return encStr;
    }

    public static String decrypt(String encryptedText) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKey key = getSecretKey();
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        decryptCipher = Cipher.getInstance(key.getAlgorithm());
        decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] enc = Base64.getDecoder().decode(encryptedText);
        byte[] utf8 = decryptCipher.doFinal(enc);
        String charSet = "UTF-8";
        String plainStr = new String(utf8, charSet);
        return plainStr;
    }
}

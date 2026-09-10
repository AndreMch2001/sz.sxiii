package com.sxiii;

import org.mindrot.jbcrypt.BCrypt;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoUtil {
    
    private static final int SALT_SIZE = 16;
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH_BIT = 128;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;
    
    // ========== VERIFICAR SENHA MESTRA ==========
    
    /**
     * Gera um hash BCrypt da senha mestra.
     * Usado para verificar se a senha digitada está certa.
     */
    public static String hashMasterPassword(String masterPassword) {
        return BCrypt.hashpw(masterPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verifica se a senha digitada bate com o hash salvo.
     */
    public static boolean verifyMasterPassword(String masterPassword, String hash) {
        return BCrypt.checkpw(masterPassword, hash);
    }
    
    // ========== CRIPTOGRAFAR CONTEÚDO DO COFRE ==========
    
    /**
     * Gera uma chave AES a partir da senha mestra + salt.
     * PBKDF2 transforma senha humana em chave criptográfica.
     */
    private static SecretKey deriveKey(String masterPassword, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
    
    /**
     * Criptografa texto usando AES-GCM.
     * Retorna: Base64(salt + IV + ciphertext)
     */
    public static String encrypt(String plaintext, String masterPassword) throws Exception {
        // 1. Gerar salt aleatório
        byte[] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        
        // 2. Gerar IV aleatório (Initialization Vector)
        byte[] iv = new byte[IV_SIZE];
        random.nextBytes(iv);
        
        // 3. Derivar chave AES da senha mestra
        SecretKey key = deriveKey(masterPassword, salt);
        
        // 4. Criptografar
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
        
        // 5. Combinar: salt + IV + ciphertext e codificar em Base64
        byte[] combined = new byte[salt.length + iv.length + ciphertext.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(iv, 0, combined, salt.length, iv.length);
        System.arraycopy(ciphertext, 0, combined, salt.length + iv.length, ciphertext.length);
        
        return Base64.getEncoder().encodeToString(combined);
    }
    
    /**
     * Descriptografa texto AES-GCM.
     */
    public static String decrypt(String encryptedBase64, String masterPassword) throws Exception {
        // 1. Decodificar Base64
        byte[] combined = Base64.getDecoder().decode(encryptedBase64);
        
        // 2. Extrair salt, IV e ciphertext
        byte[] salt = new byte[SALT_SIZE];
        byte[] iv = new byte[IV_SIZE];
        byte[] ciphertext = new byte[combined.length - SALT_SIZE - IV_SIZE];
        
        System.arraycopy(combined, 0, salt, 0, SALT_SIZE);
        System.arraycopy(combined, SALT_SIZE, iv, 0, IV_SIZE);
        System.arraycopy(combined, SALT_SIZE + IV_SIZE, ciphertext, 0, ciphertext.length);
        
        // 3. Derivar mesma chave AES
        SecretKey key = deriveKey(masterPassword, salt);
        
        // 4. Descriptografar
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] plaintext = cipher.doFinal(ciphertext);
        
        return new String(plaintext, "UTF-8");
    }
}
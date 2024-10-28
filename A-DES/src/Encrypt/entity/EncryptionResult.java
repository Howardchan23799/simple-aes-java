package Encrypt.entity;

/**
 * @Author {陈友豪}
 * @Date 2024/10/28 18:31
 */
public class EncryptionResult {
    private String ciphertext; // 加密后的文本
    private String key;        // 密钥

    // 构造函数
    public EncryptionResult(String ciphertext, String key) {
        this.ciphertext = ciphertext;
        this.key = key;
    }

    // 获取加密文本
    public String getCiphertext() {
        return ciphertext;
    }

    // 获取密钥
    public String getKey() {
        return key;
    }
}

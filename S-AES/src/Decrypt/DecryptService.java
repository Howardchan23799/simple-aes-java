package Decrypt;

import Common.Function;
import Common.Init;


/**
 * @Author {陈友豪}
 * @Date 2024/10/27 20:33
 */
public class DecryptService {
    public static Init init;
    public static Function function;
    public static String Decrypt(String str_ciphertext, String str_key, int sig, int round) {
        //加密 sig == 0为解密为16位   sig == 1为解密为ascii round为解密轮次
        ////round = 3 处理为看k1 k2 k1操作
        int[] plaintext2 = DecryptDo(str_ciphertext, str_key, sig, round);
        StringBuilder trans_cipher = new StringBuilder();

        if (sig == 0) {    // 返回二进制明文字符串
            return function.arryToString(plaintext2);
        } else if (sig == 1) {  // 返回ASCII字符串
            for (int i = 0; i < 2; i++) {
                int tmp_ascll = 0;
                for (int j = 0; j < 8; j++) {
                    tmp_ascll = tmp_ascll * 2 + plaintext2[i * 8 + j];
                }
                trans_cipher.append((char) tmp_ascll);
            }
            return trans_cipher.toString();
        } else{
            return "error";
        }
    }

    public static int[] DecryptEachStep(int[] mid_text, int[] key, int[] exkey1, int[] exkey2) {
        mid_text = init.fun_xor(mid_text, exkey2);  //轮密钥加2

        mid_text = function.rowDisplacement(mid_text);//行位移
        mid_text = function.halfByteReplacement2(mid_text);  //逆半字节替换
        mid_text = init.fun_xor(mid_text, exkey1);  //轮密钥加1
        mid_text = init.columnConfusion2(mid_text);  //逆列混淆

        mid_text = function.rowDisplacement(mid_text);//行位移
        mid_text = function.halfByteReplacement2(mid_text);  //逆半字节替换
        mid_text = init.fun_xor(mid_text, key);

        return mid_text;
    }

    public static int[] DecryptDo(String str_ciphertext, String str_key, int sig, int round) {
        int[] ciphertext = new int[16];
        int[] key = new int[16];

        for (int i = 0; i < 16; i++) {
            ciphertext[i] = Character.getNumericValue(str_ciphertext.charAt(i));
        }

        key = function.getKeys(str_key, round, key);

        int[] mid_text = ciphertext;
        int[] plaintext2 = new int[0];

        if (round == 1) {
            //生成密钥
            int[] exkey1 = init.expandKey(key, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);

            mid_text = DecryptEachStep(mid_text, key, exkey1, exkey2);

            plaintext2 = mid_text;
        } else if (round == 2) {
            int[] key1 = new int[16];
            int[] key2 = new int[16];
            for (int i = 0; i < 16; i++) {
                key1[i] = key[i + 16];
                key2[i] = key[i];
            }

            int[] exkey1 = init.expandKey(key1, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);
            mid_text = DecryptEachStep(mid_text, key1, exkey1, exkey2);
            exkey1 = init.expandKey(key2, 1);
            exkey2 = init.expandKey(exkey1, 2);
            mid_text = DecryptEachStep(mid_text, key2, exkey1, exkey2);

            plaintext2 = mid_text;
        } else if (round == 3) {
            int[] key1 = new int[16];  //后16位
            int[] key2 = new int[16];  //前16位
            for (int i = 0; i < 16; i++) {
                key1[i] = key[i + 16];
                key2[i] = key[i];
            }
            int[] exkey1 = init.expandKey(key2, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);
            mid_text = DecryptEachStep(mid_text, key2, exkey1, exkey2);
            exkey1 = init.expandKey(key1, 1);
            exkey2 = init.expandKey(exkey1, 2);
            mid_text = DecryptEachStep(mid_text, key1, exkey1, exkey2);
            exkey1 = init.expandKey(key2, 1);
            exkey2 = init.expandKey(exkey1, 2);
            mid_text = DecryptEachStep(mid_text, key2, exkey1, exkey2);

            plaintext2 = mid_text;
        }
        return plaintext2;
    }


    public static void CBC_Decrypt(String str_ciphertext, String str_key, String str_iv) {
        int[] ciphertext = new int[str_ciphertext.length()];
        int[] key = new int[16];
        int[] iv = new int[16];

        String str_plaintext = new String();

        for (int i = 0; i < str_ciphertext.length(); i++) {
            ciphertext[i] = Character.getNumericValue(str_ciphertext.charAt(i));
        }

        key = function.getKeys(str_key, 1, key);
        int[] exkey1 = init.expandKey(key, 1);
        int[] exkey2 = init.expandKey(exkey1, 2);

        for (int i = 0; i < 16; i++) {
            iv[i] = Character.getNumericValue(str_iv.charAt(i));
        }

        for(int i = ciphertext.length-16; i >= 0; i -= 16){
            int[] every_ciphertext = new int[16];
            int[] first_ciphertext = new int[16];
            for(int j = 0; j < 16; j++){
                every_ciphertext[j] = ciphertext[j+i];
            }
            every_ciphertext = DecryptEachStep(every_ciphertext, key, exkey1, exkey2);
            if(i == 0) {
                init.fun_xor(every_ciphertext, iv);
            }else{
                for(int j = 0; j < 16; j++){
                    first_ciphertext[j] = ciphertext[j+i-16];
                }
                init.fun_xor(every_ciphertext, first_ciphertext);
            }

            StringBuilder trans_cipher = new StringBuilder();
            for (int ii = 0; ii < 2; ii++) {
                int tmp_ascll = 0;
                for (int j = 0; j < 8; j++) {
                    tmp_ascll = tmp_ascll * 2 + every_ciphertext[ii * 8 + j];
                }
                trans_cipher.append((char) tmp_ascll);

            }

            str_plaintext = trans_cipher + str_plaintext;

        }
        System.out.println("明文 = "+str_plaintext);
    }


}


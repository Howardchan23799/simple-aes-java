package Encrypt;
import Common.Function;
import Common.Init;
import Encrypt.entity.EncryptionResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author {陈友豪}
 * @Date 2024/10/27 20:34
 */
public class EncryptService {
    public static Init init;
    public static Function function;

    public static EncryptionResult Encrypt(String str_plaintext, int sig, int round) {
        // 加密  sig == 0为 16位加密   sig == 1为ascii加密  round为加密轮次
        // round = 3 处理为看k1 k2 k1操作
        Map<Integer, int[]> res = EncryptDo(str_plaintext, sig, round);
        String ciphertext = function.arryToString(res.get(0));
        String key = function.arryToString(res.get(1));

        // 创建并返回 EncryptionResult 实体对象
        return new EncryptionResult(ciphertext, key);
    }


    public static int[] EncryptEachStep(int[] mid_text, int[] key, int[] exkey1, int[] exkey2) {
        mid_text = init.fun_xor(mid_text, key);

        mid_text = function.halfByteReplacement(mid_text);  //半字节替换
        mid_text = function.rowDisplacement(mid_text);//行位移
        mid_text = init.columnConfusion(mid_text);  //列混淆

        mid_text = init.fun_xor(mid_text, exkey1);  //轮密钥加1
        mid_text = function.halfByteReplacement(mid_text);  //半字节替换
        mid_text = function.rowDisplacement(mid_text);//行位移
        mid_text = init.fun_xor(mid_text, exkey2);  //轮密钥加2
        return mid_text;
    }

//    public static String Encrypt(String str_plaintext, String str_key, int sig, int round) {
//        //加密  sig == 0为 16位加密   sig == 1为ascii加密  round为加密轮次
//        Map<Integer, int[]> res = EncryptUsedOwnKeyDo(str_plaintext, str_key, sig, round);
//        int[] ciphertext = res.get(0);
//        return function.arryToString(ciphertext);
//    }
    public static Map<Integer, int[]> EncryptUsedOwnKeyDo(String str_plaintext, String str_key, int sig, int round) {
        //生成明文
        int[] plaintext = new int[16];

        if (sig == 1) {
            int ascll_plaintext = 0;
            for (int i = 0; i < str_plaintext.length(); i++) {
                char ch = str_plaintext.charAt(i);
                ascll_plaintext = (int) ch;
                for (int j = 7; j >= 0; j--) {
                    plaintext[j + i * 8] = ascll_plaintext % 2;
                    ascll_plaintext /= 2;
                }
            }
        } else if (sig == 0) {
            for (int i = 0; i < 16; i++) {
                plaintext[i] = Character.getNumericValue(str_plaintext.charAt(i));
            }
        }

        int[] mid_text = plaintext;
        int[] ciphertext = new int[0];

        //生成密钥
        int[] total_key = new int[0];
        if (round == 1) {
            int[] key = new int[16];
            function.getKeys(str_key, round, key);

            total_key = key;
            int[] exkey1 = init.expandKey(key, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);

            mid_text = EncryptEachStep(mid_text, key, exkey1, exkey2);

            ciphertext = mid_text;

        } else if (round == 2) {
            total_key = function.getKeys(str_key, round, total_key);

            for (int pi = 0; pi < 2; pi++) {
                int[] key = new int[16];
                for (int i = 0; i < 16; i++) {
                    key[i] = total_key[i + pi * 16];
                }

                int[] exkey1 = init.expandKey(key, 1);
                int[] exkey2 = init.expandKey(exkey1, 2);

                mid_text = EncryptEachStep(mid_text, key, exkey1, exkey2);
            }
            ciphertext = mid_text;
        }
        Map<Integer, int[]> res = new HashMap<>();
        res.put(0, ciphertext);
        return res;
    }

    public static Map<Integer, int[]> EncryptDo(String str_plaintext, int sig, int round) {
        Random random = new Random();  // 创建Random对象

        //生成明文
        int[] plaintext = new int[16];

        if (sig == 1) {
            int ascll_plaintext = 0;
            for (int i = 0; i < str_plaintext.length(); i++) {
                char ch = str_plaintext.charAt(i);
                ascll_plaintext = (int) ch;
                for (int j = 7; j >= 0; j--) {
                    plaintext[j + i * 8] = ascll_plaintext % 2;
                    ascll_plaintext /= 2;
                }
            }
        } else if (sig == 0) {
            for (int i = 0; i < 16; i++) {
                plaintext[i] = Character.getNumericValue(str_plaintext.charAt(i));
            }
        }


        int[] mid_text = plaintext;
        int[] ciphertext = new int[0];

        //生成密钥
        int[] total_key = new int[0];
        if (round == 1) {
            int[] key = new int[16];
            for (int i = 0; i < 16; i++) {
                key[i] = random.nextInt(2);
            }

            total_key = key;
            int[] exkey1 = init.expandKey(key, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);

            mid_text = EncryptEachStep(mid_text, key, exkey1, exkey2);

            ciphertext = mid_text;

        } else if (round == 2) {
            total_key = new int[32];
            for (int pi = 0; pi < 2; pi++) {
                int[] key = new int[16];
                for (int i = 0; i < 16; i++) {
                    key[i] = random.nextInt(2);
                }
                for (int i = 0; i < 16; i++) {
                    total_key[i + pi * 16] = key[i];
                }

                int[] exkey1 = init.expandKey(key, 1);
                int[] exkey2 = init.expandKey(exkey1, 2);

                mid_text = EncryptEachStep(mid_text, key, exkey1, exkey2);
            }
            ciphertext = mid_text;
        } else if (round == 3) {
            total_key = new int[32];
            int[] key1 = new int[16];
            for (int pi = 0; pi < 2; pi++) {
                int[] key = new int[16];
                for (int i = 0; i < 16; i++) {
                    key[i] = random.nextInt(2);
                }
                for (int i = 0; i < 16; i++) {
                    total_key[i + pi * 16] = key[i];
                }
                if (pi == 0) {
                    key1 = key;
                }

                int[] exkey1 = init.expandKey(key, 1);
                int[] exkey2 = init.expandKey(exkey1, 2);

                mid_text = EncryptEachStep(mid_text, key, exkey1, exkey2);
            }

            int[] exkey1 = init.expandKey(key1, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);

            mid_text = EncryptEachStep(mid_text, key1, exkey1, exkey2);
            ciphertext = mid_text;
        }
        Map<Integer, int[]> res = new HashMap<>();
        ;
        res.put(0, ciphertext);
        res.put(1, total_key);
        return res;
    }



    public static void CBC_Encrypt(String str_plaintext) {
        Random random = new Random();  // 创建Random对象

        int[] ciphertext = new int[0];

        if (str_plaintext.length() % 2 == 1) {
            str_plaintext = str_plaintext + " ";
        }

        //生成密钥
        int[] key = new int[16];
        for (int i = 0; i < 16; i++) {
            key[i] = random.nextInt(2);
        }
        int[] exkey1 = init.expandKey(key, 1);
        int[] exkey2 = init.expandKey(exkey1, 2);

        //生成IV
        int[] iv = new int[16];
        for (int i = 0; i < 16; i++) {
            iv[i] = random.nextInt(2);
        }

        int[] every_ciphertext = new int[16];
        for (int i = 0; i < str_plaintext.length(); i += 2) {
            String every_str_plaintext = str_plaintext.substring(i, i + 2);
            int[] plaintext = new int[16];
            int ascll_plaintext = 0;
            for (int j = 0; j < every_str_plaintext.length(); j++) {
                char ch = every_str_plaintext.charAt(j);
                ascll_plaintext = (int) ch;
                for (int k = 7; k >= 0; k--) {
                    plaintext[k + j * 8] = ascll_plaintext % 2;
                    ascll_plaintext /= 2;
                }
            }
            if (i == 0) {
                init.fun_xor(plaintext, iv);
                plaintext = EncryptEachStep(plaintext, key, exkey1, exkey2);
                every_ciphertext = plaintext;
            }else {
                init.fun_xor(plaintext, every_ciphertext);
                plaintext = EncryptEachStep(plaintext, key, exkey1, exkey2);
                every_ciphertext = plaintext;
            }

            //扩展ciphertext
            int[] copy = Arrays.copyOf(ciphertext, ciphertext.length);
            ciphertext = new int[ciphertext.length+16];
            for(int j = 0; j < copy.length; j++){
                ciphertext[j] = copy[j];
            }
            for(int j = 0; j < plaintext.length; j++){
                ciphertext[j+copy.length] = plaintext[j];
            }
        }
        System.out.println("密文 = ");
        for(int i = 0 ; i < ciphertext.length; i++){
            System.out.print(ciphertext[i]);
        }
        System.out.println();

        System.out.println("密钥 = ");
        for(int i = 0 ; i < key.length; i++){
            System.out.print(key[i]);
        }
        System.out.println();

        System.out.println("IV = ");
        for(int i = 0 ; i < iv.length; i++){
            System.out.print(iv[i]);
        }
        System.out.println();

    }



}

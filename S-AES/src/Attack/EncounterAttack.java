package Attack;

import Decrypt.DecryptService;
import Encrypt.EncryptService;
import Common.Init;

import java.util.*;

/**
 * @Author {陈友豪}
 * @Date 2024/10/27 20:24
 */
public class EncounterAttack {
    public static Init init;
    public static EncryptService encryptService;
    public static DecryptService decryptService;

    public static void EncounterAttack(String str_plaintext1, String str_ciphertext1, String str_plaintext2, String str_ciphertext2) {
        System.out.println("正在处理明密文对1");
        List<int[]> list1 = EncounterAttackOnePair(str_plaintext1, str_ciphertext1);
        System.out.println("正在处理明密文对2");
        List<int[]> list2 = EncounterAttackOnePair(str_plaintext2, str_ciphertext2);
        Set<String> set = new HashSet<>(); // 用于存储list1中所有数组的字符串表示
        for (int[] array : list1) {
            set.add(Arrays.toString(array)); // 将数组转换为字符串并添加到HashSet中
        }

        System.out.println("正在查找匹配密钥");
        List<String> matchedKeys = new ArrayList<>(); // 存储匹配的结果，以避免在循环中直接输出

        for (int[] array : list2) {
            String arrayStr = Arrays.toString(array);
            if (set.contains(arrayStr)) {
                matchedKeys.add("找到相同的数组: " + arrayStr);
            }
        }

        // 循环结束后输出所有匹配的结果
        for (String match : matchedKeys) {
            System.out.println(match);
        }


    }

    public static List<int[]> EncounterAttackOnePair(String str_plaintext, String str_ciphertext) {
        int[] plaintext = new int[16];
        int[] ciphertext = new int[16];
        for (int i = 0; i < 16; i++) {
            plaintext[i] = Character.getNumericValue(str_plaintext.charAt(i));
            ciphertext[i] = Character.getNumericValue(str_ciphertext.charAt(i));
        }

        List<int[]> list = new ArrayList<>();
        Map<String, List<Integer>> map = new HashMap<>(); // 使用HashMap存储中间结果和对应的索引
        for (int i = 0; i < 65536; i++) {
            int[] key1 = new int[16];
            init.xtob(key1, 0, 15, i);
            int[] exkey1 = init.expandKey(key1, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);
            int[] mid = encryptService.EncryptEachStep(plaintext, key1, exkey1, exkey2);
            String midKey = Arrays.toString(mid); // 将数组转换为字符串作为HashMap的键
            map.computeIfAbsent(midKey, k -> new ArrayList<>()).add(i);
        }

        for (int i = 0; i < 65536; i++) {
            int[] key2 = new int[16];
            init.xtob(key2, 0, 15, i);
            int[] exkey1 = init.expandKey(key2, 1);
            int[] exkey2 = init.expandKey(exkey1, 2);
            int[] res = decryptService.DecryptEachStep(ciphertext, key2, exkey1, exkey2);
            String resKey = Arrays.toString(res);
            if (map.containsKey(resKey)) {
                for (int index : map.get(resKey)) {
                    int[] total_key = new int[32];
                    init.xtob(total_key, 0, 15, index);
                    System.arraycopy(key2, 0, total_key, 16, 16);
                    list.add(total_key);
                }
            }
        }
        return list;
    }


}

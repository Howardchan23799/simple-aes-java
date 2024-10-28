package Common;

/**
 * @Author {陈友豪}
 * @Date 2024/10/28 14:26
 */
public class Function {
    public static  Init init;
    public static int[] halfByteReplacement(int[] mid_text) {
        for (int i = 0; i < 4; i++) {
            int[] tmparr = new int[4];
            for (int j = 0; j < 4; j++) {
                tmparr[j] = mid_text[i * 4 + j];
            }
            tmparr = init.halfByteReplacement(tmparr);
            for (int j = 0; j < 4; j++) {
                mid_text[i * 4 + j] = tmparr[j];
            }
        }
        return mid_text;
    }

    public static int[] halfByteReplacement2(int[] mid_text) {
        for (int i = 0; i < 4; i++) {
            int[] tmparr = new int[4];
            for (int j = 0; j < 4; j++) {
                tmparr[j] = mid_text[i * 4 + j];
            }
            tmparr = init.halfByteReplacement2(tmparr);
            for (int j = 0; j < 4; j++) {
                mid_text[i * 4 + j] = tmparr[j];
            }
        }
        return mid_text;
    }

    public static int[] rowDisplacement(int[] mid_text) {
        for (int i = 0; i < 4; i++) {
            int t = mid_text[i + 8];
            mid_text[i + 8] = mid_text[i + 12];
            mid_text[i + 12] = t;
        }
        return mid_text;
    }

    public static int[] getKeys(String str_key, int round, int[] total_key) {
        if (round == 1) {
            for (int i = 0; i < 16; i++) {
                total_key[i] = Character.getNumericValue(str_key.charAt(i));
            }
        } else if (round == 2 || round == 3) {
            total_key = new int[32];
            for (int i = 0; i < 32; i++) {
                total_key[i] = Character.getNumericValue(str_key.charAt(i));
            }
        }
        return total_key;
    }

    public static String arryToString(int[] arry) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arry.length; i++) {
            result.append(arry[i]);
        }
        return result.toString(); // 返回明文字符串
    }

}

package Common;

import java.util.Arrays;

/**
 * @Author {陈友豪}
 * @Date 2024/10/27 19:49
 */
public class Init {
    public static int[][] S_box = {
            {9,4,10,11},
            {13,1,8,5},
            {6,2,0,3},
            {12,14,15,7}};

    public static int[][] S_box2 = {
            {10,5,9,11},
            {1,7,8,15},
            {6,0,2,3},
            {12,4,13,14}
    };

    public static int[][] GF_multiply = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //0
            {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15}, //1
            {0,2,4,6,8,10,12,14,3,1,7,5,11,9,15,13}, //2
            {0,3,6,5,12,15,10,9,11,8,13,14,7,4,1,2}, //3
            {0,4,8,12,3,7,11,15,6,2,14,10,5,1,13,9}, //4
            {0,5,10,15,7,2,13,8,14,11,4,1,9,12,3,6}, //5
            {0,6,12,10,11,13,7,1,5,3,9,15,14,8,2,4}, //6
            {0,7,14,9,15,8,1,6,13,11,3,4,2,5,12,11}, //7
            {0,8,3,11,6,14,5,13,12,4,15,7,10,2,9,1}, //8
            {0,9,1,8,2,11,3,10,4,13,5,12,6,15,7,14}, //9
            {0,10,7,13,14,4,9,3,15,5,8,2,1,11,6,12}, //A
            {0,11,5,14,10,1,15,4,7,12,2,9,13,6,8,3}, //B
            {0,12,11,7,5,9,14,2,10,6,1,13,15,3,4,8}, //C
            {0,13,9,4,1,12,8,5,2,15,11,6,3,14,10,7}, //D
            {0,14,15,1,13,3,2,12,9,7,6,8,4,10,11,5}, //E
            {0,15,13,2,9,6,4,11,1,14,12,3,8,7,5,10}
    };

    public static int[][] rcon = {
            {1,0,0,0, 0,0,0,0},
            {0,0,1,1, 0,0,0,0}
    };


    public static int[] fun_xor(int[] a, int[] b){  //输入整个数组
        int[] c = new int[a.length];
        for(int i = 0; i < a.length; i++){
            c[i] = a[i] ^ b[i];
        }
        return c;
    }

    public static int[] halfByteReplacement(int[] a){  //输入四位数组
        int[] ans = new int[4];
        int x = a[0];
        x = x*2 + a[1];
        int y = a[2];
        y = y*2 + a[3];
        int t = S_box[x][y];
        for(int i = 3; i >= 0; i--){
            ans[i] = t % 2;
            t /= 2;
        }
        return ans;
    }

    public static int[] halfByteReplacement2(int[] a){  //输入四位数组
        int[] ans = new int[4];
        int x = a[0];
        x = x*2 + a[1];
        int y = a[2];
        y = y*2 + a[3];
        int t = S_box2[x][y];
        for(int i = 3; i >= 0; i--){
            ans[i] = t % 2;
            t /= 2;
        }
        return ans;
    }

    public static int[] columnConfusion(int[] a){
        int s00 = btox(a,0,3);
        int s01 = btox(a,8,11);
        int s10 = btox(a,4,7);
        int s11 = btox(a,12,15);

        int s00q = s00^GF_multiply[4][s10];
        int s10q = GF_multiply[4][s00]^s10;
        int s01q = s01^GF_multiply[4][s11];
        int s11q = GF_multiply[4][s01]^s11;

        xtob(a,0,3,s00q);
        xtob(a,8,11,s01q);
        xtob(a,4,7,s10q);
        xtob(a,12,15,s11q);

        return a;
    }

    public static int[] columnConfusion2(int[] a){
        int s00 = btox(a,0,3);
        int s01 = btox(a,8,11);
        int s10 = btox(a,4,7);
        int s11 = btox(a,12,15);

        int s00q = GF_multiply[9][s00]^GF_multiply[2][s10];
        int s10q = GF_multiply[2][s00]^GF_multiply[9][s10];
        int s01q = GF_multiply[9][s01]^GF_multiply[2][s11];
        int s11q = GF_multiply[2][s01]^GF_multiply[9][s11];

        xtob(a,0,3,s00q);
        xtob(a,8,11,s01q);
        xtob(a,4,7,s10q);
        xtob(a,12,15,s11q);

        return a;
    }

    public static int btox(int[] a, int i, int j){
        int ans = 0;
        int pi = i;
        while(pi <= j){
            ans = ans*2 + a[pi];
            pi++;
        }
        return ans;
    }

    public static void xtob(int[] a, int i, int j, int x){
        for(int p = j; p >= i; p--){
            a[p] = x%2;
            x /= 2;
        }
    }

    public static int[] expandKey(int[] key, int pi){  //输入16位密钥 pi输入1,2
        int[] w0 = new int[8];
        int[] w1 = new int[8];
        int[] ans_key = new int[16];
        for(int i = 0 ; i < 8; i++){
            w0[i] = key[i];
            w1[i] = key[i+8];
        }
        int[] tmp = Arrays.copyOf(w1,w1.length);
        int[] w2 = fun_xor(w0,expandKey_g(w1, pi));
        w1 = tmp;
        int[] w3 = fun_xor(w2,w1);
        for(int i = 0; i < 8; i++){
            ans_key[i] = w2[i];
            ans_key[i+8] = w3[i];
        }
        return ans_key;
    }

    public static int[] expandKey_g(int[] a, int pi){  //输入8位数组 pi输入1,2
        for(int i = 0; i < 4; i++){
            int t = a[i];
            a[i] = a[i+4];
            a[i+4] = t;
        }
        for(int i = 0; i < 2; i++){
            int[] tmparr = new int[4];
            for(int j = 0; j < 4; j++){
                tmparr[j] = a[i*4 + j];
            }
            tmparr = halfByteReplacement(tmparr);
            for(int j = 0; j < 4; j++){
                a[i*4 + j] = tmparr[j];
            }
        }
        a = fun_xor(a, rcon[pi-1]);
        return a;
    }
}

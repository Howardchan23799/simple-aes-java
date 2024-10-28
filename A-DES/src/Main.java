/**
 * @Author {陈友豪}
 * @Date 2024/10/27 19:44
 */
import Attack.EncounterAttack;
import Decrypt.DecryptService;
import Encrypt.EncryptService;
import Encrypt.entity.EncryptionResult;

import java.util.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        EncryptService encryptService = new EncryptService();
        DecryptService decryptService = new DecryptService();
        EncounterAttack encounterAttack = new EncounterAttack();
        encounterAttack.EncounterAttack("1100001111000011",
                                        "0100011100110110",
                                        "1111000011110000",
                                        "1110010101111000" );

        encryptService. CBC_Encrypt("yyhyyds");
        decryptService.CBC_Decrypt("0110000011111001100110001111100101100000111011011101101100010110",
                                    "1000001000110111",
                                "1110110001101100");

    }
}
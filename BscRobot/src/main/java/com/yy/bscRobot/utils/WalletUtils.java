package com.yy.bscRobot.utils;

import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;

public class WalletUtils {

    private static final SecureRandom secureRandom = new SecureRandom();
    public static final int PRIVATE_KEY_SIZE = 32;

    public static String generateBip39Wallet() {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "123456");
        ECKeyPair ecKeyPair = ECKeyPair.create(Hash.sha256(seed));
        BigInteger privateKey = ecKeyPair.getPrivateKey();
        String privateKeyString=Numeric.toHexStringWithPrefix(privateKey);
        return privateKeyString.toLowerCase(Locale.ROOT);
    }

}

package util;

import java.util.Random;

class CodeGenerator {

    static CodeGenerator getInstance() {
        return CodeGenerator.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CodeGenerator instance = new CodeGenerator();
    }

    private CodeGenerator() {
    }


    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private static final int codeLength = 8;


    String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            code.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return code.toString();
    }
}

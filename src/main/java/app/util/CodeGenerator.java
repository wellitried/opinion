package app.util;

import java.util.Random;

class CodeGenerator {

    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int codeLength = 10;


    private CodeGenerator() {
    }

    static CodeGenerator getInstance() {
        return CodeGenerator.SingletonHolder.instance;
    }

    String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            code.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return code.toString();
    }

    private static class SingletonHolder {
        private static final CodeGenerator instance = new CodeGenerator();
    }
}

package com.inov8.integration.middleware.util;

public class DecimalHexBinaryConverter {

    public static String decimalToHexa(Integer decimalNumber) {
        return Integer.toHexString(decimalNumber);
    }

    public static String decimalToBinary(Integer decimalNumber) {
        StringBuilder binaryNumber = new StringBuilder();
        StringBuilder sbBinary = new StringBuilder();
        String binaryString = Integer.toBinaryString(decimalNumber);
        char[] binary = binaryString.toCharArray();
        int counter = 0;
        for (int i=binary.length-1; i>=0; i--) {
            counter++;
            sbBinary.append(binary[i]);
            if (counter == 4) counter = 0;
        }

        // ex: dec [100] == binary [0110 0100]
        for (int i=0; i<4-counter; i++) {
            if (counter > 0) sbBinary.append("0");
        }

        for (int i=sbBinary.length()-1; i>=0;i--) {
            binaryNumber.append(sbBinary.toString().charAt(i));
        }

        return binaryNumber.toString();
    }

    public static Integer binaryToDecimal(String binaryNumber) {
        return Integer.parseInt(binaryNumber, 2);
    }

    public static String binaryToHexa(String binaryNumber) {
        return decimalToHexa(binaryToDecimal(binaryNumber));
    }

    public static Integer hexaToDecimal(String hexaNumber) {
        return Integer.parseInt(hexaNumber, 16);
    }

    public static String hexaToBinary(String hexaNumber) {
        return decimalToBinary(hexaToDecimal(hexaNumber));
    }
    
    
}
package com.inov8.verifly.common.util;

import java.util.Random;
/**
 *
 *
 * @author irfan mirza
 * @version 1.0
 * @date  06-Sep-2006
 */
public class RandomNumberGenerator {
    public static String generatePin(int pinLength)
    {
        Random generator = new Random();
        String pin = new String();
        for (int i = 0; i < pinLength; i++)
        {

            pin = pin+ generator.nextInt(9);
        }

        return pin;
    }

}

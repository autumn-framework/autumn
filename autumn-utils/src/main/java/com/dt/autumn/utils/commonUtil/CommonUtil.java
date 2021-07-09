package com.dt.autumn.utils.commonUtil;

import java.util.*;

public class CommonUtil {

    private static volatile CommonUtil instance;

    private CommonUtil() {

    }

    public static CommonUtil getInstance() {
        if (instance == null) {
            synchronized (CommonUtil.class) {
                if (instance == null) {
                    instance = new CommonUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Generate random string
     *
     * @param targetStringLength - Length of the required string
     * @return
     */
    public String generateRandomString(int targetStringLength) {
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        String output = generatedString.toUpperCase();
        return output;
    }

    public String generateRandomStringWithLimits(int targetStringLength,char startChar,char endChar) {
        int leftLimit = (int)startChar;
        int rightLimit = (int)endChar;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        String output = generatedString.toUpperCase();
        return output;
    }


    /**
     * Get Random number
     *
     * @param length
     * @return
     */
    public int getRandomNumber(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length);
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /**
     *  Get Random MacAddress
     * @return
     */
    public String getRandomMacAddress(){
        String mac = "";
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int n = r.nextInt(255);
            mac += String.format("%02x", n) + (i<5?":":"");
        }
        return mac.toUpperCase();
    }
}

package com.dt.autumn.utils.commonUtil;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

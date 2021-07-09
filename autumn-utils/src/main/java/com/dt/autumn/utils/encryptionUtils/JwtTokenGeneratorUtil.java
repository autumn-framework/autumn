package com.dt.autumn.utils.encryptionUtils;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.*;

public class JwtTokenGeneratorUtil {

    private static volatile JwtTokenGeneratorUtil instance;

    public static JwtTokenGeneratorUtil getInstance() {
        if (instance == null) {
            synchronized (JwtTokenGeneratorUtil.class) {
                if (instance == null) {
                    instance = new JwtTokenGeneratorUtil();
                }
            }
        }
        return instance;
    }


    public String generateJWTTokenHMAC256(Map<String, Object> headers, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return generateJWTToken(headers, algorithm);
    }

    private String generateJWTToken(Map<String, Object> headers, Algorithm algorithm) {
        JWTCreator.Builder jwtBuilder = JWT.create();
        jwtBuilder.withHeader(headers);
        return jwtBuilder.sign(algorithm);
    }

    public String generateJWTTokenHMAC256(JWTCreator.Builder jwtBuilder, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return generateJWTToken(jwtBuilder, algorithm);
    }

    private String generateJWTToken(JWTCreator.Builder jwtBuilder, Algorithm algorithm) {
        return jwtBuilder.sign(algorithm);
    }


}

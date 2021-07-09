package com.dt.autumn.utils.encryptionUtils;

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
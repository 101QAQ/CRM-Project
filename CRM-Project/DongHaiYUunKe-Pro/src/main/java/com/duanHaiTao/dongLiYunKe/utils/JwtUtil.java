package com.duanHaiTao.dongLiYunKe.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.Map;

public class JwtUtil {

    private static final String  secret="&#@88712&@";

    public static String getToken (Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);
        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));
    }


    public static Object toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static String getToken (TUser tUser,Calendar time) throws JsonProcessingException{
        String userJson = new ObjectMapper().writeValueAsString(tUser);
        return JWT.create().withExpiresAt(time.getTime())
                .withClaim("user", userJson)
                .sign(Algorithm.HMAC256(secret));
    }

    public static DecodedJWT verifyToken(String token){
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    public static TUser parseToken(String token) throws JsonProcessingException {
        DecodedJWT decodedJWT = verifyToken(token);
        return new ObjectMapper().readValue(decodedJWT.getClaim("user").asString(), TUser.class);
    }

}

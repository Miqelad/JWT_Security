/**
 * @author BomboRa
 */
package com.miq.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.miq.security.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTService {
    @Value("${jwt-secret}")
    private String secret;

    /**
     * Способ подписи и создания JWT с использованием введенного секрета
     *
     * @param email {@link User#getEmail()}
     * @return {@link String} генерация токена по секрету
     * @throws IllegalArgumentException некорректность аргументов
     * @throws JWTCreationException     проблема сборки jwt
     */
    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("AllMine")
                .withClaim("email", email)
                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60*24))
                .withIssuer("MIQ")
                .sign(Algorithm.HMAC256(secret));
    }


    /**
     * Способ проверки JWT,
     * а затем декодирования и извлечения электронной почты пользователя,
     * хранящейся в payload токена
     *
     * @param token {@link String} токен
     * @return {@link String} email пользователя
     */
    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException, JsonProcessingException {
//        String[] chunks = token.split("\\.");
//        Base64.Decoder decoder = Base64.getUrlDecoder();
//        String header = new String(decoder.decode(chunks[0]));
//        String payload = new String(decoder.decode(chunks[1]));
//        UserDetailsInfoHeader userDetailsInfoHeader = new ObjectMapper().readValue(header, UserDetailsInfoHeader.class);
//        UserDetailsInfoPayload userDetailsInfoPayload = new ObjectMapper().readValue(payload, UserDetailsInfoPayload.class);

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
//                .withSubject(userDetailsInfoPayload.getSub())
//                .withIssuer(userDetailsInfoPayload.getIss())
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}

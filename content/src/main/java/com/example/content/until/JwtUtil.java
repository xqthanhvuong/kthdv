package com.example.content.until;

import com.example.content.constant.JwtConstant;
import com.example.content.exception.InvalidTokenException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtUtil {

    @NonFinal
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hours


    public SignedJWT parseToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new InvalidTokenException(JwtConstant.TOKEN_EMPTY);
        }
        try {
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new InvalidTokenException(JwtConstant.TOKEN_PARSE_ERROR);
        }

    }

    public boolean validateTokenExpiration(SignedJWT signedJWT) {
        Date expirationDate;
        try {
            expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new InvalidTokenException(JwtConstant.TOKEN_PARSE_ERROR);
        }
        return ObjectUtils.isNotEmpty(expirationDate) && expirationDate.after(new Date());
    }

}

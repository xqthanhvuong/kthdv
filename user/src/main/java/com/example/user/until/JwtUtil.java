package com.example.user.until;

import com.example.user.constant.JwtConstant;
import com.example.user.exception.InvalidTokenException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.privateKey}")
    @NonFinal
    private String privateKeyStr;
    @NonFinal
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    public String generateToken(String username, String role, Long userId) {
        try {
            String cleanedKey = privateKeyStr.replace("\n", "").replace("\r", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "")
                    .replace(" ", "");
            byte[] decodedKey = Base64.getDecoder().decode(cleanedKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("id", userId)
                    .issuer("medhealth.com")
                    .claim("scope", role) // Add role to the JWT
                    .issueTime(new Date())
                    .expirationTime(new Date(new Date().getTime() + EXPIRE_DURATION))
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            JWSSigner signer = new RSASSASigner(privateKey);
            signedJWT.sign(signer);
            return signedJWT.serialize();

        } catch (JOSEException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InvalidTokenException(JwtConstant.JWT_GENER_ERROR);
        }
    }

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

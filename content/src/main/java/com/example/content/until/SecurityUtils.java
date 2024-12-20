package com.example.content.until;

import com.nimbusds.jose.JWSObject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtils {

    /**
     * Retrieves the username of the currently authenticated user.
     *
     * @return the username of the current user or null if no authentication is
     * present.
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }

    public static Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated()) {
                throw new IllegalStateException("User is not authenticated");
            }

            if (authentication.getPrincipal() instanceof Jwt jwt) {
                // Lấy giá trị id từ claim trong token JWT
                Object idClaim = jwt.getClaim("id");
                if (idClaim instanceof Number idNumber) {
                    return idNumber.longValue(); // Ép kiểu thành Long
                } else {
                    throw new IllegalStateException("Token does not contain a valid 'id'");
                }
            } else {
                throw new IllegalStateException("Authentication principal is not an instance of Jwt");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getJwtTokenFromRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader("Authorization"); // Lấy token từ header Authorization
    }

}


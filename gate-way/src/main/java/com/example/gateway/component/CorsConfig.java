package com.example.gateway.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        // Tạo đối tượng CorsConfiguration
        CorsConfiguration config = new CorsConfiguration();
        // Cho phép tất cả origin
        config.addAllowedOrigin("*");
        // Cho phép tất cả header
        config.addAllowedHeader("*");
        // Cho phép tất cả method (GET, POST, PUT, DELETE, …)
        config.addAllowedMethod("*");
        // Nếu cần, bạn cũng có thể set cho phép credentials
        // config.setAllowCredentials(true);

        // Khai báo source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Mọi đường dẫn đều áp dụng config này
        source.registerCorsConfiguration("/**", config);

        // Trả về bean CorsWebFilter với cấu hình đã thiết lập
        return new CorsWebFilter(source);
    }
}


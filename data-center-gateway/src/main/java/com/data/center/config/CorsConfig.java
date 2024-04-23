//package com.data.center.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class CorsConfig {
//    @Bean
//    public CorsFilter corsFilter()
//    {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        // 是否允许请求带有验证信息
//        config.setAllowCredentials(true);
//
//        // 允许访问的客户端域名
//        // (springboot2.4以上的加入这一段可解决 allowedOrigins cannot contain the special value "*"问题)
//        List<String> allowedOriginPatterns = new ArrayList<>();
//        allowedOriginPatterns.add("*");
//        config.setAllowedOriginPatterns(allowedOriginPatterns);
//
//        // 设置访问源地址
//        // config.addAllowedOrigin("*");
//        // 设置访问源请求头
//        config.addAllowedHeader("*");
//        // 设置访问源请求方法
//        config.addAllowedMethod("*");
//        // 对接口配置跨域设置
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter((CorsConfigurationSource) source);
//    }
//}
//

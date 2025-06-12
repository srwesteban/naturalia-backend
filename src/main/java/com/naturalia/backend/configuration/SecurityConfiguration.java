package com.naturalia.backend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 🔓 Permitir TODO temporalmente
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();




    }
//        http
//                .cors().and()
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//
//                        // 🔓 Auth y documentación pública
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
//
//                        // 🌐 Acceso público
//                        .requestMatchers(HttpMethod.GET, "/stays/**", "/categories/**", "/features/**", "/policies/**").permitAll()
//
//                        // 🏡 Stays
//                        .requestMatchers(HttpMethod.POST, "/stays/**").hasAnyRole("HOST", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/stays/**").hasAnyRole("HOST", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/stays/**").hasAnyRole("HOST", "ADMIN")
//
//                        // 🗂️ Features
//                        .requestMatchers(HttpMethod.POST, "/features/**").hasAnyRole("ADMIN", "HOST")
//                        .requestMatchers(HttpMethod.PUT, "/features/**").hasAnyRole("ADMIN", "HOST")
//                        .requestMatchers(HttpMethod.DELETE, "/features/**").hasAnyRole("ADMIN", "HOST")
//                        //& Categories
//                        .requestMatchers(HttpMethod.POST,  "/categories/**").hasAnyRole("ADMIN", "HOST")
//                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasAnyRole("ADMIN", "HOST")
//                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasAnyRole("ADMIN", "HOST")
//
//                        // ⭐ Favoritos
//                        .requestMatchers(HttpMethod.GET, "/favorites/**").hasAnyRole("USER", "HOST")
//                        .requestMatchers(HttpMethod.POST, "/favorites/**").hasAnyRole("USER", "HOST")
//                        .requestMatchers(HttpMethod.PUT, "/favorites/**").hasAnyRole("USER", "HOST")
//                        .requestMatchers(HttpMethod.DELETE, "/favorites/**").hasAnyRole("USER", "HOST")
//
//                        // 📅 Reservas
//                        .requestMatchers(HttpMethod.GET, "/reservations/**").hasAnyRole("USER", "HOST")
//                        .requestMatchers(HttpMethod.POST, "/reservations/**").hasAnyRole("USER", "HOST")
//                        .requestMatchers(HttpMethod.PUT, "/reservations/**").hasAnyRole("USER", "HOST")
//                        .requestMatchers(HttpMethod.DELETE, "/reservations/**").hasAnyRole("USER", "HOST")
//
//                        // 📝 Reseñas
//                        .requestMatchers(HttpMethod.GET, "/reviews", "/reviews/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/reviews", "/reviews/**").hasAnyRole("USER", "HOST", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/reviews", "/reviews/**").hasAnyRole("USER", "HOST", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/reviews", "/reviews/**").hasAnyRole("USER", "HOST", "ADMIN")
//
//                        // 👤 Usuarios
//                        .requestMatchers(HttpMethod.PUT, "/users/*/role").hasRole("USER")
//                        .requestMatchers("/users/**").hasRole("ADMIN")
//
//                        // 🔐 Todo lo demás requiere autenticación
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of(
//                "http://localhost:5173",
//                "https://naturalia-frontend.vercel.app"
//        ));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }




}

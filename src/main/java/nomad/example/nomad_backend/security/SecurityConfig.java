package nomad.example.nomad_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final OAuth2SuccessHandler oauth2SuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {


        return http

                .csrf(csrf -> csrf.disable())


                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/refresh-token",
                                "/api/auth/logout",
                                "/oauth2/**",
                                "/login/**",
<<<<<<< HEAD
                                "/api/v1/wishlist/**",
=======
                                "/api/opportunities",
                                "/api/v1/test-email",
                                "/api/opportunities/cards",
                                "/api/contact/**",
>>>>>>> 98d0d332fc23ecb712840674817962454018730d
                                "/api/v1/projects/**"
                        )
                        .permitAll()


                        .requestMatchers(
                                "/api/auth/me"
                        )
                        .authenticated()


                        .anyRequest()
                        .authenticated()
                )


                .oauth2Login(oauth ->
                        oauth
                                .successHandler(oauth2SuccessHandler)
                )


                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )


                .build();
    }
}
package nomad.example.nomad_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
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

                        // Public endpointlər
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/contact/**",
                                "/oauth2/**",
                                "/login/**"
                        ).permitAll()


                        // USER + ADMIN
                        .requestMatchers(
                                "/api/auth/me",
                                "/api/v1/projects/**",
                                "/api/v1/wishlist/**"
                        ).hasAnyRole("USER", "ADMIN")


                        // Hamı baxa bilər
                        .requestMatchers(
                                "/api/opportunities/**"
                        ).permitAll()


                        // ADMIN
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")


                        .anyRequest()
                        .authenticated()
                )


                .oauth2Login(oauth ->
                        oauth.successHandler(oauth2SuccessHandler)
                )


                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}
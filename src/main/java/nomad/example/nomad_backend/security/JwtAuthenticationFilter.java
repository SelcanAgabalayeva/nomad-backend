package nomad.example.nomad_backend.security;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String authHeader =
                request.getHeader("Authorization");


        if(authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request,response);
            return;
        }


        String token =
                authHeader.substring(7);


        try {

            String email =
                    jwtService.extractEmail(token);


            User user =
                    userRepository.findByEmail(email)
                            .orElse(null);


            if(user != null &&
                    jwtService.isTokenValid(token,user)) {


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.emptyList()
                        );


                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }


        } catch (ExpiredJwtException e){

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    """
                    {
                      "message": "Token expired",
                      "status": 401
                    }
                    """
            );

            return;


        } catch (Exception e){

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    """
                    {
                      "message": "Invalid token",
                      "status": 401
                    }
                    """
            );

            return;
        }


        filterChain.doFilter(request,response);
    }
}


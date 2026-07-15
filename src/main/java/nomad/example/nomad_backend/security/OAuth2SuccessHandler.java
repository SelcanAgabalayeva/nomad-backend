package nomad.example.nomad_backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;

    private final JwtService jwtService;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {


        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) authentication;


        OAuth2User oauthUser =
                token.getPrincipal();


        String email =
                oauthUser.getAttribute("email");


        String name =
                oauthUser.getAttribute("name");


        String[] names = name.split(" ");


        String firstName = names[0];


        String lastName;

        if (names.length > 1) {
            lastName = names[names.length - 1];
        } else {
            lastName = "";
        }


        User user =
                userRepository.findByEmail(email)
                        .orElseGet(() -> {


                            User newUser = new User();


                            newUser.setFirstName(firstName);

                            newUser.setLastName(lastName);

                            newUser.setEmail(email);


                            newUser.setPassword("GOOGLE_USER");

                            newUser.setPhoneNumber(
                                    "GOOGLE_" + email
                            );


                            newUser.setProvider("GOOGLE");


                            return userRepository.save(newUser);

                        });



        String jwt =
                jwtService.generateToken(user);



        response.setContentType(
                "application/json"
        );


        response.getWriter()
                .write("""
                {
                    "token":"%s"
                }
                """.formatted(jwt));

    }
}
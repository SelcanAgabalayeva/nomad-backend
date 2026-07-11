package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthController {


    @GetMapping("/oauth-success")
    public String success(
            @AuthenticationPrincipal OAuth2User user
    ){

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");


        System.out.println(email);
        System.out.println(name);


        return "home";
    }

}

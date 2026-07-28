package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.UserResponseDto;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl
        implements ProfileService {


    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public UserResponseDto uploadProfileImage(
            MultipartFile file
    ) throws IOException {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        User user =
                (User) auth.getPrincipal();


        String url = fileStorageService.save(file);


        user.setProfileImageUrl(url);


        userRepository.save(user);


        return modelMapper.map(
                user,
                UserResponseDto.class
        );
    }



    @Override
    @Transactional
    public void deleteProfileImage(){

        User user =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();


        user.setProfileImageUrl(null);


        userRepository.save(user);
    }

}

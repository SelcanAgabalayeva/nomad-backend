package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.ChangePasswordRequest;
import nomad.example.nomad_backend.dtos.UpdateUserRequest;
import nomad.example.nomad_backend.dtos.UserResponse;
public interface UserService {

    UserResponse updateProfile(Long id, UpdateUserRequest request);

    void deleteAccount(Long id);

    void changePassword(Long id, ChangePasswordRequest request);
    UserResponse getUser(Long id);
}

package br.com.valdircezar.userserviceapi.service;

import br.com.valdircezar.userserviceapi.entity.User;
import br.com.valdircezar.userserviceapi.mapper.UserMapper;
import br.com.valdircezar.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return userMapper.fromEntity(find(id));
    }

    public void save(CreateUserRequest createUserRequest) {
        verifyIfEmailAlreadyExists(createUserRequest.email(), null);
        userRepository.save(userMapper.fromRequest(createUserRequest));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream().map(userMapper::fromEntity)
                .toList();
    }

    public UserResponse update(final String id, final UpdateUserRequest updateUserRequest) {
        User entity = find(id);
        verifyIfEmailAlreadyExists(updateUserRequest.email(), id);
        return userMapper.fromEntity(userRepository.save(userMapper.update(updateUserRequest, entity)));
    }

    private User find(final String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
    }

    private void verifyIfEmailAlreadyExists(final String email, final String id) {
        userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                });
    }

}

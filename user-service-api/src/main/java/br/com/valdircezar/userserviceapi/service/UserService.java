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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;

    public UserResponse findById(final String id) {
        return mapper.fromEntity(find(id));
    }

    public void save(CreateUserRequest request) {
        verifyIfEmailAlreadyExists(request.email(), null);
        repository.save(
                mapper.fromRequest(request)
                        .withPassword(encoder.encode(request.password()))
        );
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream().map(mapper::fromEntity)
                .toList();
    }

    public UserResponse update(final String id, final UpdateUserRequest request) {
        User entity = find(id);
        verifyIfEmailAlreadyExists(request.email(), id);
        return mapper.fromEntity(
                repository.save(
                        mapper.update(request, entity)
                                .withPassword(request.password() != null ? encoder.encode(request.password()) : entity.getPassword())
                )
        );
    }

    private User find(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
    }

    private void verifyIfEmailAlreadyExists(final String email, final String id) {
        repository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                });
    }

}

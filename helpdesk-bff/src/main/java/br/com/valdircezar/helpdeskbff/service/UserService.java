package br.com.valdircezar.helpdeskbff.service;

import br.com.valdircezar.helpdeskbff.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFeignClient userFeignClient;


    @Cacheable(value = "users", key = "#id")
    public UserResponse findById(final String id) {
        return userFeignClient.findById(id).getBody();
    }

    @CacheEvict(value = "users", allEntries = true)
    public void save(CreateUserRequest request) {
        userFeignClient.save(request);
    }

    @Cacheable(value = "users")
    public List<UserResponse> findAll() {
        return userFeignClient.findAll().getBody();
    }

    @CacheEvict(value = "users", allEntries = true)
    public UserResponse update(final String id, final UpdateUserRequest request) {
        return userFeignClient.update(id, request).getBody();
    }

}

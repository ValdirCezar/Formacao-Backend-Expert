package br.com.valdircezar.helpdeskbff.client;

import jakarta.validation.Valid;
import models.requests.AuthenticateRequest;
import models.requests.CreateUserRequest;
import models.requests.RefreshTokenRequest;
import models.requests.UpdateUserRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;
import models.responses.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "auth-service-api",
        path = "/api/auth"
)
public interface AuthFeignClient {

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody final AuthenticateRequest request) throws Exception;

    @PostMapping("/refresh-token")
    ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody final RefreshTokenRequest request);
}

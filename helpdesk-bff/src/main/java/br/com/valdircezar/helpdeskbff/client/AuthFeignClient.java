package br.com.valdircezar.helpdeskbff.client;

import jakarta.validation.Valid;
import models.requests.AuthenticateRequest;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "localhost:8765/auth-service-api",
        path = "/api/auth"
)
public interface AuthFeignClient {

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody final AuthenticateRequest request) throws Exception;

    @PostMapping("/refresh-token")
    ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody final RefreshTokenRequest request);
}

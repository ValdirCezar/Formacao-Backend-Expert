package br.com.valdircezar.helpdeskbff.service;

import br.com.valdircezar.helpdeskbff.client.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import models.requests.AuthenticateRequest;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient feignClient;

    public AuthenticationResponse authenticate(AuthenticateRequest request) throws Exception {
        return feignClient.authenticate(request).getBody();
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        return feignClient.refreshToken(request).getBody();
    }
}

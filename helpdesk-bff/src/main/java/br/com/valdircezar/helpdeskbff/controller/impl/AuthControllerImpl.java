package br.com.valdircezar.helpdeskbff.controller.impl;

import br.com.valdircezar.helpdeskbff.controller.AuthController;
import br.com.valdircezar.helpdeskbff.service.AuthService;
import lombok.RequiredArgsConstructor;
import models.requests.AuthenticateRequest;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                authService.authenticate(request)
        );
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok().body(
                authService.refreshToken(request)
        );
    }
}

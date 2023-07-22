package models.responses;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token,
        String type
) {
}

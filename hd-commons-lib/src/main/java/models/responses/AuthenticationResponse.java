package models.responses;

import lombok.Builder;
import lombok.With;

@With

@Builder
public record AuthenticationResponse(
        String token,
        String refreshToken,
        String type
) {
}

package models.requests;

import lombok.With;
import models.enums.ProfileEnum;

import java.util.Set;

@With
public record CreateUserRequest(
    String name,
    String email,
    String password,
    Set<ProfileEnum> profiles
) { }

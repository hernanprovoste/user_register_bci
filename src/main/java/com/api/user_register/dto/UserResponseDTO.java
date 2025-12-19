package com.api.user_register.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponseDTO {

    private UUID id;
    private LocalDateTime created;
    private LocalDateTime modified;

    @JsonProperty("last_login")
    private LocalDateTime lastLogin;
    private String token;

    @JsonProperty("isactive")
    private Boolean is_active;

}

package com.api.user_register.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponseDTO {

    @JsonProperty("mensaje")
    private String message;
}

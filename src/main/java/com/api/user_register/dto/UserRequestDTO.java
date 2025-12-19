package com.api.user_register.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;

    @Data
    public static class PhoneDTO {
        private String number;
        private String citycode;
        private String contrycode;
    }

}

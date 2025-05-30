package com.naturalia.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String documentType;
    private String documentNumber;
    private String phoneNumber;
}

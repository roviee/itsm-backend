package com.janr.itsm.auth.dto;

import com.janr.itsm.auth.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserDto {
    private String fullName;

    private String email;

    private String password;

    private Role role;
}

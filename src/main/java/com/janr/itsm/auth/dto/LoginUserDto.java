package com.janr.itsm.auth.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    private String email;

    private String password;
}

package com.janr.itsm.auth.dto;

import com.janr.itsm.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String fullname;

    private String email;

    private Role role;
}

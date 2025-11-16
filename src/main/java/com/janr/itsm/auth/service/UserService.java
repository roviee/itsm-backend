package com.janr.itsm.auth.service;

import com.janr.itsm.auth.dto.UserDto;
import com.janr.itsm.auth.model.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();
    List<UserDto> getAllUsersStaff();
}

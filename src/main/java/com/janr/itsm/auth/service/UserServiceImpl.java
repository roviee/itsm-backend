package com.janr.itsm.auth.service;

import com.janr.itsm.auth.dto.UserDto;
import com.janr.itsm.auth.enums.Role;
import com.janr.itsm.auth.model.User;
import com.janr.itsm.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<User> allUsers() {

        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public List<UserDto> getAllUsersStaff() {
        return toUserDTOList(userRepository.findByRole(Role.SUPPORT_STAFF)) ;
    }

    private List<UserDto> toUserDTOList(List<User> users) {
        return users.stream().map(this::mapToUserDto).toList();
    }


    private UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullname(user.getFullname());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }


}

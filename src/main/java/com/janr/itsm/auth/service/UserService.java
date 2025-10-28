package com.janr.itsm.auth.service;

import com.janr.itsm.auth.model.User;
import com.janr.itsm.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> allUsers() {

        return new ArrayList<>(userRepository.findAll());
    }

}

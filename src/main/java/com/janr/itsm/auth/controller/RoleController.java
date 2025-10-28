package com.janr.itsm.auth.controller;

import com.janr.itsm.auth.model.User;
import com.janr.itsm.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final UserService userService;

    @GetMapping("/user/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_STAFF', 'ADMIN')")
    public String userDashboard() {
        return "Welcome, User!";
    }

    @GetMapping("/support/dashboard")
    @PreAuthorize("hasAnyRole('SUPPORT_STAFF', 'ADMIN')")
    public String supportDashboard() {
        return "Welcome, Support Staff!";
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome, Admin!";
    }
    @GetMapping("/admin/dashboard/getUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}

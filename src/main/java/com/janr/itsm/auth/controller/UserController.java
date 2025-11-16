package com.janr.itsm.auth.controller;

import com.janr.itsm.auth.service.UserService;
import com.janr.itsm.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users/staff")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<ApiResponse> getAllUsersStaff(){
        return okResponse(userService.getAllUsersStaff());
    }

    private ResponseEntity<ApiResponse> okResponse (Object data) {
        return ResponseEntity.ok(new ApiResponse("success", data));
    }
}

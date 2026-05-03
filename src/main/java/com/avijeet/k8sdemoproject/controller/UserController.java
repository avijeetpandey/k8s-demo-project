package com.avijeet.k8sdemoproject.controller;

import com.avijeet.k8sdemoproject.dtos.UserRequestDto;
import com.avijeet.k8sdemoproject.dtos.UserResponseDto;
import com.avijeet.k8sdemoproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.addUser(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userResponseDtos = userService.getAllUsers();
        return ResponseEntity.ok(userResponseDtos);
    }
}

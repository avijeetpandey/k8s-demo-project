package com.avijeet.k8sdemoproject.dtos;

public record UserResponseDto(
        String name,
        String age,
        Long id
) {}

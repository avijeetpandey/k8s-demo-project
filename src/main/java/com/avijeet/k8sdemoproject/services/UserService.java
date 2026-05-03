package com.avijeet.k8sdemoproject.services;

import com.avijeet.k8sdemoproject.dtos.UserRequestDto;
import com.avijeet.k8sdemoproject.dtos.UserResponseDto;
import com.avijeet.k8sdemoproject.entities.User;
import com.avijeet.k8sdemoproject.exceptions.UserNotFoundException;
import com.avijeet.k8sdemoproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        User user = toEntity(userRequestDto);
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return toDto(user);
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = null;

        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        user = optionalUser.get();

        user.setName(userRequestDto.name());
        user.setAge(userRequestDto.age());
        User updatedUser = userRepository.save(user);

        return toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toDto).toList();
    }

    private User toEntity(UserRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.name())
                .age(userRequestDto.age())
                .build();
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getName(),
                String.valueOf(user.getAge()),
                user.getId()
        );
    }
}

package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.UserRegisterDTO;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.Role;
import com.bianca.backend.models.User;
import com.bianca.backend.repositories.RoleRepository;
import com.bianca.backend.repositories.UserRepository;
import com.bianca.backend.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldEncodePassword_andAssignDefaultRole() {

        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("newuser");
        registerDTO.setEmail("new@example.com");
        registerDTO.setPassword("plainPassword");

        Role userRole = new Role(AppRole.ROLE_USER);


        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword123");
        when(roleRepository.findByRoleName(AppRole.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        userService.createUser(registerDTO);


        org.mockito.Mockito.verify(passwordEncoder).encode("plainPassword");

        org.mockito.ArgumentCaptor<User> userCaptor = org.mockito.ArgumentCaptor.forClass(User.class);
        org.mockito.Mockito.verify(userRepository).save(userCaptor.capture());
        assertEquals("encodedPassword123", userCaptor.getValue().getPassword());
    }

    @Test
    void createUser_shouldThrowException_whenDefaultRoleNotFound() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setPassword("pass");

        when(roleRepository.findByRoleName(AppRole.ROLE_USER)).thenReturn(Optional.empty());

        assertThrows(APIException.class, () -> {
            userService.createUser(registerDTO);
        });
    }

    @Test
    void updateUser_shouldThrowException_whenPasswordsDoNotMatch() {
        User existingUser = new User();
        existingUser.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserRegisterDTO updateDTO = new UserRegisterDTO();
        updateDTO.setPassword("newPassword");
        updateDTO.setConfirmPassword("differentPassword");

        assertThrows(APIException.class, () -> {
            userService.updateUser(1L, updateDTO);
        });
    }

}
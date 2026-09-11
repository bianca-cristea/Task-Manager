package com.bianca.backend.services;

import com.bianca.backend.dtos.UserDTO;

import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUsers();
    public UserDTO getUserById(Long id);
    public UserDTO createUser(UserDTO userDTO);
    public UserDTO updateUser(Long id, UserDTO userDTO);
    public UserDTO deleteUser(UserDTO userDTO);
}

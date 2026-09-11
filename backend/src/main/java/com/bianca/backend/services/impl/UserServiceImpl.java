package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.UserDTO;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.exception.ResourceNotFoundException;
import com.bianca.backend.models.User;
import com.bianca.backend.repositories.UserRepository;
import com.bianca.backend.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> usersFromDb = userRepository.findAll();

        if(usersFromDb.isEmpty())throw new APIException("No users in the database.");

        return usersFromDb.stream().map(user -> modelMapper.map(user,UserDTO.class)).toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User userFromDb = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","userId",id));
        return modelMapper.map(userFromDb,UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setUsername(userDTO.getUsername());
        userRepository.save(newUser);

        return modelMapper.map(newUser,UserDTO.class);

    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User userToUpdate = userRepository.findById(userDTO.getUserId()).orElse(null);
        if(userToUpdate == null)throw new APIException("User not found.");
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setUsername(userDTO.getUsername());
        userToUpdate.setPassword(userDTO.getUsername());

        userRepository.save(userToUpdate);
        return modelMapper.map(userToUpdate,UserDTO.class);
    }

    @Override
    public UserDTO deleteUser(UserDTO userDTO) {
        User userToDelete = userRepository.findById(userDTO.getUserId()).orElse(null);
        if(userToDelete == null)throw new APIException("User not found.");
        userRepository.delete(userToDelete);
        return modelMapper.map(userToDelete,UserDTO.class);
    }
}

package com.bianca.backend.services.impl;


import com.bianca.backend.dtos.UserDTO;
import com.bianca.backend.dtos.UserRegisterDTO;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.exception.ResourceNotFoundException;
import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.Role;
import com.bianca.backend.models.User;
import com.bianca.backend.repositories.RoleRepository;
import com.bianca.backend.repositories.UserRepository;
import com.bianca.backend.services.UserService;
import com.bianca.backend.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthUtil authUtil;


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
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        User newUser = new User();
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setUsername(userRegisterDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(() -> new APIException("Role USER is not found"));
        newUser.setRoles(List.of(userRole));
        userRepository.save(newUser);

        return modelMapper.map(newUser,UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long id, UserRegisterDTO userRegisterDTO) {
        User updatedUser = userRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("User","userId",id));

        updatedUser.setEmail(userRegisterDTO.getEmail());
        updatedUser.setUsername(userRegisterDTO.getUsername());

        if(userRegisterDTO.getPassword() != null && !userRegisterDTO.getPassword().isBlank()){
            if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
                throw new APIException("Password and confirm password do not match.");
            }
            updatedUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        }
         boolean isAdmin = authUtil.isAdmin();
        if(isAdmin){
            updatedUser.setRoles(userRegisterDTO.getRoles().stream().map(role -> modelMapper.map(role,Role.class)).toList());
        }

        userRepository.save(updatedUser);

        return modelMapper.map(updatedUser,UserDTO.class);
    }



    @Override
    public UserDTO deleteUser(Long id) {
        User userToDelete = userRepository.findById(id).orElse(null);
        if(userToDelete == null)throw new APIException("User not found.");
        userRepository.delete(userToDelete);
        return modelMapper.map(userToDelete,UserDTO.class);
    }
}

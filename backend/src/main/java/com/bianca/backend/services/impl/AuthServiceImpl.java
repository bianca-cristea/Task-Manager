package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.AuthenticationResult;
import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.Role;
import com.bianca.backend.models.User;
import com.bianca.backend.repositories.RoleRepository;
import com.bianca.backend.repositories.UserRepository;
import com.bianca.backend.security.jwt.JwtUtils;
import com.bianca.backend.security.request.LoginRequest;
import com.bianca.backend.security.request.SignupRequest;
import com.bianca.backend.security.response.MessageResponse;
import com.bianca.backend.security.response.UserInfoResponse;
import com.bianca.backend.security.services.UserDetailsImpl;
import com.bianca.backend.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public AuthenticationResult login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles, userDetails.getEmail(), jwtCookie.toString());

        return new AuthenticationResult(response, jwtCookie);

    }


    @Override
    public ResponseEntity<MessageResponse> register(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: username already exists."));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already exists."));
        }

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> rolesStr = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (rolesStr == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found"));
            roles.add(userRole);
        } else {
            rolesStr.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "user":
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(userRole);
                        break;
                    default:
                        Role defaultRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(defaultRole);
                }
            });
        }
        user.setRoles((List<Role>) roles);
        userRepository.save(user);
        return ResponseEntity.ok().body(new MessageResponse("User registered successfully."));
    }


    @Override
    public UserInfoResponse getUserDetails(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl userDetails)) {
            throw new RuntimeException("Invalid principal type: " + principal);
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );
    }


    @Override
    public ResponseCookie logout() {
        return jwtUtils.getCleanJwtCookie();
    }

}

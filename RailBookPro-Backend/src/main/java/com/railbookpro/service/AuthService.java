package com.railbookpro.service;

import com.railbookpro.domain.entity.Role;
import com.railbookpro.domain.entity.User;
import com.railbookpro.domain.enums.RoleType;
import com.railbookpro.dto.auth.AuthResponse;
import com.railbookpro.dto.auth.LoginRequest;
import com.railbookpro.dto.auth.RegisterRequest;
import com.railbookpro.exception.DuplicateResourceException;
import com.railbookpro.repository.RoleRepository;
import com.railbookpro.repository.UserRepository;
import com.railbookpro.security.CustomUserDetails;
import com.railbookpro.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditService auditService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        Role passengerRole = roleRepository.findByName(RoleType.PASSENGER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.PASSENGER).build()));

        Set<Role> roles = new HashSet<>();
        roles.add(passengerRole);

        User user = User.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .roles(roles)
                .build();

        userRepository.save(user);
        auditService.log(user.getUsername(), "REGISTER", "New passenger registered");

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        auditService.log(user.getUsername(), "LOGIN", "User logged in");
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("userId", user.getId());

        String token = jwtService.generateToken(user.getUsername(), claims);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}

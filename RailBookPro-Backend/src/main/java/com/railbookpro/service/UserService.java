package com.railbookpro.service;

import com.railbookpro.domain.entity.Role;
import com.railbookpro.domain.entity.User;
import com.railbookpro.domain.enums.RoleType;
import com.railbookpro.dto.user.ChangePasswordRequest;
import com.railbookpro.dto.user.UserRequest;
import com.railbookpro.dto.user.UserResponse;
import com.railbookpro.exception.BadRequestException;
import com.railbookpro.exception.DuplicateResourceException;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.UserMapper;
import com.railbookpro.repository.RoleRepository;
import com.railbookpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> search(String query) {
        return userRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query)
                .stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createByAdmin(UserRequest request) {
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }
        String username = request.getEmail().split("@")[0];
        if (userRepository.existsByUsername(username)) {
            username = username + System.nanoTime() % 1000;
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required when creating a user");
        }

        Set<Role> roles = new HashSet<>();
        RoleType roleType = "ADMIN".equalsIgnoreCase(request.getRole()) ? RoleType.ADMIN : RoleType.PASSENGER;
        roles.add(resolveRole(roleType));

        User user = User.builder()
                .username(username)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(request.getActive() == null || request.getActive())
                .roles(roles)
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = findUser(id);

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        if (request.getRole() != null) {
            Set<Role> roles = new HashSet<>();
            RoleType roleType = "ADMIN".equalsIgnoreCase(request.getRole()) ? RoleType.ADMIN : RoleType.PASSENGER;
            roles.add(resolveRole(roleType));
            user.setRoles(roles);
        }

        UserResponse response = userMapper.toResponse(userRepository.save(user));
        notificationService.create(user, "Profile Updated", "Your profile details were updated successfully.");
        return response;
    }

    @Transactional
    public void delete(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = findUser(userId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        notificationService.create(user, "Password Changed", "Your password was changed successfully.");
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private Role resolveRole(RoleType type) {
        return roleRepository.findByName(type)
                .orElseGet(() -> roleRepository.save(Role.builder().name(type).build()));
    }
}

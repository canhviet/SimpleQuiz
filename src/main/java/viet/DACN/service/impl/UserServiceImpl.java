package viet.DACN.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import viet.DACN.dto.request.UserRequest;
import viet.DACN.dto.response.PageResponse;
import viet.DACN.dto.response.ResponseData;
import viet.DACN.dto.response.UserResponse;
import viet.DACN.exception.InvalidDataException;
import viet.DACN.model.Role;
import viet.DACN.model.User;
import viet.DACN.repo.RoleRepository;
import viet.DACN.repo.UserRepository;
import viet.DACN.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public ResponseData<?> all_users() {
        List<User> users = userRepository.findAll();
        List<UserResponse> list = users.stream().map(user -> UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .first_name(user.getFirst_name())
        .last_name(user.getLast_name())
        .build()).toList();

        return new ResponseData<>(HttpStatus.OK.value(), "get all users", list);
    }

    @Override
    public PageResponse<?> getAllUser(int pageNo, int pageSize) {
        Page<User> page = userRepository.findAll(PageRequest.of(pageNo, pageSize));

        List<UserResponse> list = page.stream().map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .first_name(user.getFirst_name())
                        .last_name(user.getLast_name())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(list)
                .build();
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new InvalidDataException("username not found"));
    }

    @Override
    public Long saveUser(UserRequest request) {
        User user = User.builder()
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        Set<Role> roles = new HashSet<>();
        String defaultRoleName = "ROLE_USER"; 
        Role role = roleRepository.findByName(defaultRoleName)
                .orElseThrow(() -> new InvalidDataException("Vai trò " + defaultRoleName + " không tồn tại"));
        roles.add(role);
        
        user.setRoles(roles);
        
        userRepository.save(user);
        log.info("User has added successfully, userId={}", user.getId());

        return user.getId();
    }

    @Override
    public void updateUser(Long userId, UserRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public UserResponse getUser(Long userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public void deleteUser(Long userId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

}
package viet.DACN.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import viet.DACN.dto.request.UserRequest;
import viet.DACN.dto.response.PageResponse;
import viet.DACN.dto.response.ResponseData;
import viet.DACN.dto.response.UserResponse;
import viet.DACN.model.User;

public interface UserService {
    ResponseData<?> all_users();
    PageResponse<?> getAllUser(int pageNo, int pageSize);
    User getByUsername(String username);
    Long saveUser(UserRequest request);
    void updateUser(Long userId, UserRequest request);
    UserResponse getUser(Long userId);
    void deleteUser(Long userId);
    UserDetailsService userDetailsService();
}

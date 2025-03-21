package viet.DACN.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import viet.DACN.dto.request.UserRequest;
import viet.DACN.dto.response.PageResponse;
import viet.DACN.dto.response.ResponseData;
import viet.DACN.dto.response.ResponseError;
import viet.DACN.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    private ResponseData<?> allUsers() {
        try {
            return userService.all_users();
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "list users fail");
        }
    }

    @GetMapping("/list")
    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                         @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        try {
            PageResponse<?> users = userService.getAllUser(pageNo, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "users", users);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping(value = "/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequest request) {
        log.info("Request add user, {} {}", request.getFirst_name(), request.getLast_name());
        Long userId = userService.saveUser(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "user.add.success", userId);
    }


}

package viet.DACN.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import viet.DACN.dto.request.SignInRequest;
import viet.DACN.dto.response.TokenResponse;
import viet.DACN.service.AuthenticationService;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponse> accessToken(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authenticationService.accessToken(request), OK);
    }

    // @PostMapping("/refresh-token")
    // public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
    //     return new ResponseEntity<>(authenticationService.refreshToken(request), OK);
    // }

    // @PostMapping("/remove-token")
    // public ResponseEntity<String> removeToken(HttpServletRequest request) {
    //     return new ResponseEntity<>(authenticationService.removeToken(request), OK);
    // }

    // @PostMapping("/forgot-password")
    // public ResponseEntity<String> forgotPassword(@RequestBody String email) throws MessagingException, UnsupportedEncodingException {
    //     return new ResponseEntity<>(authenticationService.forgotPassword(email), OK);
    // }

    // @PostMapping("/reset-password")
    // public ResponseEntity<String> resetPassword(@RequestBody String secretKey) {
    //     return new ResponseEntity<>(authenticationService.resetPassword(secretKey), OK);
    // }

    // @PostMapping("/change-password")
    // public ResponseEntity<String> changePassword(@RequestBody @Valid ResetPasswordDTO request) {
    //     return new ResponseEntity<>(authenticationService.changePassword(request), OK);
    // }

    // @PostMapping("/register")
    // public ResponseEntity<String> register(@RequestBody RegisterRequest res) {
    //     return new ResponseEntity<>(authenticationService.register(res), OK);
    // }

}

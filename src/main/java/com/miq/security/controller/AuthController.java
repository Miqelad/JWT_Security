/**
 * @author BomboRa
 */
package com.miq.security.controller;

import com.miq.security.dto.LoginCred;
import com.miq.security.dto.user.UserDetailsInfo;
import com.miq.security.entity.User;
import com.miq.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> registerHandler(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }


    @PostMapping("/login")
    public ResponseEntity<UserDetailsInfo> loginHandler(@RequestBody LoginCred body) {
        return ResponseEntity.ok(userService.login(body));
    }


}

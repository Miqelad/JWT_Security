/**
 * @author BomboRa
 */
package com.miq.security.controller;

import com.miq.security.entity.User;
import com.miq.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<User> getUserDetails() {
        return ResponseEntity.ok(userService.getUserInfo());
    }


}

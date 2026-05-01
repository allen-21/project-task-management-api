package com.muchanga.dev.projecttaskmanagementapi.controller.user;

import com.muchanga.dev.projecttaskmanagementapi.dto.user.UserResponseDTO;
import com.muchanga.dev.projecttaskmanagementapi.dto.user.UserUpdateDTO;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(userService.getProflile(currentUser));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal User currentUser,
                                                  @RequestBody @Valid UserUpdateDTO data){
        return ResponseEntity.ok(userService.upadate(currentUser, data));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User currentUser){
        userService.delete(currentUser);
        return ResponseEntity.noContent().build();
    }
}

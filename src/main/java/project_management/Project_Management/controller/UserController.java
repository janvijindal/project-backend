package project_management.Project_Management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_management.Project_Management.model.User;
import project_management.Project_Management.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get user by JWT
    @GetMapping("/profile")
    public ResponseEntity<User> getUserByJwt(@RequestHeader("Authorization") String token) {
        // Assuming the JWT comes in the form "Bearer <token>", extract the actual JWT
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        User user = userService.findUserByJwt(jwt);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id) throws Exception {
        Optional<User> user = Optional.ofNullable(userService.findUserById(id));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ObjectId id) throws Exception {
        Optional<User> user = Optional.ofNullable(userService.findUserById(id));
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

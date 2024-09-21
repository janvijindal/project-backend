package project_management.Project_Management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_management.Project_Management.model.PlayType;
import project_management.Project_Management.model.Subscription;
import project_management.Project_Management.model.User;
import project_management.Project_Management.service.SubscriptionService;
import project_management.Project_Management.service.UserService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Subscription> createSubscription(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            User user = userService.findUserByJwt(jwt);
            Subscription subscription = subscriptionService.create(user);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Retrieve the subscription of a user by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Subscription> getUserSubscription(@PathVariable ObjectId userId) {
        try {
            Subscription subscription = subscriptionService.getUserSubscription(userId);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update the subscription type for a user
    @PutMapping("/update")
    public ResponseEntity<Subscription> updateSubscription(
            @RequestHeader("Authorization") String token,
            @RequestParam PlayType playType) {
        try {
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            User user = userService.findUserByJwt(jwt);
            Subscription subscription = subscriptionService.updateSubscription(user.getId(), playType);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Check if a subscription is valid
    @GetMapping("/valid/{userId}")
    public ResponseEntity<Boolean> isValidSubscription(@PathVariable ObjectId userId) {
        try {
            Subscription subscription = subscriptionService.getUserSubscription(userId);
            boolean isValid = subscriptionService.isValid(subscription);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

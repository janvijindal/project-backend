package project_management.Project_Management.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project_management.Project_Management.config.JwtProvider;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.UserRepository;
import project_management.Project_Management.request.LoginReq;
import project_management.Project_Management.response.AuthResponse;
import project_management.Project_Management.service.SubscriptionService;

import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://project-management-5kajrpllz-janvis-projects-accd4478.vercel.app")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SubscriptionService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody User user) throws Exception {
         User checkUser=userRepository.findUserByEmail(user.getEmail());
         //check if already user exist or not
         if(checkUser != null){
              throw new Exception("Email already Exists,try using another email.");
         }
         //create a new user
         User new_user=new User();
         new_user.setEmail(user.getEmail());
         new_user.setUserName(user.getUserName());
         //save the password of user in encoded form in database
         new_user.setPassword(passwordEncoder.encode(user.getPassword()));

        Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //create jwt token string
         String jwt=JwtProvider.generateToken(authentication);
         //save the user in database
        User saved= userRepository.save(new_user);
        //create subscription for user
        service.create(saved);

         //create  auth response
        AuthResponse auth=new AuthResponse();
        auth.setJwt(jwt);
        auth.setMessage("Signup User Successfully");
        auth.setUser(new_user);

        //return the response of user
         return new ResponseEntity<>(auth, HttpStatus.CREATED);
    }

    @PostMapping("/signing")
    public ResponseEntity<?> signing(@RequestBody LoginReq req) {
        try {
            // Find user by email
            User user = userRepository.findUserByEmail(req.getEmail());

            // Check if the user exists
            if (user == null) {
                throw new Exception("No User found with this email.");
            }

            // Check the password (compare raw password from the request with the encoded password in the database)
            boolean checkPass = passwordEncoder.matches(req.getPassword(), user.getPassword());
            if (!checkPass) {
                throw new Exception("Password does not match, try again.");
            }

            // Authenticate the user
            Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create a JWT token for the authenticated user
            String jwt = JwtProvider.generateToken(authentication);

            // Create an AuthResponse
            AuthResponse auth = new AuthResponse();
            auth.setJwt(jwt);
            auth.setMessage("User signed in successfully.");
            auth.setUser(user);

            // Return the response
            return new ResponseEntity<>(auth, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions and return an error message with a BAD_REQUEST status
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            // Invalidate the current session, if applicable (for stateful scenarios)
            SecurityContextHolder.clearContext();

            // You can remove token from client-side storage (localStorage or cookies) using the client
            return new ResponseEntity<>("User logged out successfully.", HttpStatus.OK);

        } catch (Exception e) {
            // In case of error during logout
            return new ResponseEntity<>("Logout failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

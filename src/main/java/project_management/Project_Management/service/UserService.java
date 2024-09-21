package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.config.JwtProvider;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

     @Autowired
    private UserRepository userRepository;

     public User findUserByJwt(String jwt){
            String email= JwtProvider.getEmailFromToken(jwt);

           return findUserByEmail(email);
     }

     public User findUserByEmail(String email){
           return userRepository.findUserByEmail(email);
     }

     public User findUserById(ObjectId id) throws Exception {
          Optional<User> user=userRepository.findById(id);
          if(user.isEmpty()){
              throw new Exception("No User Found");
          }

          return user.get();
     }

     public User updateProjectSize(User user ,int size){
            user.setProjectSize(user.getProjectSize()+size);
           return userRepository.save(user);
     }

     public void deleteUser(ObjectId id){
            userRepository.deleteById(id);
     }
}

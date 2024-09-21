package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findUserByEmail(String email);
}

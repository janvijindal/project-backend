package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.Project;
import project_management.Project_Management.model.User;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

      List<Project> findByUsersContainingOrProjectOwner(User user, User projectOwner);
}

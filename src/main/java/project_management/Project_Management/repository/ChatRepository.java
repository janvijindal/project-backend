package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.Chat;

public interface ChatRepository extends MongoRepository<Chat, ObjectId> {
}

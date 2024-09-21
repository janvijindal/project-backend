package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.Message;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,ObjectId> {
     List<Message> findByChatIdOrderByCreatedAsc(ObjectId chatId);
}

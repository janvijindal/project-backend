package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.Invitation;

public interface InvitationRepository extends MongoRepository<Invitation, ObjectId> {
      Invitation findByToken(String token);
      Invitation findByEmail(String email);
}

package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.Comments;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comments, ObjectId> {

    List<Comments> findCommentsByIssueId(ObjectId issueId);
}

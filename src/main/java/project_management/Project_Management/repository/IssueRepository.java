package project_management.Project_Management.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import project_management.Project_Management.model.Issue;

import java.util.List;

public interface IssueRepository extends MongoRepository<Issue, ObjectId> {
     List<Issue> getIssueByProjectId(ObjectId projectId);
}

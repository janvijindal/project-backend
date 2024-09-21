package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.Issue;
import project_management.Project_Management.model.Project;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.IssueRepository;
import project_management.Project_Management.request.IssueRequest;

import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    public Issue getIssueById(ObjectId id) throws Exception {
         Optional<Issue> issue=issueRepository.findById(id);
         if(issue.isEmpty()){
             throw new Exception("No issue found");
         }

         return issue.get();
    }
    public List<Issue> getIssueByProjectId(ObjectId ProjectId) throws Exception {
        return issueRepository.getIssueByProjectId(ProjectId);

    }

    public Issue createIssue(IssueRequest issue, User user) throws Exception {
        Project project=projectService.getProjectById(issue.getProjectId());
        Issue i=new Issue();
        i.setPriority(issue.getPriority());
        i.setDescription(issue.getDescription());
        i.setStatus(issue.getStatus());
        i.setTitle(issue.getTitle());
        i.setProjectId(issue.getProjectId());
        i.setDueDate(issue.getDueDate());
        i.setProject(project);


        return issueRepository.save(i);
    }

    public void DeleteIssue(ObjectId issueId){
            issueRepository.deleteById(issueId);

    }

    public Issue addUserToIssue(ObjectId issueId,ObjectId userId) throws Exception {
           User user=userService.findUserById(userId);
          Issue issue=getIssueById(issueId);
          issue.setAssignIssueUser(user);
          return issueRepository.save(issue);
    }

    public Issue updateStatus(ObjectId issueId,String status) throws Exception {
          Issue issue=getIssueById(issueId);
          issue.setStatus(status);
        return issueRepository.save(issue);
    }
}

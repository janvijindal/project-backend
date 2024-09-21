package project_management.Project_Management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_management.Project_Management.model.Issue;
import project_management.Project_Management.request.IssueRequest;
import project_management.Project_Management.model.User;
import project_management.Project_Management.service.IssueService;
import project_management.Project_Management.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/issue")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    // Get issue by ID
    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable ObjectId id) {
        try {
            Issue issue = issueService.getIssueById(id);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get issues by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssuesByProjectId(@PathVariable ObjectId projectId) {
        try {
            List<Issue> issues = issueService.getIssueByProjectId(projectId);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create a new issue
    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody IssueRequest issueRequest, @RequestHeader("Authorization") String token) throws Exception {
        try {

            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            User user = userService.findUserByJwt(jwt);
            Issue issue = issueService.createIssue(issueRequest, user);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // Delete an issue
    @DeleteMapping("/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable ObjectId issueId) {
        issueService.DeleteIssue(issueId);
        return ResponseEntity.noContent().build();
    }

    // Update issue status
    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issue> updateStatus(@PathVariable ObjectId issueId, @PathVariable String status) {
        try {
            Issue issue = issueService.updateStatus(issueId, status);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Assign a user to an issue
    @PutMapping("/{issueId}/user/{userId}")
    public ResponseEntity<Issue> addUserToIssue(@PathVariable ObjectId issueId, @PathVariable ObjectId userId) {
        try {
            Issue issue = issueService.addUserToIssue(issueId, userId);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

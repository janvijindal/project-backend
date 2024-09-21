package project_management.Project_Management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_management.Project_Management.model.Chat;
import project_management.Project_Management.model.Invitation;
import project_management.Project_Management.model.Project;
import project_management.Project_Management.model.User;
import project_management.Project_Management.request.InviteReq;
import project_management.Project_Management.service.InvitationService;
import project_management.Project_Management.service.ProjectService;
import project_management.Project_Management.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvitationService invitationService;



    // Create a new project
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project, @RequestHeader("Authorization") String token) throws Exception {

        String jwt = token.startsWith("Bearer") ? token.substring(7) : token;
        User user = userService.findUserByJwt(jwt);
        Project createdProject = projectService.createProject(project, user);
        return ResponseEntity.ok(createdProject);
    }

    // Get projects by team (with optional filtering by category and tag)
    @GetMapping("/all-projects")
    public ResponseEntity<List<Project>> getProjectByUserOrCategoryOrTag(@RequestHeader("Authorization") String token,
                                                          @RequestParam(required = false) String category,
                                                          @RequestParam(required = false) String tag) throws Exception {
        String jwt = token.startsWith("Bearer") ? token.substring(7) : token;
        User user = userService.findUserByJwt(jwt);
        List<Project> projects = projectService.getProjectByTeam(user, category, tag);
        return ResponseEntity.ok(projects);
    }

    // Get a project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable ObjectId id) throws Exception {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // Update a project by ID
    @PostMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable ObjectId id, @RequestBody Project updateProject) throws Exception {
        Project updatedProject = projectService.updateProject(updateProject, id);
        return ResponseEntity.ok(updatedProject);
    }

    // Delete a project by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@RequestHeader("Authorization") String token,
                                              @PathVariable ObjectId id) throws Exception {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        User user = userService.findUserByJwt(jwt);
        projectService.deleteProject(id,user.getId());
        return new ResponseEntity<>("Project deleted Successfully", HttpStatus.OK);
    }

    // Add a user to the project
    @PostMapping("/invite")
    public ResponseEntity<Invitation> addUserToProject(@RequestBody InviteReq req,
                                                    @RequestHeader("Authorization") String token) throws Exception {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        User userSender = userService.findUserByJwt(jwt);
        User reciever=userService.findUserByEmail(req.getEmail());
       Invitation invitation= invitationService.sendInvitation(req.getEmail(),req.getProjectId());
        projectService.addUserToProject(invitation.getProjectId(),userSender.getId(),reciever.getId());
        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/accept_invitation")
    public ResponseEntity<Map<String, Object>> AcceptInvitation(@RequestParam String token,
                                                                @RequestHeader("Authorization") String jwt) throws Exception {
        String jwt_token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        User user = userService.findUserByJwt(jwt_token);
        Invitation invitation = invitationService.acceptInvitation(token, user.getId());

        // Example of constructing a URL to redirect to the project page
        String projectPageUrl = String.format("/projects/%s", invitation.getProjectId());

        // Fetch the new user details (or modify according to your needs)
        User newUser = userService.findUserById(user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Project invitation Accepted");
        response.put("redirectUrl", projectPageUrl);
        response.put("user", newUser); // Add new user details to response

        return ResponseEntity.ok(response);
    }




    // Remove a user from the project
    @PostMapping("/removeUser/{projectId}")
    public ResponseEntity<Project> removeUserFromProject(@RequestBody InviteReq req,
                                                         @RequestHeader("Authorization") String token) throws Exception {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        User user = userService.findUserByJwt(jwt);
        Project updatedProject = projectService.removeUserToProject(req.getProjectId(), user.getId());
        return ResponseEntity.ok(updatedProject);
    }

    // Get chat details by project ID
    @GetMapping("/{id}/chat")
    public ResponseEntity<Chat> getChatByProjectId(@PathVariable ObjectId id) throws Exception {
        Chat chat = projectService.getChatByProjectId(id);
        return ResponseEntity.ok(chat);
    }

    // Search for projects by keyword
    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam String keyword, @RequestHeader("Authorization") String token) throws Exception {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        User user = userService.findUserByJwt(jwt);
        List<Project> projects = projectService.searchProject(keyword, user);
        return ResponseEntity.ok(projects);
    }
}

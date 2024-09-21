package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.Invitation;
import project_management.Project_Management.model.Project;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.InvitationRepository;
import project_management.Project_Management.repository.ProjectRepository;


import java.util.UUID;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository projectRepository;

    public Invitation sendInvitation(String email, ObjectId projectId){
         String token= UUID.randomUUID().toString();
        Invitation invitation=new Invitation();
        invitation.setEmail(email);
        invitation.setToken(token);
        invitation.setProjectId(projectId);
        invitationRepository.save(invitation);

        String link="http://localhost:3000/accept_invitation?token="+token;
        emailService.sendEmailWithToken(email,link);

        return invitation;
    }

    public Invitation acceptInvitation(String token, ObjectId userId) throws Exception {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new Exception("Invalid token");
        }

        // Add additional logic here to accept the invitation
        Project project = projectService.getProjectById(invitation.getProjectId());
        if (project == null) {
            throw new Exception("Project not found");
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            throw new Exception("User not found");
        }

        // Optionally delete or update the invitation
        invitationRepository.delete(invitation);

        return invitation;
    }


    public void deleteInvitation(String token){
         Invitation invitation=invitationRepository.findByToken(token);
         invitationRepository.delete(invitation);
    }

    public String getTokenByUserMain(String email){
       Invitation invitation= invitationRepository.findByEmail(email);
       return invitation.getToken();
    }


}

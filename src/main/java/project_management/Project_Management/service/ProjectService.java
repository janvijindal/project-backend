package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.Chat;
import project_management.Project_Management.model.Project;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

     @Autowired
    private ProjectRepository projectRepository;

     @Autowired
     private UserService userService;

     @Autowired
     private ChatService chatService;

    public Project createProject(Project project, User user) {
        Project newProject = new Project();
        newProject.setProjectName(project.getProjectName());
        newProject.setCategory(project.getCategory());
        newProject.setDescription(project.getDescription());
        newProject.setTags(project.getTags());
        newProject.setProjectOwner(user);
        newProject.getUsers().add(user);

        // Initialize chat for the project
        Chat chat = new Chat();

        // Save the project with chat
        Project savedProject = projectRepository.save(newProject);
        Project res = projectRepository.save(newProject);

        // Set the project for the chat
        chat.setProject(savedProject);
        chat.getUsers().add(user);
        Chat c =chatService.createChat(chat);

        savedProject.setChat(c);
       return projectRepository.save(savedProject);

    }



    public List<Project> getProjectByTeam(User user,String category,String tag){
         List<Project> projectList = projectRepository.findByUsersContainingOrProjectOwner(user, user);
           if(category != null){
                projectList=projectList.stream().filter(p->p.getCategory().equals(category)).toList();
           }
         if(tag != null){
             projectList=projectList.stream().filter(p->p.getTags().contains(tag)).toList();
         }

         return projectList;
     }

     public Project getProjectById(ObjectId id) throws Exception {
          Optional<Project> project= projectRepository.findById(id);
          if(project.isEmpty()){
               throw new Exception("No project found");
          }

          return project.get();
     }

    public void deleteProject(ObjectId projectId, ObjectId userId) throws Exception {
        Project project = getProjectById(projectId);
        if (!project.getProjectOwner().getId().equals(userId)) {
            throw new Exception("Only the project owner can delete this project.");
        }
        projectRepository.deleteById(projectId);
    }
     public Project updateProject(Project updateProject,ObjectId id) throws Exception {
            Project p=getProjectById(id);
            p.setProjectName(updateProject.getProjectName());
            p.setTags(updateProject.getTags());
            p.setDescription(updateProject.getDescription());

             return projectRepository.save(p);
     }

     public void addUserToProject(ObjectId projectId, ObjectId userId,ObjectId receiverId) throws Exception {
               Project project=getProjectById(projectId);
               User user=userService.findUserById(userId);
               User reciever=userService.findUserById(receiverId);

               if(reciever == null || project.getUsers().contains(reciever)){
                   throw new Exception("User already Exists");
               }

               if(project.getChat()!= null) {
                   project.getChat().getUsers().add(reciever);
               }
               project.getUsers().add(reciever);

             projectRepository.save(project);


     }

    public Project removeUserToProject(ObjectId projectId, ObjectId userId) throws Exception {
        Project project=getProjectById(projectId);
        User user=userService.findUserById(userId) ;
        if(!project.getUsers().contains(user)){
            throw new Exception("User already don't Exists");
        }
        project.getChat().getUsers().remove(user);
        project.getUsers().remove(user);

        return projectRepository.save(project);
    }

    public Chat getChatByProjectId(ObjectId id) throws Exception {
        Project project = getProjectById(id);
        return project.getChat();
    }

    public List<Project> searchProject(String keyword, User user) {
        // Search for projects where the user is either a team member or the owner
        List<Project> userProjects = projectRepository.findByUsersContainingOrProjectOwner(user, user);

        // Additional search within these projects for the keyword in title or description
        return userProjects.stream()
                .filter(project -> project.getProjectName().toLowerCase().contains(keyword.toLowerCase()) ||
                        project.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }





}

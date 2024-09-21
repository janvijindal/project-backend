package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.Chat;
import project_management.Project_Management.model.Message;
import project_management.Project_Management.model.Project;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.ChatRepository;
import project_management.Project_Management.repository.MessageRepository;
import project_management.Project_Management.repository.ProjectRepository;
import project_management.Project_Management.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    public Message sendMessage(ObjectId sendId, ObjectId projectId, String content) throws Exception {
        // Fetch the project and user from the repositories
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<User> optionalUser = userRepository.findById(sendId);

        if (optionalProject.isEmpty()) {
            throw new Exception("Project not found");
        }

        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }

        // Get the project and user from the Optionals
        Project project = optionalProject.get();
        User user = optionalUser.get();

        // Get the chat associated with the project
        Chat chat = project.getChat();
        if (chat == null) {
            // If chat doesn't exist, create a new one
            chat = new Chat();
            chat.setProject(project);
            chat.getUsers().add(user); // Add the user to the chat
            chatService.createChat(chat); // Save chat to the DB
            project.setChat(chat);
            projectRepository.save(project); // Save the project with the new chat
        }

        // Create and save the new message
        Message message = new Message();
        message.setMessage(content);
        message.setChat(chat);
        message.setSender(user);
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        chat.getMessageList().add(savedMessage); // Add the message to the chat's message list

        // Update the chat with the new message list
        chatRepository.save(chat); // Make sure to update the chat in the DB

        return savedMessage;
    }



    public List<Message> getMessageByProjectId(ObjectId projectId) throws Exception {
         Chat chat=projectService.getProjectById(projectId).getChat();

        return messageRepository.findByChatIdOrderByCreatedAsc(chat.getId());


    }
}

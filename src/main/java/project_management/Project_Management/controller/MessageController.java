package project_management.Project_Management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_management.Project_Management.model.Message;
import project_management.Project_Management.request.MessageReq;
import project_management.Project_Management.service.MessageService;
import project_management.Project_Management.service.ProjectService;
import project_management.Project_Management.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestBody MessageReq req) {
        try {
            Message message = messageService.sendMessage(req.getSendId(), req.getProjectId(),req.getMessage());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Retrieve all messages for a specific project by its ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByProjectId(@PathVariable ObjectId projectId) {
        try {
            List<Message> messages = messageService.getMessageByProjectId(projectId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

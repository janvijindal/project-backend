package project_management.Project_Management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.Chat;
import project_management.Project_Management.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

     public Chat createChat(Chat chat){
           return chatRepository.save(chat);
     }
}

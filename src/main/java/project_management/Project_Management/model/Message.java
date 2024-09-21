package project_management.Project_Management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import project_management.Project_Management.domain.ObjectIdSerializer;

import java.time.LocalDateTime;

@Document("message")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String message;
    private LocalDateTime created;
    @DBRef
    @JsonIgnore
    private Chat chat;
    @DBRef
    private User sender;

    public LocalDateTime getCreatedAt() {
        return created;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.created =created;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public ObjectId getId() {
        return id;
    }


    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

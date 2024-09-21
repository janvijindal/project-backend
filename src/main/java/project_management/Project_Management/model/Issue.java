package project_management.Project_Management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import project_management.Project_Management.domain.ObjectIdSerializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Issues")
@Data
public class Issue {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    @DBRef
    private User assignIssueUser;
    @DBRef
    private Project project;
    private String title;
    private String description;
    private ObjectId projectId;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags=new ArrayList<>();
    private List<Comments> comments=new ArrayList<>();

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public User getAssignIssueUser() {
        return assignIssueUser;
    }

    public void setAssignIssueUser(User assignIssueUser) {
        this.assignIssueUser = assignIssueUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

package project_management.Project_Management.request;

import org.bson.types.ObjectId;

public class InviteReq {

   private ObjectId projectId;
   private String email;

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package project_management.Project_Management.request;

import org.bson.types.ObjectId;

public class MessageReq {

     private ObjectId sendId;
     private ObjectId projectId;
     private String message;

    public ObjectId getSendId() {
        return sendId;
    }

    public void setSendId(ObjectId sendId) {
        this.sendId = sendId;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

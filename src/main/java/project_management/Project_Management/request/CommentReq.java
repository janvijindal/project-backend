package project_management.Project_Management.request;

import org.bson.types.ObjectId;

public class CommentReq {

   private ObjectId issueId;

    public ObjectId getIssueId() {
        return issueId;
    }

    public void setIssueId(ObjectId issueId) {
        this.issueId = issueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

}

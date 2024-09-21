package project_management.Project_Management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_management.Project_Management.model.Comments;
import project_management.Project_Management.model.User;
import project_management.Project_Management.request.CommentReq;
import project_management.Project_Management.service.CommentsService;
import project_management.Project_Management.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private UserService userService;

    // Create a new comment
    @PostMapping()
    public ResponseEntity<Comments> createComment(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentReq req) {
        try {
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            User user = userService.findUserByJwt(jwt);
            Comments createdComment = commentsService.createComment(req.getIssueId(),user.getId(),req.getContent());
            return ResponseEntity.ok(createdComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Delete a comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable ObjectId commentId,
            @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            User user = userService.findUserByJwt(jwt);
            commentsService.deleteComment(commentId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get comments by issue ID
    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<Comments>> getCommentsByIssueId(@PathVariable ObjectId issueId) {
        List<Comments> comments = commentsService.getCommentsByIssueId(issueId);
        return ResponseEntity.ok(comments);
    }


}

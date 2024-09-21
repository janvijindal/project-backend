package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.Comments;
import project_management.Project_Management.model.Issue;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.CommentRepository;
import project_management.Project_Management.repository.IssueRepository;
import project_management.Project_Management.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

     public Comments createComment(ObjectId issueId, ObjectId userId,String Comment) throws Exception {
         Optional<Issue> issue=issueRepository.findById(issueId);
         Optional<User> user=userRepository.findById(userId);
         if(issue.isEmpty()){
              throw  new Exception(("No issue found"));
         }
         if(user.isEmpty()){
             throw  new Exception(("No user found"));
         }

         Comments comments=new Comments();
         comments.setIssue(issue.get());
         comments.setUser(user.get());
         comments.setComment(Comment);
         comments.setCommentCreated(LocalDateTime.now());

         Comments saved= commentRepository.save(comments);
         issue.get().getComments().add(saved);

         return saved;
     }

     public void deleteComment(ObjectId comment,ObjectId userId) throws Exception {
         Optional<Comments> comments=commentRepository.findById(comment);
         Optional<User> user=userRepository.findById(userId);
         if(comments.isEmpty()){
             throw  new Exception(("No comment found"));
         }
         if(user.isEmpty()){
             throw  new Exception(("No user found"));
         }

         if(comments.get().getUser().equals(user.get())){
                 commentRepository.delete(comments.get());
         }

         else{
             throw new Exception("You are not authorize to delete the comment");
         }

     }

     public List<Comments> getCommentsByIssueId(ObjectId issueId){
          return commentRepository.findCommentsByIssueId(issueId);
     }


}

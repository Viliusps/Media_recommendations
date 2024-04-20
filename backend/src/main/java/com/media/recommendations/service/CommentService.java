package com.media.recommendations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.media.recommendations.model.Comment;
import com.media.recommendations.model.User;
import com.media.recommendations.model.requests.CommentRequest;
import com.media.recommendations.repository.CommentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public List<Comment> getAllComments() {
        return commentRepository.findAllByOrderByIdAsc();
    }

     public Comment getCommentById(long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            return optionalComment.get();
        }
        return null;
    }

    public Comment createComment(CommentRequest comment) {
        User user = userService.userByUsername(comment.getUsername());
        Comment newComment = new Comment();
        newComment.setCommentText(comment.getCommentText());
        newComment.setMovie(comment.getMovie());
        newComment.setGame(comment.getGame());
        newComment.setSong(comment.getSong());
        newComment.setRating(comment.getRating());
        newComment.setUser(user);
        return commentRepository.save(newComment);
    }

    public boolean existsComment(long id) {
        return commentRepository.existsById(id);
    }

    public Comment updateComment(Long id, Comment comment) {
        Comment commentFromDb = commentRepository.findById(id).get();
        commentFromDb.setCommentText(comment.getCommentText());
        commentFromDb.setMovie(comment.getMovie());
        commentFromDb.setSong(comment.getSong());
        commentFromDb.setGame(comment.getGame());
        commentFromDb.setRating(comment.getRating());
        return commentRepository.save(commentFromDb);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    
}

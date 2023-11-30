package com.media.recommendations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.media.recommendations.model.Comment;
import com.media.recommendations.repository.CommentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

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

    public Comment createComment(Comment comment) {
        Comment newComment = new Comment();
        newComment.setCommentText(comment.getCommentText());
        newComment.setMovie(comment.getMovie());
        newComment.setRating(comment.getRating());
        return commentRepository.save(newComment);
    }

    public boolean existsComment(long id) {
        return commentRepository.existsById(id);
    }

    public Comment updateComment(Long id, Comment comment) {
        Comment commentFromDb = commentRepository.findById(id).get();
        commentFromDb.setCommentText(comment.getCommentText());
        commentFromDb.setMovie(comment.getMovie());
        commentFromDb.setRating(comment.getRating());
        return commentRepository.save(commentFromDb);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    
}

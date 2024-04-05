package com.media.recommendations.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.media.recommendations.model.Comment;
import com.media.recommendations.repository.CommentRepository;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllComments() {
        when(commentRepository.findAllByOrderByIdAsc()).thenReturn(Arrays.asList(new Comment(), new Comment()));
        List<Comment> result = commentService.getAllComments();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCommentByIdWhenCommentExists() {
        Comment comment = new Comment();
        comment.setId(1L);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        Comment result = commentService.getCommentById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetCommentByIdWhenCommentDoesNotExist() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Comment result = commentService.getCommentById(1L);
        assertNull(result);
    }

    @Test
    public void testCreateComment() {
        Comment comment = new Comment();
        comment.setCommentText("Nice movie!");
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment createdComment = commentService.createComment(comment);
        assertNotNull(createdComment);
        assertEquals("Nice movie!", createdComment.getCommentText());
    }

    @Test
    public void testExistsComment() {
        when(commentRepository.existsById(1L)).thenReturn(true);
        boolean exists = commentService.existsComment(1L);
        assertTrue(exists);
    }

    @Test
    public void testUpdateComment() {
        Comment existingComment = new Comment();
        existingComment.setId(1L);
        existingComment.setCommentText("Old comment");

        Comment newComment = new Comment();
        newComment.setCommentText("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);

        Comment updatedComment = commentService.updateComment(1L, newComment);
        assertNotNull(updatedComment);
        assertEquals("Updated comment", updatedComment.getCommentText());
    }

    @Test
    public void testDeleteComment() {
        doNothing().when(commentRepository).deleteById(anyLong());
        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).deleteById(anyLong());
    }
}

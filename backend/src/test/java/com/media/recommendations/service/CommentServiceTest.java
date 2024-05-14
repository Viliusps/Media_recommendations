package com.media.recommendations.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.media.recommendations.model.User;
import com.media.recommendations.model.requests.CommentRequest;
import com.media.recommendations.repository.CommentRepository;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserService userService;

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
        User mockUser = new User();
        mockUser.setUsername("testUser");

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setUsername("testUser");
        commentRequest.setCommentText("This is a comment");
        commentRequest.setMovie(1l);
        commentRequest.setRating(5);

        Comment expectedComment = new Comment();
        expectedComment.setCommentText("This is a comment");
        expectedComment.setMovie(1l);
        expectedComment.setRating(5);
        expectedComment.setUser(mockUser);

        when(userService.userByUsername("testUser")).thenReturn(mockUser);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comment actualComment = commentService.createComment(commentRequest);

        // Assert
        assertEquals(expectedComment.getCommentText(), actualComment.getCommentText());
        assertEquals(expectedComment.getMovie(), actualComment.getMovie());
        assertEquals(expectedComment.getGame(), actualComment.getGame());
        assertEquals(expectedComment.getSong(), actualComment.getSong());
        assertEquals(expectedComment.getRating(), actualComment.getRating());
        assertEquals(expectedComment.getUser(), actualComment.getUser());

        verify(userService).userByUsername("testUser");
        verify(commentRepository).save(any(Comment.class));
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
package com.thinktrail.blog.service;

import com.thinktrail.blog.model.Comment;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.repository.CommentRepository;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.service.impl.CommentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private Comment testComment;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        commentService = new CommentServiceImpl(commentRepository, userRepository);

        User testUser = User.builder().id(1L).firstName("Test").lastName("User").email("test@example.com").password("1234").build();
        Post testPost = Post.builder().id(1L).title("Test Post").content("Post content").build();
        testComment = Comment.builder()
                .id(1L)
                .content("Test Comment")
                .user(testUser)
                .post(testPost)
                .build();
    }

    @Test
    void createCommentTest() {
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment saved = commentService.createComment(testComment);

        assertNotNull(saved);
        assertEquals("Test Comment", saved.getContent());
        verify(commentRepository, times(1)).save(testComment);
    }

    @Test
    void findCommentByIdTest() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        Comment found = commentService.findCommentById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(commentRepository).findById(1L);
    }

    @Test
    void findCommentById_nonExistingId() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.findCommentById(99L);
        });

        assertEquals("Like not found with ID: 99", exception.getMessage());
    }

    @Test
    void findCommentsByUserIdTest() {
        List<Comment> comments = List.of(testComment);
        when(commentRepository.findByUserId(1L)).thenReturn(comments);

        List<Comment> result = commentService.findCommentsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(testComment, result.getFirst());
    }

    @Test
    void findCommentsByPostIdTest() {
        List<Comment> comments = List.of(testComment);
        when(commentRepository.findByPostId(1L)).thenReturn(comments);

        List<Comment> result = commentService.findCommentsByPostId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Comment", result.getFirst().getContent());
    }

    @Test
    void findByCreatedAtTest() {
        LocalDateTime time = testComment.getCreatedAt();
        when(commentRepository.findByCreatedAt(time)).thenReturn(List.of(testComment));

        List<Comment> comments = commentService.findByCreatedAt(time);

        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getContent());
    }

    @Test
    void deleteComment() {
        commentService.deleteComment(testComment);

        verify(commentRepository, times(1)).delete(testComment);
    }

    @Test
    void deleteCommentById() {
        commentService.deleteCommentById(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }
}

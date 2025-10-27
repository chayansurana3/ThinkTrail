//package com.thinktrail.blog.service;
//
//import com.thinktrail.blog.model.Like;
//import com.thinktrail.blog.model.Post;
//import com.thinktrail.blog.model.User;
//import com.thinktrail.blog.repository.LikeRepository;
//import com.thinktrail.blog.service.impl.LikeServiceImpl;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class LikeServiceTest {
//
//    @Mock
//    private LikeRepository likeRepository;
//
//    @InjectMocks
//    private LikeServiceImpl likeService;
//
//    @Mock
//    private Like like;
//
//    @Mock
//    private User user;
//
//    @Mock
//    private Post post;
//
//    private AutoCloseable closeable;
//
//    @BeforeEach
//    void setUp() {
//        closeable = MockitoAnnotations.openMocks(this);
//
//        user = User.builder().id(1L).build();
//        post = Post.builder().id(100L).build();
//        like = Like.builder().id(10L).user(user).post(post).build();
//    }
//
//    @Test
//    void testCreateLike_success() {
//        when(likeRepository.save(any(Like.class))).thenReturn(like);
//
//        Like savedLike = likeService.createLike(like);
//
//        assertNotNull(savedLike);
//        assertEquals(10L, savedLike.getId());
//        verify(likeRepository).save(like);
//    }
//
//    @Test
//    void testGetLikeById_found() {
//        when(likeRepository.findById(10L)).thenReturn(Optional.of(like));
//
//        Like found = likeService.getLikeById(10L);
//
//        assertNotNull(found);
//        assertEquals(10L, found.getId());
//        verify(likeRepository).findById(10L);
//    }
//
//    @Test
//    void testGetLikeById_notFound() {
//        when(likeRepository.findById(10L)).thenReturn(Optional.empty());
//
//        Exception ex = assertThrows(RuntimeException.class, () -> likeService.getLikeById(10L));
//        assertEquals("Like not found with ID: 10", ex.getMessage());
//    }
//
//    @Test
//    void testGetLikesByUser() {
//        List<Like> likes = List.of(like);
//        when(likeRepository.findByUserId(user.getId())).thenReturn(likes);
//
//        List<Like> result = likeService.getLikesByUser(user);
//
//        assertEquals(1, result.size());
//        verify(likeRepository).findByUserId(user.getId());
//    }
//
//    @Test
//    void testGetLikesByPost() {
//        List<Like> likes = List.of(like);
//        when(likeRepository.findByPostId(post.getId())).thenReturn(likes);
//
//        List<Like> result = likeService.getLikesByPost(post);
//
//        assertEquals(1, result.size());
//        verify(likeRepository).findByPostId(post.getId());
//    }
//
//    @Test
//    void testExistsByUserIdAndPostId() {
//        when(likeRepository.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(like);
//
//        Like found = likeService.existsByUserIdAndPostId(user.getId(), post.getId());
//
//        assertNotNull(found);
//        verify(likeRepository).findByUserIdAndPostId(user.getId(), post.getId());
//    }
//
//    @Test
//    void testCountByPostId() {
//        when(likeRepository.countByPostId(post.getId())).thenReturn(5L);
//
//        long count = likeService.countByPostId(post.getId());
//
//        assertEquals(5L, count);
//        verify(likeRepository).countByPostId(post.getId());
//    }
//
//    @Test
//    void testDeleteLike() {
//        likeService.deleteLike(like);
//
//        verify(likeRepository).delete(like);
//    }
//
//    @Test
//    void testDeleteLikeById() {
//        likeService.deleteLikeById(10L);
//
//        verify(likeRepository).deleteById(10L);
//    }
//
//    @Test
//    void testDeleteByUserIdAndPostId() {
//        likeService.deleteByUserIdAndPostId(user.getId(), post.getId());
//
//        verify(likeRepository).deleteByUserIdAndPostId(user.getId(), post.getId());
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        closeable.close();
//    }
//}

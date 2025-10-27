package com.thinktrail.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktrail.blog.dto.UserDTO;
import com.thinktrail.blog.model.Role;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserDTO sampleUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setRole("USER");
        return dto;
    }

    private User sampleUser() {
        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .role(Role.USER)
                .build();
    }


    @Test
    @WithMockUser
    void testCreateUser_success() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(sampleUser());

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));

        Mockito.verify(userService).createUser(any(User.class));
    }

    @Test
    @WithMockUser
    void testCreateUser_failure_invalidData() throws Exception {
        Mockito.when(userService.createUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetUserById_success() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(sampleUser());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser
    void testGetUserById_notFound() throws Exception {
        Mockito.when(userService.getUserById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetAllUsers_success() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(sampleUser()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser
    void testGetAllPostsByUser_success() throws Exception {
        Mockito.when(userService.getAllPostsByUser(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/1/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void testGetAllLikesByUser_success() throws Exception {
        Mockito.when(userService.getAllLikesByUser(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/1/likes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetAllCommentsByUser_success() throws Exception {
        Mockito.when(userService.getAllCommentsByUser(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDeleteUserById_success() throws Exception {
        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUserById(1L);
    }

    @Test
    @WithMockUser
    void testDeleteUser_success() throws Exception {
        mockMvc.perform(delete("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserDTO())))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser(any(User.class));
    }

    @Test
    @WithMockUser
    void testCreateUser_failure() throws Exception {
        Mockito.when(userService.createUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("Invalid user data"));

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user data"));
    }

    @Test
    @WithMockUser
    void testGetAllPostsByUser_failure() throws Exception {
        Mockito.when(userService.getAllPostsByUser(1L))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/api/users/1/posts"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    @WithMockUser
    void testGetAllLikesByUser_empty() throws Exception {
        Mockito.when(userService.getAllLikesByUser(1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    void testDeleteUserById_failure() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Cannot delete non-existing user"))
                .when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot delete non-existing user"));
    }

    @Test
    @WithMockUser
    void testDeleteUser_missingBody() throws Exception {
        mockMvc.perform(delete("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNoContent()); // May fail at service layer
    }
}
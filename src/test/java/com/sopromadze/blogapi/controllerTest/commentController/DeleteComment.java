package com.sopromadze.blogapi.controllerTest.commentController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class DeleteComment {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentServiceImpl commentService;

    /*Test:Check if delete Comment works
    * Input:Long PostId,Long commentId, UserPrincipal current
    * Output:ResponseEntity<?> (ApiResponse.success, status.No_Content)
    * */
    @Test
    @DisplayName("Delete comment success for authorized user")
    @WithUserDetails("user")
    public void deleteComment() throws Exception {
        ApiResponse apiResponse = new ApiResponse(true, "You successfully deleted comment");

        when(commentService.deleteComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(apiResponse);
        mockMvc.perform(delete("/api/posts/{postId}/comments/{id}", 1L, 1L)
                        .contentType("application/json"))
                 .andExpect(status().isNoContent());
    }

    /*
    * Test:Check if delete comment throws unauthorized exception
    * Input:Long PostId,Long commentId, UserPrincipal current
    * Output:ResponseEntity<?> (ApiResponse.error, status.UNAUTHORIZED)
    * */
    @Test
    @DisplayName("Delete comment unauthorized for unauthorized user")
    public void unauthorizedDeleteComment() throws Exception {
        ApiResponse apiResponse = new ApiResponse(false, "You are not authorized to delete comment");

        when(commentService.deleteComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(apiResponse);
        mockMvc.perform(delete("/api/posts/{postId}/comments/{id}", 1L, 1L)
                        .contentType("application/json"))
                 .andExpect(status().isUnauthorized());
    }

    /*
    * Test:Check if delete comment throws not found exception
    * Input:Long PostId,Long commentId, UserPrincipal current
    * Output:ResponseEntity<?> (ApiResponse.error, status.NOT_FOUND)
    * */
    @Test
    @DisplayName("Delete comment not found")
    @WithUserDetails("user")
    public void notFoundDeleteComment() throws Exception {
        ApiResponse apiResponse = new ApiResponse(false, "Not found");

        when(commentService.deleteComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(apiResponse);
        mockMvc.perform(delete("/api/posts/{postId}/comments/{id}", 1L, 1L)
                        .contentType("application/json"))
                 .andExpect(status().isNotFound());
    }
}

package com.sopromadze.blogapi.controllerTest.photoController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.service.impl.PhotoServiceImpl;
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
public class DeletePhoto {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PhotoServiceImpl photoService;

    /*Test:Check if delete Photo works
    * Input:Long PhotoId, UserPrincipal current
    * Output:ResponseEntity<?> (ApiResponse.success,status.No_Content)
    * */
    @Test
    @DisplayName("Delete photo success")
    @WithUserDetails("user")
    public void deletePhoto() throws Exception {
    ApiResponse apiResponse = new ApiResponse(true, "Photo deleted successfully");

    when(photoService.deletePhoto(Mockito.anyLong(),Mockito.any())).thenReturn(apiResponse);
        mockMvc.perform(delete("/api/photos/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    /*Test:Check if delete photo throws unauthorized exception
    * Input:Long PhotoId, null
    * Output:ResponseEntity<?> (ApiResponse.failure,status.unauthorized)
    * */
    @Test
    @DisplayName("Delete photo unauthorized")
    public void deletePhotoUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/photos/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    /*Test:Check if delete photo throws not found exception
    * Input:Long PhotoId, UserPrincipal current
    * Output:ResponseEntity<?> (ApiResponse.failure,status.not_found)
    * */
    @Test
    @DisplayName("Delete photo not found")
    @WithUserDetails("user")
    public void deletePhotoBadRequest() throws Exception {
        ApiResponse apiResponse = new ApiResponse(false, "Photo not found");

        when(photoService.deletePhoto(Mockito.anyLong(),Mockito.any())).thenReturn(apiResponse);
        mockMvc.perform(delete("/api/photos/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

}

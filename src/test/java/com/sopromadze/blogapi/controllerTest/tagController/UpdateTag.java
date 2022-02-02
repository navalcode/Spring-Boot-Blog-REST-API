package com.sopromadze.blogapi.controllerTest.tagController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class UpdateTag {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagServiceImpl tagService;

    private Tag tag;

    @BeforeEach
    void init(){
        tag = new Tag();
        tag.setName("tag");
        tag.setId(1L);
        tag.setCreatedBy(1L);
    }

    /*Test: Check if update category returns 200 when updated
    * Input:Long TagId, RequestBody newTag, UserPrincipal Current
    * Output: ResponseEntity<newTag>
    * */
    @Test
    @DisplayName("Tag controller update tag success")
    @WithUserDetails("user")
    void updateTag_success() throws Exception{
        String tagJson = objectMapper.writeValueAsString(tag);

        when(tagService.updateTag(Mockito.anyLong(),Mockito.any(),Mockito.any())).thenReturn(tag);

        MvcResult mvcResult = mockMvc.perform(put("/api/tags/{id}",1L)
                .contentType("application/json")
                .content(tagJson))
                .andExpect(status().isOk()).andReturn();

        String actualInt = mvcResult.getResponse().getContentAsString();

        Tag actual= objectMapper.readValue(actualInt,Tag.class);
        Tag expected = objectMapper.readValue(tagJson,Tag.class);

        assertTrue(actual.getName().equals(expected.getName()));
        assertTrue(actual.getId().equals(expected.getId()));
    }

    /*Test:Check if update Tag throws 401 when user is not logged in
    * Input:Long PostId, new Post
    * Output:ResponseEntity<Post> with status 401 unauthorized
    * */
    @Test
    @DisplayName("Controller update tag unauthorized")
    void updateTag_unauthorized () throws Exception{
        String tagJson = objectMapper.writeValueAsString(tag);

        when(tagService.updateTag(Mockito.anyLong(),Mockito.any(),Mockito.any())).thenReturn(tag);

        MvcResult mvcResult = mockMvc.perform(put("/api/tags/{id}",1L)
                        .contentType("application/json")
                        .content(tagJson))
                .andExpect(status().isUnauthorized()).andReturn();

    }

    /*Test:Check if update Tag throws 404 when tag is not found
    * Input:PostId, PostRequest, UserPrincipal
    * Output:ResponseEntity<Post> with status 404 not found
    * */
    @Test
    @DisplayName("Controller update tag not found")
    @WithUserDetails("user")
    void updateTag_notFound () throws Exception{
        String tagJson = objectMapper.writeValueAsString(tag);

        when(tagService.updateTag(Mockito.anyLong(),Mockito.any(),Mockito.any())).thenThrow(new ResourceNotFoundException("Tag","id",1L));

        MvcResult mvcResult = mockMvc.perform(put("/api/tags/{id}",1L)
                        .contentType("application/json")
                        .content(tagJson))
                .andExpect(status().isNotFound()).andReturn();
    }

    /*Test:Check if update tag throws 400 when the request is invalid
    * Input:TagId, wrong Tag
    * Output:ResponseEntity<Tag> with status 400 bad request
    * */
    @Test
    @DisplayName("Controller update tag bad request")
    @WithUserDetails("user")
    void updateTag_badRequest() throws Exception{
        String tagJson = "Not a real tag json";

        MvcResult mvcResult = mockMvc.perform(put("/api/tags/{id}",1L)
                        .contentType("application/json")
                        .content(tagJson))
                .andExpect(status().isBadRequest()).andReturn();
    }
}

package com.sopromadze.blogapi.serviceTest.post;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import com.sopromadze.blogapi.utils.AppUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetPostsByTag {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    /*
    * Test: Check if getPostsByTag returns all posts with the given tag
    * Input:Long TagId, int page, int size
    * Output:PagedResponse<Post> containing all posts with the given tag
    */
    @Test
    @DisplayName("Get posts by tag works")
    void getPostsByTag_succes(){
        //Create Tags
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("tag1");
        tag1.setPosts(new ArrayList<>());

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("tag2");
        tag2.setPosts(new ArrayList<>());

        //Create Posts
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("title1");
        post1.setBody("body1");
        post1.setTags(new ArrayList<>());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("title2");
        post2.setBody("body2");
        post2.setTags(new ArrayList<>());

        Post post3 = new Post();
        post3.setId(3L);
        post3.setTitle("title3");
        post3.setBody("body3");
        post3.setTags(new ArrayList<>());
        //Set posts to tag
        tag1.getPosts().add(post1);
        tag1.getPosts().add(post2);
        tag2.getPosts().add(post3);

        //Set tags to post
        post1.getTags().add(tag1);
        post2.getTags().add(tag1);
        post3.getTags().add(tag2);

        //Create pageable
        Pageable pageable = PageRequest.of(1, 2, Sort.Direction.DESC, CREATED_AT);

        //Create Page of posts
        Page<Post> page = new PageImpl<>(List.of(post1,post2), pageable, 3);

        //Mock TagRepository
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));
        //Mock PostRepository
        when(postRepository.findByTagsIn(Collections.singletonList(tag1), pageable)).thenReturn(page);

        assertTrue(postService.getPostsByTag(1L,1,2).getContent().contains(post1));
        assertTrue(postService.getPostsByTag(1L,1,2).getContent().contains(post2));
        assertTrue(postService.getPostsByTag(1L,1,2).getContent().size() == 2);
        assertFalse(postService.getPostsByTag(1L,1,2).getContent().contains(post3));

    }

    /*
     * Test: Check if getPostsByTag throws ResourceNotFoundException if tag is not found
     * Input: Wrong Long TagId, int page, int size
     * Output:ResourceNotFoundException
     */
    @Test
    @DisplayName("Get posts by tag throws ResourceNotFoundException")
    void getPostsByTag_fails(){
        Tag tag = new Tag();
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByTag(1L,1,2));
    }


}

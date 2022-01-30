package com.sopromadze.blogapi.serviceTest.comment;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
import com.sopromadze.blogapi.utils.AppUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllComments {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    Post post;
    Comment comment, comment2;
    User user;


    @BeforeEach
    void init() {

        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create user
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("user");
        user.setRoles(rolesUser);

        post = new Post();
        post.setId(1L);
        post.setUser(user);

        comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setId(1L);
        comment.setName("comment1");

        comment2 = new Comment();
        comment2.setPost(post);
        comment2.setUser(user);
        comment2.setId(1L);
        comment2.setName("comment2");


    }

    @Test
    void getAllComments_success() {
        AppUtils.validatePageNumberAndSize(1, 1);

        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.DESC, "createdAt");

        List<Comment> commentsList = new ArrayList<Comment>();
        commentsList.add(comment);
        Page<Comment> comments = new PageImpl(commentsList, pageable, commentsList.size());
        when(commentRepository.findByPostId(1L, pageable)).thenReturn(comments);


        assertTrue(commentService.getAllComments(1L, 1, 1).getContent().contains(comment));


    }

    @Test
    void getAllComments_success_fail() {
        assertThrows(NullPointerException.class ,
                ()->commentService.getAllComments(0L, 1, 1));
    }
    
}

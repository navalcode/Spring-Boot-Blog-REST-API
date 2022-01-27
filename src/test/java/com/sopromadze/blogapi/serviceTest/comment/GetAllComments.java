package com.sopromadze.blogapi.serviceTest.comment;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllComments {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    CommentServiceImpl commentService;

    PagedResponse<Comment> resultadoEsperado;
    Post post;

    /*
    @BeforeEach
    void init() {
        Pageable pageable = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");
        Category category = new Category();
        category.setName("categoria1");
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        Page<Category> categories = new PageImpl<>(Arrays.asList(category));
        Post


        when(commentRepository.findByPostId(1L, pageable)).;
        List<Category> content = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(content);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

    }
    
     */


}

package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findByUserId(Long UserId, Pageable pageable);

	Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

	Page<Post> findByTagsIn(List<Tag> tags, Pageable pageable);

	Long countByUserId(Long userId);
}

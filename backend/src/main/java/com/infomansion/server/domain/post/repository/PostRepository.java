package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
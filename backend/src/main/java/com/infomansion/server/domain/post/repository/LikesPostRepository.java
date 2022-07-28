package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.post.domain.LikesPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesPostRepository extends JpaRepository<LikesPost, Long> {
}

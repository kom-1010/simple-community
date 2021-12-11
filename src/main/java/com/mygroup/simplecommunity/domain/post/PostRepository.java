package com.mygroup.simplecommunity.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.id DESC")
    List<Post> findAllDesc();

    @Query("SELECT p FROM Post p WHERE p.title LIKE %?1% ORDER BY p.id DESC")
    List<Post> findAllByTitleLikeDesc(String title);

    @Query("SELECT p FROM Post p WHERE p.author.name LIKE %?1% ORDER BY p.id DESC")
    List<Post> findAllByAuthorLikeDesc(String name);
}

package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

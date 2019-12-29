package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PostRepository {
  private final EntityManager em;

  public void save(Post post) {
    if (post.getId() == null) {
      em.persist(post);
    } else {
      em.merge(post);
    }
  }
}

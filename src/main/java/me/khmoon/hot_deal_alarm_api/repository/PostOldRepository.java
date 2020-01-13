package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static me.khmoon.hot_deal_alarm_api.helper.JpaResultHelper.getSingleResultOrNull;

@Repository
@RequiredArgsConstructor
public class PostOldRepository {
  private final EntityManager em;

  public Long save(Post post) {
    if (post.getId() == null) {
      em.persist(post);
    } else {
      em.merge(post);
    }
    return post.getId();
  }

  public List<Long> saveAll(List<Post> posts) {
    List<Long> result = new ArrayList<>();

    for (Post post : posts) {
      result.add(save(post));
    }
    return result;
  }

  public Post findOne(Long id) {
    return em.find(Post.class, id);
  }

  public List<Post> findAll() {
    return em.createQuery("select p from Post p", Post.class).getResultList();
  }

  public Post findOneByOriginId(Long postOriginId, Long boardId, Long siteId) {
    Query query = em.createQuery("select  p from Post p where p.postOriginId=:postOriginId and p.board.id=:boardId and p.board.site.id=:siteId", Post.class)
            .setParameter("postOriginId", postOriginId)
            .setParameter("boardId", boardId)
            .setParameter("siteId", siteId);
    return (Post) getSingleResultOrNull(query);
  }
}

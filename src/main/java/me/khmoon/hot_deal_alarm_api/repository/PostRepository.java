package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Query("select  p from Post p where p.postOriginId=:postOriginId and p.board.id=:boardId and p.board.site.id=:siteId")
  Optional<Post> findOneByOriginId(@Param("postOriginId") Long postOriginId, @Param("boardId") Long boardId, @Param("siteId") Long siteId);
  // TODO findOneByOriginId 단일 쿼리가 아닌 in query를 이용하여 한번에 받아 성능 개선해보도록 하자
}

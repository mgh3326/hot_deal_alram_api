package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Query("select  p from Post p where p.postOriginId=:postOriginId and p.board.id=:boardId")
  Optional<Post> findOneByOriginId(@Param("postOriginId") Long postOriginId, @Param("boardId") Long boardId);

  // findOneByOriginId 단일 쿼리가 아닌 in query를 이용하여 한번에 받아 성능 개선해보도록 하자
  // TODO 이게 맞는 쿼리일까 튜닝이 필요할듯 하다.
  @Query("select p from Post p where p.board.id = :boardId and p.postOriginId in :postOriginIds")
  List<Post> findInOriginIds(@Param("postOriginIds") List<Long> postOriginIds, @Param("boardId") Long boardId); // 적용하자!

  @Query("select p from Post p where p.board.id = :boardId")
  Slice<Post> findPostsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

  @Query("select p from Post p")
// 지워도 될거 같은데
  Slice<Post> findPosts(Pageable pageable);
}

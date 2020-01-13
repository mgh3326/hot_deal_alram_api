package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
  @Query("select b from Board b where b.boardName=:boardName and b.site.id=:siteId")
  Optional<Board> findOneByBoardName(@Param("boardName") BoardName boardName, @Param("siteId") Long siteId);

  @Query("select b from Board b" +
          " join fetch b.site s order by s.id")
  List<Board> findAllWithSite();// TODO order by 성능과 site에서 board를 fetch join하는 것 중에서 성능 비교를 해볼 필요가 있을것 같다.
}

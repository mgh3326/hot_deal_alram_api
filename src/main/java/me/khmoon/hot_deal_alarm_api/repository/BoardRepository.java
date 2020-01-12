package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
  private final EntityManager em;

  public void save(Board board) {
    em.persist(board);
  }

  public List<Board> findAll() {
    return em.createQuery("select b from Board b", Board.class).getResultList();
  }

  public Board findOne(Long id) {
    return em.find(Board.class, id);
  }

  public Board findOneByBoardName(BoardName boardName, Long siteId) {
    return em.createQuery("select b from Board b where b.boardName=:boardName and b.site.id=:siteId", Board.class)
            .setParameter("boardName", boardName)
            .setParameter("siteId", siteId)
            .getSingleResult();
  }

  public List<Board> findAllWithSite() {
    return em.createQuery("select b from Board b" +
            " join fetch b.site s order by s.id", Board.class).getResultList();// TODO order by 성능과 site에서 board를 fetch join하는 것 중에서 성능 비교를 해볼 필요가 있을것 같다.
  }
}

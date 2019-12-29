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

  public Board findOneByBoardName(BoardName boardName) {
    return em.createQuery("select b from Board b where b.boardName=:boardName", Board.class).setParameter("boardName", boardName).getSingleResult();
  }
}

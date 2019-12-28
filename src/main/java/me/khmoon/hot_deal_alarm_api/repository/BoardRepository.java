package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
  private final EntityManager em;

  public Long save(Board board) {
    em.persist(board);
    return board.getId();
  }

  public List<Board> findAll() {
    return em.createQuery("select board from Board board", Board.class).getResultList();
  }

  public Board findOne(Long id) {
    return em.find(Board.class, id);
  }
}

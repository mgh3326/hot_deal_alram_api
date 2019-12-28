package me.khmoon.hot_deal_alram_api.repository;

import me.khmoon.hot_deal_alram_api.domain.board.Board;
import me.khmoon.hot_deal_alram_api.domain.board.BoardName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardRepositoryTest {
  @Autowired
  BoardRepository boardRepository;

  @Test
  public void 게시판_저장_불러오기() {
    //given
    Board board = new Board();
    BoardName boardName = BoardName.PPOMPPU_DOMESTIC;
    board.setBoardName(boardName);
    boardRepository.save(board);
    //when

    List<Board> boardList = boardRepository.findAll();

    //then
    Board board1 = boardList.get(0);
    assertThat(board1.getBoardName()).isEqualTo(boardName);
  }
}
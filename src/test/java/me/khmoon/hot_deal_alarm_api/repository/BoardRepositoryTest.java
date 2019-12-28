package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardRepositoryTest {
  @Autowired
  BoardRepository boardRepository;
  @Autowired
  EntityManager em;

  @Test
  @Transactional
  public void 게시판_저장_불러오기() {
    //given
    BoardName boardName = BoardName.PPOMPPU_DOMESTIC;
    String boardBaseListUrl = "http://m.ppomppu.co.kr/new/bbs_list.php?id=ppomppu";
    String boardBaseViewUrl = "http://m.ppomppu.co.kr/new/bbs_view.php?id=ppomppu";
    boardRepository.save(Board.builder().boardName(boardName).boardBaseListUrl(boardBaseListUrl).boardBaseViewUrl(boardBaseViewUrl).build());
    //when
    List<Board> boardList = boardRepository.findAll();

    //then
    Board board1 = boardList.get(0);
    assertThat(board1.getBoardName()).isEqualTo(boardName);
    assertThat(board1.getBoardBaseListUrl()).isEqualTo(boardBaseListUrl);
    assertThat(board1.getBoardBaseViewUrl()).isEqualTo(boardBaseViewUrl);
  }

}
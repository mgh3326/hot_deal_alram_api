package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test") // Like this
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardRepositoryTest {
  @Autowired
  BoardRepository boardRepository;
  @Autowired
  EntityManager em;

  @Value("${ppomppu.baseUrl.list}")
  private String boardBaseListUrl;

  @Value("${ppomppu.baseUrl.view}")
  private String boardBaseViewUrl;

  @Test
  @Transactional
  public void 게시판_저장_불러오기() {
    //given
    BoardName boardName = BoardName.PPOMPPU_DOMESTIC;
    boardRepository.save(Board.builder().boardName(boardName).boardBaseListUrl(boardBaseListUrl).boardBaseViewUrl(boardBaseViewUrl).build());
    //when
    List<Board> boardList = boardRepository.findAll();

    //then
    Board board1 = boardList.get(0);
    assertEquals(board1.getBoardName(), boardName, "equal test board name");
    assertEquals(board1.getBoardBaseListUrl(), boardBaseListUrl, "equal test board base list url");
    assertEquals(board1.getBoardBaseViewUrl(), boardBaseViewUrl, "equal test board base view url");
  }

}
package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Like this
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardServiceTest {
  @Autowired
  BoardService boardService;
  @Autowired
  BoardRepository boardRepository;
  @Value("${ppomppu.baseUrl.list}")
  private String boardBaseListUrl;

  @Value("${ppomppu.baseUrl.view}")
  private String boardBaseViewUrl;

  @Test
  void add() {
    BoardName boardName = BoardName.PPOMPPU_DOMESTIC;
    Board board = Board.builder().boardName(boardName).boardBaseListUrl(boardBaseListUrl).boardBaseViewUrl(boardBaseViewUrl).build();
    boardService.add(board);
    Board board1 = boardRepository.findOne(board.getId());
    assertEquals(board1.getBoardName(), boardName, "equal test board name");
    assertEquals(board1.getBoardBaseListUrl(), boardBaseListUrl, "equal test board base list url");
    assertEquals(board1.getBoardBaseViewUrl(), boardBaseViewUrl, "equal test board base view url");
  }
}
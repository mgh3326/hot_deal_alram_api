package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
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
class PageServiceTest {
  @Autowired
  BoardService boardService;
  @Autowired
  BoardRepository boardRepository;
  @Value("${ppomppu.domestic.baseUrl.list}")
  private String boardBaseListUrl;

  @Value("${ppomppu.domestic.baseUrl.view}")
  private String boardBaseViewUrl;
  @Autowired
  PageService pageService;

  @Test
  void savePage() {
    //board 저장
    BoardName boardName = BoardName.PPOMPPU_DOMESTIC;
    Board board = Board.builder().boardName(boardName).boardBaseListUrl(boardBaseListUrl).boardBaseViewUrl(boardBaseViewUrl).build();
    boardService.add(board);

    int pageNum = 1;
    Page page = Page.builder().pageNum(pageNum).build();
    pageService.savePage(boardName, page);
    assertEquals(page.getPageNum(), pageNum, "equal test page page_num");

  }
}
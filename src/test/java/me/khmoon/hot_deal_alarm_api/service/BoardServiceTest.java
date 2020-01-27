package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.common.BaseServiceTest;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardKoreanName;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BoardServiceTest extends BaseServiceTest {
  @Autowired
  private BoardService boardService;
  @Autowired
  private SiteService siteService;
  private String boardParam;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;
  private BoardKoreanName boardKoreanName = BoardKoreanName.국내;

  @PostConstruct
  public void init() {
    boardParam = applicationProperties.getPpomppu().getParam().getDomestic();
  }

  @BeforeEach
  void init2() {
    Site site = Site.builder().siteName(siteName).build();
    siteService.add(site);
  }

  @Test
  @DisplayName("게시판 저장 (id)")
  void add() {
    Site site = siteService.findOneBySiteName(siteName);
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteId(board, site.getId());
    Board board1 = boardService.findOne(board.getId());
    assertEquals(board1.getBoardName(), boardName, "equal test board name");
    assertEquals(board1.getBoardParam(), boardParam, "equal test board name");
    assertEquals(boardKoreanName, board1.getBoardKoreanName(), "equal test board name");
  }


  @Test
  @DisplayName("게시판 저장 (site name)")
  void add2() {
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteName(board, siteName);
    Board board1 = boardService.findOne(board.getId());
    assertEquals(board1.getBoardName(), boardName, "equal test board name");
    assertEquals(board1.getBoardParam(), boardParam, "equal test board name");
  }

}
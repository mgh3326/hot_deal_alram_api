package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test") // Like this
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class PageServiceTest {
  @Autowired
  BoardService boardService;
  @Autowired
  BoardRepository boardRepository;
  @Autowired
  PageService pageService;
  @Autowired
  SiteService siteService;
  @Value("${ppomppu.param.domestic}")
  private String boardParam;
  @Value("${ppomppu.url.list}")
  private String siteListUrl;
  @Value("${ppomppu.url.view}")
  private String siteViewUrl;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;
  private int pageNum = 1;

  @Test
  @DisplayName("페이지 저장 (Name)")
  void savePage() {
    // 사이트 저
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.add(site.getId(), board);

    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(60).build();
    pageService.savePage(boardName, page);
    assertEquals(page.getPageNum(), pageNum, "equal test page page_num");

  }

  @Test
  @DisplayName("페이지 저장 (id)")
  void savePage2() {
    // 사이트 저
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.add(site.getId(), board);

    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(60).build();
    pageService.savePage(board.getId(), page);
    assertEquals(page.getPageNum(), pageNum, "equal test page page_num");

  }
}
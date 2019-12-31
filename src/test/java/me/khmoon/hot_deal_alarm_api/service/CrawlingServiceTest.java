package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test") // Like this
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CrawlingServiceTest {
  @Autowired
  BoardService boardService;
  @Autowired
  BoardRepository boardRepository;
  @Autowired
  PageService pageService;
  @Autowired
  SiteService siteService;
  @Autowired
  CrawlingService crawlingService;
  @Value("${ppomppu.param.domestic}")
  private String boardParam;
  @Value("${ppomppu.url.list}")
  private String siteListUrl;
  @Value("${ppomppu.url.view}")
  private String siteViewUrl;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;

  @Test
  void parse() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.add(site.getId(), board);

    int pageNum = 1;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePage(board.getId(), page);

    crawlingService.Parse(page.getId());

  }
}
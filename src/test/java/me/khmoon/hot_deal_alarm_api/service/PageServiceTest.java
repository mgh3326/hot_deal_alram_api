package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")

@SpringBootTest
@Transactional
class PageServiceTest {
  @Autowired
  private BoardService boardService;
  @Autowired
  private PageService pageService;
  @Autowired
  private SiteService siteService;
  @Autowired
  private ApplicationProperties applicationProperties;
  @Autowired
  private EntityManager em;
  private String boardParam;
  private String siteListUrl;
  private String siteViewUrl;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;

  private SiteName siteNameDealbada = SiteName.DEALBADA;
  private String boardParamDealbada;
  private String siteListUrlDealbada;
  private String siteViewUrlDealbada;

  @PostConstruct
  public void init() {
    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    boardParam = applicationProperties.getPpomppu().getParam().getDomestic();

    siteListUrlDealbada = applicationProperties.getDealbada().getUrl().getList();
    siteViewUrlDealbada = applicationProperties.getDealbada().getUrl().getView();
    boardParamDealbada = applicationProperties.getDealbada().getParam().getDomestic();
  }

  @Test
  @DisplayName("페이지 저장")
  void savePage() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteId(board, site.getId());

    int pageNum = 1;
    int pageRefreshSecond = 1;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());
    assertEquals(pageNum, page.getPageNum(), "equal test page page_num");
    assertEquals(pageRefreshSecond, page.getPageRefreshSecond(), "equal test page page_num");
    LocalDateTime prePageRefreshingDateTime = page.getPageRefreshingDateTime();
    try {
      Thread.sleep(1); // 0.001초 지나고
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    page.updatePageRefreshingDateTime();
//    em.flush();

    Page one = pageService.findOne(page.getId());
    LocalDateTime pageRefreshingDateTime = one.getPageRefreshingDateTime();
    assertNotEquals(prePageRefreshingDateTime, pageRefreshingDateTime, "modified datetime");

    List<Page> pages = pageService.findAllBySiteId(site.getId());
    Long count = pageService.countBySiteId(site.getId());
    assertEquals(1, pages.size(), "findAllBySiteId page size");
    assertEquals(1, count, "findAllBySiteId page size");

    Page pageForRefreshing = pageService.findOneForRefreshing();
    Page pageForRefreshingBySiteId = pageService.findOneForRefreshingBySiteId(site.getId());
    assertNull(pageForRefreshing, "findAllBySiteId page size");
    assertNull(pageForRefreshingBySiteId, "findAllBySiteId page size");
    one = pageService.findOne(page.getId());
    one.updatePageRefreshingDateTime();

//    em.flush();
    try {
      Thread.sleep(1000); // 1초 지나고
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Page oneForRefreshing = pageService.findOneForRefreshing(); // 1초 된거 있나 확인하도록!
    Page oneForRefreshingBySiteId = pageService.findOneForRefreshingBySiteId(site.getId()); // 1초 된거 있나 확인하도록!
    assertNotNull(oneForRefreshing, "findAllBySiteId page size");
    assertNotNull(oneForRefreshingBySiteId, "findAllBySiteId page size");
  }

  @Test
  @DisplayName("페이지 저장")
  void savePageDealbada() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteNameDealbada).siteListUrl(siteListUrlDealbada).siteViewUrl(siteViewUrlDealbada).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParamDealbada).build();
    boardService.addWithSiteId(board, site.getId());

    int pageNum = 1;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());
    assertEquals(pageNum, page.getPageNum(), "equal test page page_num");
    assertEquals(pageRefreshSecond, page.getPageRefreshSecond(), "equal test page page_num");
  }
}
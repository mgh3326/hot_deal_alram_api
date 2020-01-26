package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.common.BaseServiceTest;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class PageServiceTest extends BaseServiceTest {

  @Autowired
  private BoardService boardService;
  @Autowired
  private PageService pageService;
  @Autowired
  private SiteService siteService;
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
    Page one2 = pageService.findOneWithBoardWithSite(page.getId());
    LocalDateTime pageRefreshingDateTime = one.getPageRefreshingDateTime();
    LocalDateTime pageRefreshingDateTime2 = one2.getPageRefreshingDateTime();
    assertNotEquals(prePageRefreshingDateTime, pageRefreshingDateTime, "modified datetime");
    assertNotEquals(prePageRefreshingDateTime, pageRefreshingDateTime2, "modified datetime");

    List<Page> pages = pageService.findAllBySiteId(site.getId());
    Long count = pageService.countBySiteId(site.getId());
    assertEquals(1, pages.size(), "findAllBySiteId page size");
    assertEquals(1, count, "findAllBySiteId page size");

    List<Page> pagesForRefreshing = pageService.findAllForRefreshingBySiteId(site.getId());
    assertEquals(0, pagesForRefreshing.size());
    one = pageService.findOne(page.getId());
    one.updatePageRefreshingDateTime();

//    em.flush();
    try {
      Thread.sleep(1000); // 1초 지나고
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    List<Page> onesForRefreshing = pageService.findAllForRefreshingBySiteId(site.getId());
    assertNotEquals(0, onesForRefreshing.size());
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

  @Test
  void findPageIdBySiteId() {
    // site 넣기
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteId(board, site.getId());
    //page 넣기
    int pageRefreshSecond = 1;
    for (int pageNum = 1; pageNum <= 1; pageNum++) {
      pageService.savePageWithBoardId(Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build(), board.getId());
    }
    //
    pageService.deleteRedis(site.getId()); // 이전에 있을걸 대비
    Long pageIdBySiteId = pageService.findRedisPageIdBySiteId(site.getId());
    assertNull(pageIdBySiteId);
    List<Page> pages = pageService.findAllForRefreshingBySiteId(site.getId());
    for (Page page : pages) {
      page.updatePageRefreshingDateTime();
      pageService.saveRedis(site.getId(), page.getId());
    }

    pageIdBySiteId = pageService.findRedisPageIdBySiteId(site.getId());
    assertNotNull(pageIdBySiteId);
    pageIdBySiteId = pageService.findRedisPageIdBySiteId(site.getId());
    assertNull(pageIdBySiteId);
  }
}
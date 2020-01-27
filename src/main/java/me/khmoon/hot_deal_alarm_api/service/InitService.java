package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {
  private final SiteService siteService;
  private final ApplicationProperties applicationProperties;
  private final BoardService boardService;
  private final PageService pageService;

  private void addBoard(Site site, BoardName boardName, String boardParam, int startPageNum, int pageNumSize) {
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteId(board, site.getId());
    int pageRefreshSecond = 60;
    for (int pageNum = startPageNum; pageNum < pageNumSize + startPageNum; pageNum++) {
      Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
      pageService.savePageWithBoardId(page, board.getId());
    }
  }

  public void dbInitPpomppu(int startPageNum, int pageNumSize) {//TODO 게시판 별로 파라미터를 분리할 필요가 있겠다.
    String siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    String siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    Site site = Site.builder().siteName(SiteName.PPOMPPU).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    addBoard(site, BoardName.DOMESTIC, applicationProperties.getPpomppu().getParam().getDomestic(), startPageNum, pageNumSize);
    addBoard(site, BoardName.OVERSEAS, applicationProperties.getPpomppu().getParam().getOverseas(), startPageNum, pageNumSize);
  }

  public void dbInitDealbada(int startPageNum, int pageNumSize) {
    String siteListUrl = applicationProperties.getDealbada().getUrl().getList();
    String siteViewUrl = applicationProperties.getDealbada().getUrl().getView();
    Site site = Site.builder().siteName(SiteName.DEALBADA).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    addBoard(site, BoardName.DOMESTIC, applicationProperties.getDealbada().getParam().getDomestic(), startPageNum, pageNumSize);
    addBoard(site, BoardName.OVERSEAS, applicationProperties.getDealbada().getParam().getOverseas(), startPageNum, pageNumSize);
  }

  public void dbInitClien(int startPageNum, int pageNumSize) {
    String siteListUrl = applicationProperties.getClien().getUrl().getList();
    String siteViewUrl = applicationProperties.getClien().getUrl().getView();
    Site site = Site.builder().siteName(SiteName.CLIEN).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    addBoard(site, BoardName.THRIFTY, applicationProperties.getClien().getParam().getThrifty(), startPageNum, pageNumSize);
  }

  public void dbInitCoolenjoy(int startPageNum, int pageNumSize) {
    String siteListUrl = applicationProperties.getCoolenjoy().getUrl().getList();
    String siteViewUrl = applicationProperties.getCoolenjoy().getUrl().getView();
    Site site = Site.builder().siteName(SiteName.COOLENJOY).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    addBoard(site, BoardName.THRIFTY, applicationProperties.getCoolenjoy().getParam().getThrifty(), startPageNum, pageNumSize);
  }
}
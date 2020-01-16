package me.khmoon.hot_deal_alarm_api;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import me.khmoon.hot_deal_alarm_api.service.BoardService;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@Profile({"beta", "dev", "local"})
@RequiredArgsConstructor
public class InitDb {
  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInitPpomppu(1, 5);
    initService.dbInitDealbada(1, 5);
    initService.dbInitClien(0, 5);
    initService.dbInitCoolenjoy(0, 5);
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  public static class InitService {
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

    public void dbInitPpomppu(int startPageNum, int pageNumSize) {
      String siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
      String siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
      SiteName siteName = SiteName.PPOMPPU;
      Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
      siteService.add(site);
      addBoard(site, BoardName.DOMESTIC, applicationProperties.getPpomppu().getParam().getDomestic(), startPageNum, pageNumSize);
      addBoard(site, BoardName.OVERSEAS, applicationProperties.getPpomppu().getParam().getOverseas(), startPageNum, pageNumSize);
    }

    public void dbInitDealbada(int startPageNum, int pageNumSize) {
      String siteListUrl = applicationProperties.getDealbada().getUrl().getList();
      String siteViewUrl = applicationProperties.getDealbada().getUrl().getView();
      SiteName siteName = SiteName.DEALBADA;
      Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
      siteService.add(site);
      addBoard(site, BoardName.DOMESTIC, applicationProperties.getDealbada().getParam().getDomestic(), startPageNum, pageNumSize);
      addBoard(site, BoardName.OVERSEAS, applicationProperties.getDealbada().getParam().getOverseas(), startPageNum, pageNumSize);
    }

    public void dbInitClien(int startPageNum, int pageNumSize) {
      String siteListUrl = applicationProperties.getClien().getUrl().getList();
      String siteViewUrl = applicationProperties.getClien().getUrl().getView();
      SiteName siteName = SiteName.CLIEN;
      Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
      siteService.add(site);
      addBoard(site, BoardName.THRIFTY, applicationProperties.getClien().getParam().getThrifty(), startPageNum, pageNumSize);
    }

    public void dbInitCoolenjoy(int startPageNum, int pageNumSize) {
      String siteListUrl = applicationProperties.getCoolenjoy().getUrl().getList();
      String siteViewUrl = applicationProperties.getCoolenjoy().getUrl().getView();
      SiteName siteName = SiteName.COOLENJOY;
      Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
      siteService.add(site);
      addBoard(site, BoardName.THRIFTY, applicationProperties.getCoolenjoy().getParam().getThrifty(), startPageNum, pageNumSize);
    }
  }
}
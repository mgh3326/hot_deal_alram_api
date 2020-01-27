package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {
  private final SiteService siteService;
  private final BoardService boardService;
  private final PageService pageService;

  private void addBoard(Site site, BoardName boardName, int startPageNum, int pageNumSize) {
    Board board = Board.builder().boardName(boardName).build();
    boardService.addWithSiteId(board, site.getId());
    int pageRefreshSecond = 60;
    for (int pageNum = startPageNum; pageNum < pageNumSize + startPageNum; pageNum++) {
      Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
      pageService.savePageWithBoardId(page, board.getId());
    }
  }

  public void dbInitPpomppu(int startPageNum, int pageNumSize) {//TODO 게시판 별로 파라미터를 분리할 필요가 있겠다.
    Site site = Site.builder().siteName(SiteName.PPOMPPU).build();
    siteService.add(site);
    addBoard(site, BoardName.DOMESTIC, startPageNum, pageNumSize);
    addBoard(site, BoardName.OVERSEAS, startPageNum, pageNumSize);
  }

  public void dbInitDealbada(int startPageNum, int pageNumSize) {
    Site site = Site.builder().siteName(SiteName.DEALBADA).build();
    siteService.add(site);
    addBoard(site, BoardName.DOMESTIC, startPageNum, pageNumSize);
    addBoard(site, BoardName.OVERSEAS, startPageNum, pageNumSize);
  }

  public void dbInitClien(int startPageNum, int pageNumSize) {
    Site site = Site.builder().siteName(SiteName.CLIEN).build();
    siteService.add(site);
    addBoard(site, BoardName.THRIFTY, startPageNum, pageNumSize);
  }

  public void dbInitCoolenjoy(int startPageNum, int pageNumSize) {
    Site site = Site.builder().siteName(SiteName.COOLENJOY).build();
    siteService.add(site);
    addBoard(site, BoardName.THRIFTY, startPageNum, pageNumSize);
  }
}
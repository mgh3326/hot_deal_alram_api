package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.repository.PageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PageService {
  private final PageRepository pageRepository;
  private final BoardService boardService;

  //페이지 추가
  @Transactional
  public Long savePage(Page page) {
    pageRepository.save(page);
    return page.getId();
  }

  @Transactional
  public Long savePageWithBoardId(Page page, Long boardId) {
    Board board = boardService.findOne(boardId);
    page.setBoard(board);
    return savePage(page);
  }

  public Page findOne(Long id) {
    return pageRepository.findOne(id);
  }

  public List<Page> findAll() {
    return pageRepository.findAll();
  }

  public List<Page> findAllBySiteId(Long SiteId) {
    return pageRepository.findAllBySiteId(SiteId);
  }

  public Long countBySiteId(Long siteId) {
    return pageRepository.countBySiteId(siteId);
  }

  public Page findOneForRefreshing(int refreshingSecond) { // TODO redis 를 사용하는게 좋을것 같다.
    return pageRepository.findOneForRefreshing(refreshingSecond);
  }

  public Page findOneForRefreshingBySiteId(int refreshingSecond, Long siteId) {
    return pageRepository.findOneForRefreshingBySiteId(refreshingSecond, siteId);
  }

}

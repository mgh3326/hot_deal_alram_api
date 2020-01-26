package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.repository.PageRedisRepository;
import me.khmoon.hot_deal_alarm_api.repository.PageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PageService {
  private final PageRepository pageRepository;
  private final BoardService boardService;
  private final PageRedisRepository pageRedisRepository;

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
    return pageRepository.findById(id).orElseThrow();
  }

  public Page findOneWithBoardWithSite(Long id) {
    return pageRepository.findOneWithBoardWithSite(id).orElseThrow();
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

  public Page findOneForRefreshing() { // redis를 사용 할 의미가 없을것 같다.(redis에 없는걸 찾아야 하므로)
    List<Page> oneForRefreshing = pageRepository.findOneForRefreshing(LocalDateTime.now(), PageRequest.of(0, 1));
    if (oneForRefreshing.size() == 0) {
      return null;
    }
    return oneForRefreshing.get(0); // TODO null 대신에 optional을 활용해보도록 하자
  }

  public Page findOneForRefreshingBySiteId(Long siteId) {
    List<Page> oneForRefreshingBySiteId = pageRepository.findOneForRefreshingBySiteId(siteId, LocalDateTime.now(), PageRequest.of(0, 1));
    if (oneForRefreshingBySiteId.size() == 0) {
      return null;
    }
    return oneForRefreshingBySiteId.get(0);
  }

  public List<Page> findAllForRefreshingBySiteId(Long siteId) {
    return pageRepository.findAllForRefreshingBySiteId(siteId, LocalDateTime.now());
  }

  public Long findRedisPageIdBySiteId(Long siteId) {
    return pageRedisRepository.findPageIdBySiteId(siteId);
  }

  public void saveRedis(Long siteId, Long pageId) {
    pageRedisRepository.save(siteId, pageId);
  }

  public void deleteRedis(Long siteId) {
    pageRedisRepository.delete(siteId);
  }

  public Page findOneWithBoard(Long pageId) {
    return pageRepository.findOneWithBoard(pageId).orElseThrow();
  }
}

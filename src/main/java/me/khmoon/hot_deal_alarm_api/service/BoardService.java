package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final SiteService siteService;

  //게시판 추가
  @Transactional
  public Long add(Board board) {
    boardRepository.save(board);
    return board.getId();
  }

  public Long addWithSiteId(Board board, long siteId) {
    Site site = siteService.findOne(siteId);
    board.setSite(site);
    return add(board);
  }

  public Long addWithSiteName(Board board, SiteName siteName) {
    Site site = siteService.findOneBySiteName(siteName);
    board.setSite(site);
    return add(board);
  }

  public Board findOne(Long id) {
    return boardRepository.findOne(id);
  }

  public Board findOneByBoardName(BoardName boardName, Long siteId) {
    return boardRepository.findOneByBoardName(boardName, siteId);
  }

  public List<Board> findAllWithSite() {
    return boardRepository.findAllWithSite();
  }
}

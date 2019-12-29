package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final SiteRepository siteRepository;

  //게시판 추가
  @Transactional
  public Long add(long siteId, Board board) {
    Site site = siteRepository.findOne(siteId);
    board.setSite(site);
    boardRepository.save(board);
    return board.getId();
  }
}

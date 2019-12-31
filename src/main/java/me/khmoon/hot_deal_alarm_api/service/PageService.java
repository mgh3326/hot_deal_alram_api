package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import me.khmoon.hot_deal_alarm_api.repository.PageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PageService {
  private final PageRepository pageRepository;
  private final BoardRepository boardRepository;

  //페이지 추가
  @Transactional
  public Long savePage(BoardName boardName, Page page) {
    Board board = boardRepository.findOneByBoardName(boardName);
    page.setBoard(board);
    pageRepository.save(page);
    return page.getId();
  }

  //페이지 추가
  @Transactional
  public Long savePage(Long boardId, Page page) {
    Board board = boardRepository.findOne(boardId);
    page.setBoard(board);
    pageRepository.save(page);
    return page.getId();
  }
}

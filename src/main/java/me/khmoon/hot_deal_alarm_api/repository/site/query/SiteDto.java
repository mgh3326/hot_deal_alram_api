package me.khmoon.hot_deal_alarm_api.repository.site.query;

import lombok.Data;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardKoreanName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteKoreanName;

import java.util.List;
import java.util.stream.Collectors;

@Data

public class SiteDto {
  private SiteKoreanName siteKoreanName;
  private List<BoardDto> boards;

  public SiteDto(Site site) {
    this.siteKoreanName = site.getSiteKoreanName();
    this.boards = site.getBoards().stream()
            .map(BoardDto::new)
            .collect(Collectors.toList());
  }

  @Data
  private static class BoardDto {
    private BoardKoreanName boardKoreanName;
    private Long boardId;

    public BoardDto(Board board) {
      this.boardKoreanName = board.getBoardKoreanName();
      this.boardId = board.getId();
    }
  }
}


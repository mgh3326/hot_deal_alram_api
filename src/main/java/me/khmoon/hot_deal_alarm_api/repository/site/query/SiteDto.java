package me.khmoon.hot_deal_alarm_api.repository.site.query;

import lombok.Data;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;

import java.util.List;
import java.util.stream.Collectors;

@Data

public class SiteDto {
  private SiteName siteName;
  private List<BoardDto> boards;

  public SiteDto(Site site) {
    this.siteName = site.getSiteName();
    this.boards = site.getBoards().stream()
            .map(BoardDto::new)
            .collect(Collectors.toList());
  }

  @Data
  private static class BoardDto {
    private BoardName boardName;
    private Long boardId;

    public BoardDto(Board board) {
      this.boardName = board.getBoardName();
      this.boardId = board.getId();
    }
  }
}


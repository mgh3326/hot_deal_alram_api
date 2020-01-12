package me.khmoon.hot_deal_alarm_api.api;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.service.BoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
  private final BoardService boardService;

  @GetMapping("/api/boards")
  public List<BoardDto> boards() {
    List<Board> findBoards = boardService.findAllWithSite();
    return (findBoards.stream())
            .map(BoardDto::new)
            .collect(Collectors.toList());
  }

  @Data
  private static class BoardDto {
    private SiteName siteName;
    private BoardName boardName;

    public BoardDto(Board board) {
      this.siteName = board.getSite().getSiteName();
      this.boardName = board.getBoardName();
    }
  }
}

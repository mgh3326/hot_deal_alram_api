package me.khmoon.hot_deal_alarm_api.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SiteApiController {
  private final SiteService siteService;

  @GetMapping("/api/sites")
  public List<SiteDto> sites() {
    List<Site> findSites = siteService.findAllWithBoard();
    //엔티티 -> DTO 변환
    return (findSites.stream()
            .map(SiteDto::new)
            .collect(Collectors.toList()));
  }

  @Data
  private static class SiteDto {
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

      public BoardDto(Board board) {
        this.boardName = board.getBoardName();
      }
    }
  }
}

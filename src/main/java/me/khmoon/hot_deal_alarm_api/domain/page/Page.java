package me.khmoon.hot_deal_alarm_api.domain.page;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "pages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "page_id")
  private Long id;

  @NotNull
  private int pageNum;
  @NotNull
  private int pageRefreshSecond;//페이지 크롤링 시간
  @NotNull

  private LocalDateTime pageRefreshingDateTime;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @Builder
  public Page(int pageNum, int pageRefreshSecond) {
    this.pageNum = pageNum;
    this.pageRefreshSecond = pageRefreshSecond;
    this.pageRefreshingDateTime = LocalDateTime.now();
  }

  //==연관관계 메서드==//
  public void setBoard(Board board) {
    this.board = board;
    board.getPages().add(this);
  }

  public void updatePageRefreshingDateTime() {
    this.pageRefreshingDateTime = LocalDateTime.now().plusSeconds(this.pageRefreshSecond);
  }
}

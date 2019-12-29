package me.khmoon.hot_deal_alarm_api.domain.page;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;

import javax.persistence.*;

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

  private int pageNum;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @Builder
  public Page(int pageNum) {
    this.pageNum = pageNum;
  }

  //==연관관계 메서드==//
  public void setBoard(Board board) {
    this.board = board;
    board.getPages().add(this);
  }

}

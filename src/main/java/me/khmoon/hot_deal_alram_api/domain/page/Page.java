package me.khmoon.hot_deal_alram_api.domain.page;

import me.khmoon.hot_deal_alram_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alram_api.domain.board.Board;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "pages")
public class Page extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "page_id")
  private Long id;
  private String PageUrl;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "board_id")
  private Board board;
}

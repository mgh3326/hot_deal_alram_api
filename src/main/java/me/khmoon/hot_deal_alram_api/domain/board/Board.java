package me.khmoon.hot_deal_alram_api.domain.board;

import me.khmoon.hot_deal_alram_api.domain.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "boardName", columnList = "boardName")})
public class Board extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String BoradUrl;
  @Enumerated(EnumType.STRING)
  private BoardName boardName;
}

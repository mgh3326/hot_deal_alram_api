package me.khmoon.hot_deal_alarm_api.domain.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(indexes = {@Index(name = "boardName", columnList = "boardName")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private BoardName boardName;
  @Enumerated(EnumType.STRING)
  private BoardKoreanName boardKoreanName;
  private String boardParam;

  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "board")
  private List<Page> pages = new ArrayList<>();

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "site_id")
  private Site site;

  @Builder
  public Board(BoardName boardName) {
    this.boardName = boardName;
    switch (boardName) {
      case DOMESTIC:
        this.boardKoreanName = BoardKoreanName.국내;
        break;
      case OVERSEAS:
        this.boardKoreanName = BoardKoreanName.해외;
        break;
      case THRIFTY:
        this.boardKoreanName = BoardKoreanName.알구게;
        break;
    }
  }

  //==연관관계 메서드==//
  public void setSite(Site site) {
    this.site = site;
    site.getBoards().add(this);
  }

  public void addParam(String boardParam) {
    this.boardParam = boardParam;
  }
}

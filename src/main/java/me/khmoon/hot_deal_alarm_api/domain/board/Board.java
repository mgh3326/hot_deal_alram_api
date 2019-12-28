package me.khmoon.hot_deal_alarm_api.domain.board;

import lombok.*;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

  private String boardBaseListUrl;//상품 리스트
  private String boardBaseViewUrl;//상품 상세 주소

  @OneToMany(mappedBy = "board")
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "board")
  private List<Page> pages = new ArrayList<>();

  @Builder
  public Board(BoardName boardName, String boardBaseListUrl, String boardBaseViewUrl) {
    this.boardName = boardName;
    this.boardBaseListUrl = boardBaseListUrl;
    this.boardBaseViewUrl = boardBaseViewUrl;
  }
}

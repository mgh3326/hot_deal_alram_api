package me.khmoon.hot_deal_alram_api.domain.board;

import lombok.Getter;
import lombok.Setter;
import me.khmoon.hot_deal_alram_api.domain.page.Page;
import me.khmoon.hot_deal_alram_api.domain.post.Post;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(name = "boardName", columnList = "boardName")})
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private BoardName boardName;

  @OneToMany(mappedBy = "board")
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "board")
  private List<Page> pages = new ArrayList<>();
}

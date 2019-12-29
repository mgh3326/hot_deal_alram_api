package me.khmoon.hot_deal_alarm_api.domain.post;

import lombok.Getter;
import me.khmoon.hot_deal_alarm_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(indexes = {@Index(name = "postOriginId", columnList = "postOriginId")})
public class Post extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;
  private Long postOriginId;
  private String postType;// Enum으로 하고 싶었는데, Type은 현재 fix 하기 어려울것 같다.
  private String postName;
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus; // READY, COMP
  private Long PostRecommendationSize;
  private Long PostCommentSize;
  private Long PostClickCount;
  private String PostUrl;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "board_id")
  private Board board;
}

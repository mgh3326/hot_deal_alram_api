package me.khmoon.hot_deal_alram_api.domain.post;

import me.khmoon.hot_deal_alram_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alram_api.domain.board.BoardName;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "postId", columnList = "postId"), @Index(name = "boardName", columnList = "boardName")})
public class Post extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long postId;
  @Enumerated(EnumType.STRING)
  private BoardName boardName;
  private String postType;// Enum으로 하고 싶었는데, Type은 현재 fix 하기 어려울것 같다.
  private String postName;
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus; // READY, COMP
  private Long PostRecommendationSize;
  private Long PostCommentSize;
  private Long PostClickCount;
  private String PostUrl;
}

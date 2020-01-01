package me.khmoon.hot_deal_alarm_api.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(indexes = {@Index(name = "postOriginId", columnList = "postOriginId")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;
  private Long postOriginId;
  private String postType;// Enum으로 하고 싶었는데, Type은 현재 fix 하기 어려울것 같다. (그냥 한글 넣을듯)
  private String postTitle;
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus; // READY, COMP (진행중, 종료됨)
  private int PostRecommendationCount;//추천수
  private int PostCommentCount;//댓글 수
  private int PostOriginClickCount;//실제 클릭수
  private String PostUrl;
  private LocalDateTime PostOriginDateTime;
  @ColumnDefault("0")
  private int PostClickCount;//내 api 클릭수

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @Builder
  public Post(Long postOriginId,
              String postType,
              String postTitle,
              PostStatus postStatus,
              int postRecommendationCount,
              int postCommentCount,
              String postUrl,
              int postClickCount,
              int postOriginClickCount,
              LocalDateTime postOriginDateTime) {
    this.postOriginId = postOriginId;
    this.postType = postType;
    this.postTitle = postTitle;
    this.postStatus = postStatus;
    this.PostRecommendationCount = postRecommendationCount;
    this.PostCommentCount = postCommentCount;
    this.PostOriginClickCount = postOriginClickCount;
    this.PostUrl = postUrl;
    this.PostOriginDateTime = postOriginDateTime;
    this.PostClickCount = postClickCount;
  }

  //==연관관계 메서드==//
  public void setBoard(Board board) {
    this.board = board;
    board.getPosts().add(this);
  }
}

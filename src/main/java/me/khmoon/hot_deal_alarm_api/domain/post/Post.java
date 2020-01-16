package me.khmoon.hot_deal_alarm_api.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.BaseTimeEntity;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.user.UserPost;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(indexes = {@Index(name = "postOriginId", columnList = "postOriginId")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
  @Id
  @GeneratedValue
  @Column(name = "post_id")
  private Long id;
  private Long postOriginId;
  private String postType;// Enum으로 하고 싶었는데, Type은 현재 fix 하기 어려울것 같다. (그냥 한글 넣을듯)
  private String postTitle;
  private String postWriter;
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus; // READY, COMP (진행중, 종료됨)
  private int postRecommendationCount;//추천수
  @ColumnDefault("0")
  private int postDisLikeCount;//싫어요
  private int postCommentCount;//댓글 수
  private int postOriginClickCount;//실제 클릭수


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
              int postOriginClickCount,
              int postDisLikeCount, String postWriter) {
    this.postOriginId = postOriginId;
    this.postType = postType;
    this.postTitle = postTitle;
    this.postWriter = postWriter;
    this.postStatus = postStatus;
    this.postRecommendationCount = postRecommendationCount;
    this.postDisLikeCount = postDisLikeCount;
    this.postCommentCount = postCommentCount;
    this.postOriginClickCount = postOriginClickCount;
  }

  @OneToMany(mappedBy = "post")
  private List<UserPost> userPosts = new ArrayList<>();

  //==연관관계 메서드==//
  public void setBoard(Board board) {
    this.board = board;
    board.getPosts().add(this);
  }

  public Post update(Post post) {// 이렇게 equals를 확인해야하나
    if (!this.postTitle.equals(post.postTitle)) this.postTitle = post.postTitle;
    if (!this.postType.equals(post.postType)) this.postType = post.postType;
    if (!this.postWriter.equals(post.postWriter)) this.postWriter = post.postWriter;
    if (!this.postStatus.equals(post.postStatus)) this.postStatus = post.postStatus;
    this.postRecommendationCount = post.postRecommendationCount;
    this.postDisLikeCount = post.postDisLikeCount;
    this.postCommentCount = post.postCommentCount;
    this.postOriginClickCount = post.postOriginClickCount;
    return this;
  }
}

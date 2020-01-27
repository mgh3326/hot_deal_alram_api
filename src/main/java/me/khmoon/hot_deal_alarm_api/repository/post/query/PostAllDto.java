package me.khmoon.hot_deal_alarm_api.repository.post.query;

import lombok.Data;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardKoreanName;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.domain.post.PostStatus;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteKoreanName;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class PostAllDto {
  private String postType;
  private String postTitle;
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus; // READY, COMP (진행중, 종료됨)
  private int postRecommendationCount;//추천수
  private int postCommentCount;//댓글 수
  @Enumerated(EnumType.STRING)
  private BoardKoreanName boardKoreanName;
  @Enumerated(EnumType.STRING)
  private SiteKoreanName siteKoreanName;

  public PostAllDto(Post post) {
    this.postType = post.getPostType();
    this.postTitle = post.getPostTitle();
    this.postStatus = post.getPostStatus();
    this.postRecommendationCount = post.getPostRecommendationCount();
    this.postCommentCount = post.getPostCommentCount();
    this.boardKoreanName = post.getBoard().getBoardKoreanName();
    this.siteKoreanName = post.getBoard().getSite().getSiteKoreanName();
  }
}

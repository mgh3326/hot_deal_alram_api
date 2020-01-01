package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.domain.post.PostStatus;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class PostServiceTest {
  @Autowired
  private BoardService boardService;
  @Autowired
  private SiteService siteService;
  @Autowired
  private PostService postService;
  @Autowired
  private ApplicationProperties applicationProperties;
  private String boardParam;
  private String siteListUrl;
  private String siteViewUrl;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;

  @PostConstruct
  public void init() {
    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    boardParam = applicationProperties.getPpomppu().getParam().getDomestic();
  }

  @Test
  void savePost() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.add(site.getId(), board);

    String postTitle = "[티몬] 자뎅 커피/음료 230mlx30팩 / 230mlx24팩 ( 9,900원 / 무...";
    long postOriginId = 339492L;
    String postType = "기타";
    String postWriter = "까댄당";
    int postRecommendationCount = 0;
    int postCommentCount = 0;
    int postDisLikeCount = 0;
    PostStatus postStatus = PostStatus.READY;
    int postOriginClickCount = 1822;
    LocalTime parse = LocalTime.parse("21:05:54");
    LocalDateTime postOriginDateTime = LocalDate.now().atTime(parse);// 2018-07-26T02:30
    Post post = Post.builder()
            .postTitle(postTitle)
            .postOriginId(postOriginId)
            .postType(postType)
            .postWriter(postWriter)
            .postRecommendationCount(postRecommendationCount)
            .postDisLikeCount(postDisLikeCount)
            .postCommentCount(postCommentCount)
            .postStatus(postStatus)
            .postOriginClickCount(postOriginClickCount)
            .postOriginDateTime(postOriginDateTime)
            .build();
    postService.savePost(board.getId(), post);

    assertEquals(post.getPostTitle(), postTitle, "equal test post");
    assertEquals(post.getPostOriginId(), postOriginId, "equal test post");
    assertEquals(post.getPostType(), postType, "equal test post");
    assertEquals(post.getPostWriter(), postWriter, "equal test post");
    assertEquals(post.getPostRecommendationCount(), postRecommendationCount, "equal test post");
    assertEquals(post.getPostDisLikeCount(), postDisLikeCount, "equal test post");
    assertEquals(post.getPostCommentCount(), postCommentCount, "equal test post");
    assertEquals(post.getPostStatus(), postStatus, "equal test post");
    assertEquals(post.getPostOriginClickCount(), postOriginClickCount, "equal test post");
    assertEquals(post.getPostOriginDateTime(), postOriginDateTime, "equal test post");
  }

  @Test
  void savePosts() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.add(site.getId(), board);

    String postTitle = "[티몬] 자뎅 커피/음료 230mlx30팩 / 230mlx24팩 ( 9,900원 / 무...";
    long postOriginId = 339492L;
    String postType = "기타";
    String postWriter = "까댄당";
    int postRecommendationCount = 0;
    int postCommentCount = 0;
    PostStatus postStatus = PostStatus.READY;
    int postOriginClickCount = 1822;
    LocalTime parse = LocalTime.parse("21:05:54");
    LocalDateTime postOriginDateTime = LocalDate.now().atTime(parse);// 2018-07-26T02:30
    int postDisLikeCount = 0;
    Post post = Post.builder()
            .postTitle(postTitle)
            .postOriginId(postOriginId)
            .postType(postType)
            .postWriter(postWriter)
            .postRecommendationCount(postRecommendationCount)
            .postDisLikeCount(postDisLikeCount)
            .postCommentCount(postCommentCount)
            .postStatus(postStatus)
            .postOriginClickCount(postOriginClickCount)
            .postOriginDateTime(postOriginDateTime)
            .build();
    List<Post> posts = new ArrayList<>();
    posts.add(post);
    post = Post.builder()
            .postTitle(postTitle)
            .postOriginId(339491L)
            .postType(postType)
            .postWriter(postWriter)
            .postRecommendationCount(postRecommendationCount)
            .postDisLikeCount(postDisLikeCount)
            .postCommentCount(postCommentCount)
            .postStatus(postStatus)
            .postOriginClickCount(postOriginClickCount)
            .postOriginDateTime(postOriginDateTime)
            .build();
    posts.add(post);

    postService.savePostAll(board.getId(), posts);
    List<Post> posts1 = postService.findAll();
    assertEquals(posts1.size(), 2, "equal test post");
  }
}
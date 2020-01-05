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

  private String boardParamDealbada;
  private String siteListUrlDealbada;
  private String siteViewUrlDealbada;
  private SiteName siteNameDealbada = SiteName.DEALBADA;

  @PostConstruct
  public void init() {
    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    boardParam = applicationProperties.getPpomppu().getParam().getDomestic();

    siteListUrlDealbada = applicationProperties.getDealbada().getUrl().getList();
    siteViewUrlDealbada = applicationProperties.getDealbada().getUrl().getView();
    boardParamDealbada = applicationProperties.getDealbada().getParam().getDomestic();
  }

  @Test
  void savePost() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteId(board, site.getId());

    String postTitle = "[티몬] 자뎅 커피/음료 230mlx30팩 / 230mlx24팩 ( 9,900원 / 무...";
    long postOriginId = 339492L;
    String postType = "기타";
    String postWriter = "까댄당";
    int postRecommendationCount = 0;
    int postCommentCount = 0;
    int postDisLikeCount = 0;
    PostStatus postStatus = PostStatus.READY;
    int postOriginClickCount = 1822;
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
            .build();
    postService.savePostWithBoardId(post, board.getId());

    assertEquals(post.getPostTitle(), postTitle, "equal test post");
    assertEquals(post.getPostOriginId(), postOriginId, "equal test post");
    assertEquals(post.getPostType(), postType, "equal test post");
    assertEquals(post.getPostWriter(), postWriter, "equal test post");
    assertEquals(post.getPostRecommendationCount(), postRecommendationCount, "equal test post");
    assertEquals(post.getPostDisLikeCount(), postDisLikeCount, "equal test post");
    assertEquals(post.getPostCommentCount(), postCommentCount, "equal test post");
    assertEquals(post.getPostStatus(), postStatus, "equal test post");
    assertEquals(post.getPostOriginClickCount(), postOriginClickCount, "equal test post");
    assertEquals(post.getBoard().getBoardName(), boardName, "equal test post");
    assertEquals(post.getBoard().getBoardParam(), boardParam, "equal test post");
  }

  @Test
  void savePosts() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParam).build();
    boardService.addWithSiteId(board, site.getId());

    String postTitle = "[티몬] 자뎅 커피/음료 230mlx30팩 / 230mlx24팩 ( 9,900원 / 무...";
    long postOriginId = 339492L;
    String postType = "기타";
    String postWriter = "까댄당";
    int postRecommendationCount = 0;
    int postCommentCount = 0;
    PostStatus postStatus = PostStatus.READY;
    int postOriginClickCount = 1822;
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
            .build();
    post.setBoard(board);
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
            .build();
    post.setBoard(board);

    posts.add(post);

    postService.savePostAll(posts);
    List<Post> posts1 = postService.findAll();
    assertEquals(posts1.size(), 2, "equal test post");
    assertEquals(posts1.get(0).getBoard().getBoardName(), boardName, "equal test post");
    assertEquals(posts1.get(0).getBoard().getBoardParam(), boardParam, "equal test post");
    assertEquals(posts1.get(1).getBoard().getBoardName(), boardName, "equal test post");
    assertEquals(posts1.get(1).getBoard().getBoardParam(), boardParam, "equal test post");
  }

  @Test
  void savePostDealbada() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteNameDealbada).siteListUrl(siteListUrlDealbada).siteViewUrl(siteViewUrlDealbada).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParamDealbada).build();
    boardService.addWithSiteId(board, site.getId());

    String postTitle = "  [G마켓] 컬쳐랜드 10만/5만원권 각각 현금 10% 캐시백 or 카드 7% 할인 (100,000/0)";
    long postOriginId = 14423L;
    String postType = "기타";
    String postWriter = "탕진기업옥션";
    int postRecommendationCount = 22;
    int postCommentCount = 20;
    int postDisLikeCount = 0;
    PostStatus postStatus = PostStatus.READY;
    int postOriginClickCount = 9003;
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
            .build();
    postService.savePostWithBoardId(post, board.getId());

    assertEquals(post.getPostTitle(), postTitle, "equal test post");
    assertEquals(post.getPostOriginId(), postOriginId, "equal test post");
    assertEquals(post.getPostType(), postType, "equal test post");
    assertEquals(post.getPostWriter(), postWriter, "equal test post");
    assertEquals(post.getPostRecommendationCount(), postRecommendationCount, "equal test post");
    assertEquals(post.getPostDisLikeCount(), postDisLikeCount, "equal test post");
    assertEquals(post.getPostCommentCount(), postCommentCount, "equal test post");
    assertEquals(post.getPostStatus(), postStatus, "equal test post");
    assertEquals(post.getPostOriginClickCount(), postOriginClickCount, "equal test post");
    assertEquals(post.getBoard().getBoardName(), boardName, "equal test post");
    assertEquals(post.getBoard().getBoardParam(), boardParamDealbada, "equal test post");
  }
}
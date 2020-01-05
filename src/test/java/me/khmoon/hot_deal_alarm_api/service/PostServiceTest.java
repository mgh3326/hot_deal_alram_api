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

  private BoardName boardNameThrifty = BoardName.THRIFTY;
  private String siteListUrlClien;
  private String siteViewUrlClien;
  private SiteName siteNameClien = SiteName.CLIEN;
  private String boardParamClien;

  private String siteListUrlCoolenjoy;
  private String siteViewUrlCoolenjoy;
  private String boardParamCoolenjoy;
  private SiteName siteNameCoolenjoy = SiteName.COOLENJOY;

  @PostConstruct
  public void init() {


    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    boardParam = applicationProperties.getPpomppu().getParam().getDomestic();

    siteListUrlDealbada = applicationProperties.getDealbada().getUrl().getList();
    siteViewUrlDealbada = applicationProperties.getDealbada().getUrl().getView();
    boardParamDealbada = applicationProperties.getDealbada().getParam().getDomestic();

    siteListUrlClien = applicationProperties.getClien().getUrl().getList();
    siteViewUrlClien = applicationProperties.getClien().getUrl().getView();
    boardParamClien = applicationProperties.getClien().getParam().getThrifty();

    siteListUrlCoolenjoy = applicationProperties.getCoolenjoy().getUrl().getList();
    siteViewUrlCoolenjoy = applicationProperties.getCoolenjoy().getUrl().getView();
    boardParamCoolenjoy = applicationProperties.getCoolenjoy().getParam().getThrifty();
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

    checkPost(postTitle, postOriginId, postType, postWriter, postRecommendationCount, postCommentCount, postDisLikeCount, postStatus, postOriginClickCount, post, post.getBoard().getBoardName(), boardName, post.getBoard().getBoardParam(), boardParam);
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

    checkPost(postTitle, postOriginId, postType, postWriter, postRecommendationCount, postCommentCount, postDisLikeCount, postStatus, postOriginClickCount, post, post.getBoard().getBoardName(), boardName, post.getBoard().getBoardParam(), boardParamDealbada);
  }


  @Test
  void savePostClien() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteNameClien).siteListUrl(siteListUrlClien).siteViewUrl(siteViewUrlClien).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardNameThrifty).boardParam(boardParamClien).build();
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

    checkPost(postTitle, postOriginId, postType, postWriter, postRecommendationCount, postCommentCount, postDisLikeCount, postStatus, postOriginClickCount, post, boardNameThrifty, post.getBoard().getBoardName(), boardParamClien, post.getBoard().getBoardParam());
  }

  @Test
  void savePostCoolenjoy() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteNameCoolenjoy).siteListUrl(siteListUrlCoolenjoy).siteViewUrl(siteViewUrlCoolenjoy).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardNameThrifty).boardParam(boardParamCoolenjoy).build();
    boardService.addWithSiteId(board, site.getId());

    String postTitle = "[Amazon] WD Elements 데스크톱 하드 드라이브 8TB 12…";
    long postOriginId = 1400772L;
    String postType = "PC관련";
    String postWriter = "seho";
    int postRecommendationCount = 4;
    int postCommentCount = 18;
    int postDisLikeCount = 0;
    PostStatus postStatus = PostStatus.READY;
    int postOriginClickCount = 3181;
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

    checkPost(postTitle, postOriginId, postType, postWriter, postRecommendationCount, postCommentCount, postDisLikeCount, postStatus, postOriginClickCount, post, boardNameThrifty, post.getBoard().getBoardName(), boardParamClien, post.getBoard().getBoardParam());
  }

  private void checkPost(String postTitle, long postOriginId, String postType, String postWriter, int postRecommendationCount, int postCommentCount, int postDisLikeCount, PostStatus postStatus, int postOriginClickCount, Post post, BoardName boardName, BoardName boardNameExpect, String boardParam, String boardParamExpect) {
    assertEquals(postTitle, post.getPostTitle(), "equal test post");
    assertEquals(postOriginId, post.getPostOriginId(), "equal test post");
    assertEquals(postType, post.getPostType(), "equal test post");
    assertEquals(postWriter, post.getPostWriter(), "equal test post");
    assertEquals(postRecommendationCount, post.getPostRecommendationCount(), "equal test post");
    assertEquals(postDisLikeCount, post.getPostDisLikeCount(), "equal test post");
    assertEquals(postCommentCount, post.getPostCommentCount(), "equal test post");
    assertEquals(postStatus, post.getPostStatus(), "equal test post");
    assertEquals(postOriginClickCount, post.getPostOriginClickCount(), "equal test post");
    assertEquals(boardNameExpect, boardName, "equal test post");
    assertEquals(boardParamExpect, boardParam, "equal test post");
  }
}
package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")

@SpringBootTest
@Transactional
class CrawlingServiceTest {
  @Autowired
  private BoardService boardService;
  @Autowired
  private PageService pageService;
  @Autowired
  private SiteService siteService;
  @Autowired
  private CrawlingService crawlingService;
  @Autowired
  private PostService postService;
  @Autowired
  private ApplicationProperties applicationProperties;
  private String boardParamDomestic;
  private String boardParamOverseas;
  private String siteListUrl;
  private String siteViewUrl;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;
  private BoardName boardNameOverseas = BoardName.OVERSEAS;

  private SiteName siteNameDealbada = SiteName.DEALBADA;
  private String boardParamDealbada;
  private String siteListUrlDealbada;
  private String siteViewUrlDealbada;
  private SiteName siteNameClien = SiteName.CLIEN;
  private String boardParamClien;
  private String siteListUrlClien;
  private String siteViewUrlClien;
  private BoardName boardNameClien = BoardName.THRIFTY;

  @PostConstruct
  public void init() {
    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    boardParamDomestic = applicationProperties.getPpomppu().getParam().getDomestic();
    boardParamOverseas = applicationProperties.getPpomppu().getParam().getOverseas();

    siteListUrlDealbada = applicationProperties.getDealbada().getUrl().getList();
    siteViewUrlDealbada = applicationProperties.getDealbada().getUrl().getView();
    boardParamDealbada = applicationProperties.getDealbada().getParam().getDomestic();

    siteListUrlClien = applicationProperties.getClien().getUrl().getList();
    siteViewUrlClien = applicationProperties.getClien().getUrl().getView();
    boardParamClien = applicationProperties.getClien().getParam().getThrifty();
  }

  @Test
  void parsePpomppuDomestic() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParamDomestic).build();
    boardService.addWithSiteId(board, site.getId());

    int pageNum = 2;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());

    List<Post> posts = crawlingService.parse(page.getId());
    postService.savePostAllWithBoardId(posts, board.getId());
    List<Long> originIds = posts.stream().map(Post::getPostOriginId).collect(Collectors.toList());
    List<Post> posts1 = postService.findAll();
    List<Post> inOriginIds = postService.findInOriginIds(originIds, board.getId());
    assertEquals(posts.size(), posts1.size(), "equal test post");
    assertEquals(posts.size(), inOriginIds.size(), "equal test post");
    if (posts1.size() > 0) {
      assertEquals(posts1.get(0).getCreatedDateTime(), posts1.get(0).getModifiedDateTime(), "equal test post");
    }
  }


  @Test
  void parseDealbada() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteNameDealbada).siteListUrl(siteListUrlDealbada).siteViewUrl(siteViewUrlDealbada).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(boardParamDealbada).build();
    boardService.addWithSiteId(board, site.getId());

    int pageNum = 2;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());

    List<Post> posts = crawlingService.parse(page.getId());
    postService.savePostAllWithBoardId(posts, board.getId());
    List<Post> posts1 = postService.findAll();
    assertEquals(posts.size(), posts1.size(), "equal test post");
    if (posts1.size() > 0) {
      assertEquals(posts1.get(0).getCreatedDateTime(), posts1.get(0).getModifiedDateTime(), "equal test post");
    }
  }

  @Test
  void parseClien() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteNameClien).siteListUrl(siteListUrlClien).siteViewUrl(siteViewUrlClien).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardNameClien).boardParam(boardParamClien).build();
    boardService.addWithSiteId(board, site.getId());

    int pageNum = 0;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());

    List<Post> posts = crawlingService.parse(page.getId());
    postService.savePostAllWithBoardId(posts, board.getId());
    List<Post> posts1 = postService.findAll();
    assertEquals(posts.size(), posts1.size(), "equal test post");
    if (posts1.size() > 0) {
      assertEquals(posts1.get(0).getCreatedDateTime(), posts1.get(0).getModifiedDateTime(), "equal test post");
    }
  }

  @Test
  void parsePpomppuOverseas() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardNameOverseas).boardParam(boardParamOverseas).build();
    boardService.addWithSiteId(board, site.getId());

    int pageNum = 3;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());

    List<Post> posts = crawlingService.parse(page.getId());
    postService.savePostAllWithBoardId(posts, board.getId());
    List<Post> posts1 = postService.findAll();

    assertEquals(posts.size(), posts1.size(), "equal test post");
    if (posts1.size() > 0) {//CI 에서 안 될때가 있어서 만든 조건
      assertEquals(posts1.get(0).getCreatedDateTime(), posts1.get(0).getModifiedDateTime(), "equal test post");
    }
  }

  @Test
  void parseUpdate() {
    // 사이트 저장

    siteService.add(Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build());
    Site site = siteService.findOneBySiteName(siteName);
    //board 저장
    boardService.addWithSiteId(Board.builder().boardName(boardName).boardParam(boardParamDomestic).build(), site.getId());
    Board board = boardService.findOneByBoardName(boardName, site.getId());
    int pageNum = 4;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePageWithBoardId(page, board.getId());
    Set<Long> postIdSet = new HashSet<>();
    List<Post> posts = crawlingService.parse(page.getId());
    for (Post post : posts) postIdSet.add(post.getPostOriginId());
    postService.savePostAllWithBoardId(posts, board.getId());
    List<Post> posts2 = crawlingService.parse(page.getId());
    for (Post post : posts2) postIdSet.add(post.getPostOriginId());
    postService.savePostAllWithBoardId(posts2, board.getId());
    List<Post> posts1 = postService.findAll();
    System.out.println("postIdSet.size() = " + postIdSet.size());
    assertEquals(postIdSet.size(), posts1.size(), "equal test post");
    if (posts1.size() > 0) {
      assertEquals(postIdSet.size(), posts1.get(0).getBoard().getPosts().size(), "equal test post.board");
    }
  }
}
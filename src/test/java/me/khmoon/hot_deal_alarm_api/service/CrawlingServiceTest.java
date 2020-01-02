package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
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
  private String domestic;
  private String overseas;
  private String siteListUrl;
  private String siteViewUrl;
  private SiteName siteName = SiteName.PPOMPPU;
  private BoardName boardName = BoardName.DOMESTIC;

  @PostConstruct
  public void init() {
    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
    domestic = applicationProperties.getPpomppu().getParam().getDomestic();
    overseas = applicationProperties.getPpomppu().getParam().getOverseas();
  }

  @Test
  void parsePpomppuDomestic() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(domestic).build();
    boardService.add(site.getId(), board);

    int pageNum = 2;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePage(board.getId(), page);

    List<Post> posts = crawlingService.parse(page.getId());
    postService.savePostAll(board.getId(), posts);
    List<Post> posts1 = postService.findAll();
    assertEquals(posts.size(), posts1.size(), "equal test post");
    assertEquals(posts1.get(0).getCreatedDateTime(), posts1.get(0).getModifiedDateTime(), "equal test post");
  }

  @Test
  void parsePpomppuOverseas() {
    // 사이트 저장
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);

    //board 저장
    Board board = Board.builder().boardName(boardName).boardParam(overseas).build();
    boardService.add(site.getId(), board);

    int pageNum = 3;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePage(board.getId(), page);

    List<Post> posts = crawlingService.parse(page.getId());
    postService.savePostAll(board.getId(), posts);
    List<Post> posts1 = postService.findAll();

    assertEquals(posts.size(), posts1.size(), "equal test post");
    assertEquals(posts1.get(0).getCreatedDateTime(), posts1.get(0).getModifiedDateTime(), "equal test post");
  }

  @Test
  void parseUpdate() {
    // 사이트 저장

    siteService.add(Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build());
    Site site = siteService.findOneBySiteName(siteName);
    //board 저장
    boardService.add(site.getId(), Board.builder().boardName(boardName).boardParam(domestic).build());
    Board board = boardService.findOneByBoardName(boardName, site.getId());
    int pageNum = 4;
    int pageRefreshSecond = 60;
    Page page = Page.builder().pageNum(pageNum).pageRefreshSecond(pageRefreshSecond).build();
    pageService.savePage(board.getId(), page);
    Set<Long> postIdSet = new HashSet<>();
    List<Post> posts = crawlingService.parse(page.getId());
    for (Post post : posts) postIdSet.add(post.getPostOriginId());
    postService.savePostAll(board.getId(), posts);
    List<Post> posts2 = crawlingService.parse(page.getId());
    for (Post post : posts2) postIdSet.add(post.getPostOriginId());
    postService.savePostAll(board.getId(), posts2);
    List<Post> posts1 = postService.findAll();
    assertEquals(posts1.size(), postIdSet.size(), "equal test post");
  }
}
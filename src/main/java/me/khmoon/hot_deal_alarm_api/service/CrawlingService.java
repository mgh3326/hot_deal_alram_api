package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.domain.post.PostStatus;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawlingService {
  private final PageService pageService;
  private final PostService postService;
  private final ApplicationProperties applicationProperties;
  private String ppomppuListUrlFormat;
  private String userAgent;

  @PostConstruct
  public void init() {
    ppomppuListUrlFormat = applicationProperties.getPpomppu().getUrl().getList();
    userAgent = applicationProperties.getUserAgent();
  }

  public List<Post> parse(Long pageId) {
    Page page = pageService.findOne(pageId);
    Board board = page.getBoard();
    String boardParam = board.getBoardParam();
    Site site = board.getSite();
    SiteName siteName = site.getSiteName();
    int pageNum = page.getPageNum();
    List<Post> posts = new ArrayList<>();
    if (siteName.equals(SiteName.PPOMPPU)) {
      posts = ppomppuParse(boardParam, pageNum, board.getId(), site.getId());
    }
    return posts;
  }

  private List<Post> ppomppuParse(String boardParam, int pageNum, Long boardId, Long siteId) {
    ArrayList<Post> posts = new ArrayList<>();
    try {
      String url = String.format(ppomppuListUrlFormat, boardParam, pageNum);
      Document doc = Jsoup.connect(url)
              .userAgent(userAgent)
              .header("Referer", String.format(ppomppuListUrlFormat, boardParam, pageNum + 1))
              .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
              .header("Content-Type", "application/x-www-form-urlencoded")
              .header("Accept-Encoding", "gzip, deflate")
              .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
              .get();
      Elements elements = doc.select("ul.bbsList_new").not(".bbsList_new[style=margin-top:15px;]")
              .select("li.none-border");
      for (Element element : elements) {
        String writer = element.select("a > div.thmb_N2 > ul > li.names").text();
        String title = element.select("a > div.thmb_N2 > ul > li.title > span.cont").text();
        String type = element.select("a > div.thmb_N2 > ul > li.exp > span.ty").text();
        String commentCountStr = element.select("a > div.thmb_N2 > ul > li.title > span.rp").text();
        int commentCount = 0;
        if (!commentCountStr.equals("")) {
          commentCount = Integer.parseInt(commentCountStr);
        }
        String countLikeDisLike = element.select("a > div.thmb_N2 > ul > li.exp > span:nth-child(4)").text();
        String[] strings = countLikeDisLike.substring(1, countLikeDisLike.length() - 1).split("/");
        String originClickCountStr = strings[0].trim();
        int originClickCount = Integer.parseInt(originClickCountStr);
        String recommendationCountStr = strings[1].trim();
        int recommendationCount = Integer.parseInt(recommendationCountStr);
        String disLikeCountStr = strings[2].trim();
        int disLikeCount = Integer.parseInt(disLikeCountStr);
        PostStatus status = PostStatus.READY;
        if (element.select("a > div.thmb_N2 > ul > li.title > span.cont > span[style=color:#ACACAC]").size() > 0) {
          status = PostStatus.COMP;
        }
        String originUrl = element.select("a").attr("abs:href");
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(originUrl).build().getQueryParams();
        String originIdStr = queryParams.get("no").get(0);
        Long originId = Long.parseLong(originIdStr);
        Post oneByOriginId = postService.findOneByOriginId(originId, boardId, siteId);
        Post post = Post.builder()
                .postTitle(title)
                .postType(type)
                .postWriter(writer)
                .postCommentCount(commentCount)
                .postOriginClickCount(originClickCount)
                .postRecommendationCount(recommendationCount)
                .postDisLikeCount(disLikeCount)
                .postStatus(status)
                .postOriginId(originId)
                .build();
        if (oneByOriginId != null) {
          oneByOriginId.update(post);//이렇게만 해도 수정되는건가?
        } else {
          posts.add(post);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return posts;
  }
}

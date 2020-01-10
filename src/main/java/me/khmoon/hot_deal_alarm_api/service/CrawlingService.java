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

import static me.khmoon.hot_deal_alarm_api.helper.MyHelper.isNumeric;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawlingService {
  private final PageService pageService;
  private final PostService postService;
  private final ApplicationProperties applicationProperties;
  private String ppomppuListUrlFormat;
  private String dealbadaListUrlFormat;
  private String clienListUrlFormat;
  private String userAgent;

  @PostConstruct
  public void init() {
    ppomppuListUrlFormat = applicationProperties.getPpomppu().getUrl().getList();
    dealbadaListUrlFormat = applicationProperties.getDealbada().getUrl().getList();
    clienListUrlFormat = applicationProperties.getClien().getUrl().getList();
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
    switch (siteName) {
      case PPOMPPU:
        posts = ppomppuParse(boardParam, pageNum, board.getId(), site.getId());
        break;
      case DEALBADA:
        posts = dealbadaParse(boardParam, pageNum, board.getId(), site.getId());
        break;
      case CLIEN:
        posts = clienParse(boardParam, pageNum, board.getId(), site.getId());
        break;
    }
    return posts;
  }

  private List<Post> clienParse(String boardParam, int pageNum, Long boardId, Long siteId) {
    List<Post> posts = new ArrayList<>();
    try {
      Document doc = documentByUrl(boardParam, pageNum, clienListUrlFormat);
      Elements elements = doc.select("body > div.nav_container > div").select("div.list_item").select("div.symph-row");
      for (Element element : elements) {
        String writer = element.select("div.list_infomation > div.list_author > span.nickname").text();
        if ("".equals(writer)) {// writer가 이미지로 된 경우도 존재
          writer = element.select("div.list_infomation > div.list_author > span.nickimg > img").attr("alt");
        }
        String title = element.select("div.list_title > a > span[data-role=list-title-text]").text();
        String type = element.select("div.list_infomation > div.list_number > span").text();
        String commentCountStr = element.select("div.list_title > div.list_reply > span:nth-child(1)").text();
        int commentCount = 0;
        if (!"".equals(commentCountStr)) {
          commentCount = Integer.parseInt(commentCountStr);
        }
        String originClickCountStr = element.select("div.list_infomation > div.list_number > div.list_hit > span").text();
        int originClickCount = 0;
        if (isNumeric(originClickCountStr)) {
          originClickCount = Integer.parseInt(originClickCountStr);
        } else {
          if (originClickCountStr.endsWith("k")) {
            // example "11.1 k"
            String trim = originClickCountStr.substring(0, originClickCountStr.length() - 1).trim();
            originClickCount = (int) (Double.parseDouble(trim) * 1000);
          }
        }
        String recommendationCountStr = element.select("div.list_title > div.list_symph > span").text();
        int recommendationCount = Integer.parseInt(recommendationCountStr);
        int disLikeCount = 0;//TODO 이렇게 담아줘도 되려나
        PostStatus status = PostStatus.READY;
        if (!element.select("div.sold_out").isEmpty() || !element.select("div.list_title > a > span.solidout").isEmpty()) {
          status = PostStatus.COMP;
        }
        String originIdStr = element.attr("data-board-sn");
        Long originId = Long.parseLong(originIdStr);
        postsUpdate(boardId, siteId, posts, writer, title, type, commentCount, recommendationCount, disLikeCount, originClickCount, status, originId);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return posts;

  }

  private List<Post> ppomppuParse(String boardParam, int pageNum, Long boardId, Long siteId) {
    List<Post> posts = new ArrayList<>();
    try {
      Document doc = documentByUrl(boardParam, pageNum, ppomppuListUrlFormat);
      Elements elements = doc.select("ul.bbsList_new").not(".bbsList_new[style=margin-top:15px;]")
              .select("li.none-border");
      for (Element element : elements) {
        String writer = element.select("a > div.thmb_N2 > ul > li.names").text();
        String title = element.select("a > div.thmb_N2 > ul > li.title > span.cont").text();
        String type = element.select("a > div.thmb_N2 > ul > li.exp > span.ty").text();
        String commentCountStr = element.select("a > div.thmb_N2 > ul > li.title > span.rp").text();
        int commentCount = 0;
        if (!"".equals(commentCountStr)) {
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
        if (!element.select("a > div.thmb_N2 > ul > li.title > span.cont > span[style=color:#ACACAC]").isEmpty()) {
          status = PostStatus.COMP;
        }
        String originUrl = element.select("a").attr("abs:href");
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(originUrl).build().getQueryParams();
        String originIdStr = queryParams.get("no").get(0);
        Long originId = Long.parseLong(originIdStr);
        postsUpdate(boardId, siteId, posts, writer, title, type, commentCount, recommendationCount, disLikeCount, originClickCount, status, originId);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return posts;
  }

  private List<Post> dealbadaParse(String boardParam, int pageNum, Long boardId, Long siteId) {
    List<Post> posts = new ArrayList<>();
    try {
      Document doc = documentByUrl(boardParam, pageNum, dealbadaListUrlFormat);

      Elements elements = doc.select("#fboardlist > div > table > tbody").select("tr:not(.bo_notice)");
      for (Element element : elements) {
        String writer = element.select("td.td_name.sv_use > span > a > div").text();
        String title = element.select("td.td_subject > a").text();
        String type = element.select("td.td_cate > a").text();
        String commentCountStr = element.select("td.td_subject > a > span.cnt_cmt").text();
        int commentCount = 0;
        if (!"".equals(commentCountStr)) {
          commentCount = Integer.parseInt(commentCountStr);
        }
        String recommendationCountStr = element.select("td.td_num_g > span:nth-child(1)").text();
        int recommendationCount = Integer.parseInt(recommendationCountStr);
        String disLikeCountStr = element.select("td.td_num_g > span:nth-child(2)").text();
        int disLikeCount = Integer.parseInt(disLikeCountStr);
        String originClickCountStr = element.select("td:nth-child(7)").text();
        int originClickCount = Integer.parseInt(originClickCountStr);
        PostStatus status = PostStatus.READY;
        if (!element.select("td.td_subject > a > a[style=color:#ACACAC]").isEmpty()) {
          status = PostStatus.COMP;
        }
        String originIdStr = element.select("td:nth-child(1)").text();
        Long originId = Long.parseLong(originIdStr);

        postsUpdate(boardId, siteId, posts, writer, title, type, commentCount, recommendationCount, disLikeCount, originClickCount, status, originId);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return posts;
  }

  private void postsUpdate(Long boardId, Long siteId, List<Post> posts, String writer, String title, String type, int commentCount, int recommendationCount, int disLikeCount, int originClickCount, PostStatus status, Long originId) {
    Post postByOriginId = postService.findOneByOriginId(originId, boardId, siteId);
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
    if (postByOriginId != null) {
      postByOriginId.update(post);//이렇게만 해도 수정 되서 저장 할 post 에서는 제외
    } else {
//      post.setBoard(board); // TODO 이렇게 했다가 Post Servie 계층으로 바꿔서 진행해봄
      posts.add(post);
    }
  }

  private Document documentByUrl(String boardParam, int pageNum, String listUrlFormat) throws IOException {
    return Jsoup.connect(String.format(listUrlFormat, boardParam, pageNum))
            .userAgent(userAgent)
            .header("Referer", String.format(listUrlFormat, boardParam, pageNum + 1))
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Accept-Encoding", "gzip, deflate, br")
            .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
            .get();
  }

}

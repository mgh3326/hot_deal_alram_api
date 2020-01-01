package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import me.khmoon.hot_deal_alarm_api.repository.PageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawlingService {
  private final PageRepository pageRepository;
  private final ApplicationProperties applicationProperties;
  private String ppomppuListUrlFormat;
  private String userAgent;

  @PostConstruct
  public void init() {
    ppomppuListUrlFormat = applicationProperties.getPpomppu().getUrl().getList();
    userAgent = applicationProperties.getUserAgent();
  }

  public Post Parse(Long pageId) {
    Page page = pageRepository.findOne(pageId);
    Board board = page.getBoard();
    String boardParam = board.getBoardParam();
    int pageNum = page.getPageNum();
    String ppomppuListUrl = String.format(ppomppuListUrlFormat, boardParam, pageNum);
    try {
      Document doc = Jsoup.connect(ppomppuListUrl)
              .userAgent(userAgent)
              .header("Referer", String.format(ppomppuListUrlFormat, boardParam, pageNum + 1))
              .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
              .header("Content-Type", "application/x-www-form-urlencoded")
              .header("Accept-Encoding", "gzip, deflate")
              .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
              .get();
      System.out.println("doc = " + doc);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Post.builder()
            .build();
  }
}

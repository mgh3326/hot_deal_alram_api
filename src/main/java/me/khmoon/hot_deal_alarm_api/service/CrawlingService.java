package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import me.khmoon.hot_deal_alarm_api.repository.PageRepository;
import me.khmoon.hot_deal_alarm_api.repository.PostRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawlingService {
  final PostRepository postRepository;
  final BoardRepository boardRepository;
  final PageRepository pageRepository;
  @Value("${ppomppu.url.list}")
  private String ppomppuListUrlFormat;

  public Post Parse(Long pageId) {
    Page page = pageRepository.findOne(pageId);
    Board board = page.getBoard();
    String boardParam = board.getBoardParam();
    int pageNum = page.getPageNum();
    String ppomppuListUrl = String.format(ppomppuListUrlFormat, boardParam, pageNum);
    try {
      Document doc = Jsoup.connect(ppomppuListUrl).get();
      System.out.println("doc = " + doc);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Post.builder()
            .build();
  }
}

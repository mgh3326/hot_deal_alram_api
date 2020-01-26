package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchedulerService {
  private final PageService pageService;
  private final PostService postService;
  private final CrawlingService crawlingService;

  @Transactional
  public void updatePostFromPage(Long siteId) throws InterruptedException {
    log.info(String.format("SiteId : %d updatePostFromPage start", siteId));
    Long pageId = pageService.findRedisPageIdBySiteId(siteId);
    Random random = new Random();
    Thread.sleep(random.nextInt(1000) + 1000);
    if (pageId == null) {
      List<Page> pages = pageService.findAllForRefreshingBySiteId(siteId);
      if (pages.size() == 0) {
        Thread.sleep(random.nextInt(4000) + 2000);// 마지막꺼를 보고 재우는것도 방법이겠다.
      } else {
        for (Page page : pages) {
          page.updatePageRefreshingDateTime(); //TODO bulk update 이용해주면 되겠다.
          pageService.saveRedis(siteId, page.getId());//Save All을 쓰는게 이득일까?
        }

      }

    } else {
      Page page = pageService.findOneWithBoard(pageId);
      log.info(String.format("SiteId : %d updatePageRefreshingDateTime start", siteId));
      List<Post> posts = crawlingService.parse(page.getId());
      postService.saveParsed(page.getBoard().getId(), posts);
    }
  }


}

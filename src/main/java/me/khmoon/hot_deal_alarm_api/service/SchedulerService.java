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
  public void updatePostFromPage(Long siteId) {
    log.info(String.format("SiteId : %d updatePostFromPage start", siteId));
    Page page = pageService.findOneForRefreshingBySiteId(siteId);
    if (page == null) {
      try {
        Random random = new Random();
        Thread.sleep(random.nextInt(5000) + 3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      try {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000) + 1000);
        page.updatePageRefreshingDateTime();
        log.info(String.format("SiteId : %d updatePageRefreshingDateTime start", siteId));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      List<Post> posts = crawlingService.parse(page.getId());
      postService.saveParsed(page.getBoard().getId(), posts);
    }
  }


}

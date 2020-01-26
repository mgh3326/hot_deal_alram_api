package me.khmoon.hot_deal_alarm_api.programmableScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import me.khmoon.hot_deal_alarm_api.service.SchedulerService;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProgrammableScheduler {
  private final SiteService siteService;
  private final PageService pageService;
  private final SchedulerService schedulerService;
  private static ThreadPoolTaskScheduler[] schedulerArray;

  public void stopScheduler() { // TODO 데이터 변동에 따라서 이게 재 실행 되야할 것 같다. (Annotaion으로 구성하면 좋을것 같다.)
    if (schedulerArray == null)
      return;
    for (ThreadPoolTaskScheduler scheduler : schedulerArray) {
      scheduler.shutdown();
    }
    //여기 new 해준걸 없앨 필요는 없을까?
  }

  public void startScheduler() {
    stopScheduler();
    List<Site> sites = siteService.findSites();
    int size = sites.size();
    System.out.println("siteSize = " + size);
    schedulerArray = new ThreadPoolTaskScheduler[size];
    for (int i = 0; i < size; i++) {
      schedulerArray[i] = new ThreadPoolTaskScheduler();
      schedulerArray[i].initialize();
      // 스케쥴러가 시작되는 부분
      schedulerArray[i].schedule(getRunnable(sites.get(i).getId()), getTrigger(sites.get(i).getId()));
    }

  }

  private Runnable getRunnable(Long siteId) {
    return () -> {
      try {
        schedulerService.updatePostFromPage(siteId);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
  }

  private Trigger getTrigger(Long siteId) {
    // 작업 주기 설정
    Long pageSize = pageService.countBySiteId(siteId);
    log.info(String.format("pagesSize = %d sideId = %d", pageSize, siteId));
    return new PeriodicTrigger(60 , TimeUnit.SECONDS);
  }
}

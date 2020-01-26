package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PageRedisRepository {
  private final PageRepository pageRepository;
  private final SiteService siteService;
  @Resource(name = "redisTemplate")
  private ListOperations<SiteName, Long> listOperations;

  public Long findPageIdBySiteId(Long siteId) {
    Site site = siteService.findOne(siteId);
    SiteName siteName = site.getSiteName();
    return listOperations.leftPop(siteName);
  }

  public void save(Long siteId, Long pageId) {
    Site site = siteService.findOne(siteId);
    SiteName siteName = site.getSiteName();
    save(siteName, pageId);
  }

  public void save(SiteName siteName, Long pageId) {
    listOperations.rightPush(siteName, pageId);
  }

  public void saveAll(Long siteId, List<Long> pageIds) {
    Site site = siteService.findOne(siteId);
    SiteName siteName = site.getSiteName();
    saveAll(siteName, pageIds);
  }

  public void saveAll(SiteName siteName, List<Long> pageIds) {
    listOperations.rightPushAll(siteName, pageIds);
  }
}

package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
@RequiredArgsConstructor
public class PageRedisRepository {
  private final SiteService siteService;
  private final RedisTemplate<String, String> redisTemplate;
  @Resource(name = "redisTemplate")
  private ListOperations<String, String> listOperations;

  public Long findPageIdBySiteId(Long siteId) {
    Site site = siteService.findOne(siteId);
    SiteName siteName = site.getSiteName();
    String pop = listOperations.leftPop(siteName.toString());
    if (pop == null) {
      return null;
    }
    return Long.valueOf(pop);
  }

  public void save(Long siteId, Long pageId) {
    Site site = siteService.findOne(siteId);
    SiteName siteName = site.getSiteName();
    save(siteName.toString(), pageId);
  }

  public void save(String siteNameStr, Long pageId) {
    listOperations.rightPush(siteNameStr, pageId.toString());
  }

//  public void saveAll(Long siteId, List<Long> pageIds) {
//    Site site = siteService.findOne(siteId);
//    SiteName siteName = site.getSiteName();
//    saveAll(siteName.toString(), pageIds);
//  }
//
//  public void saveAll(String siteNameStr, List<Long> pageIds) {
//    listOperations.rightPushAll(siteNameStr, pageIds);
//  }

  public void delete(Long siteId) {
    Site site = siteService.findOne(siteId);
    SiteName siteName = site.getSiteName();
    delete(siteName.toString());
  }

  public void delete(String siteNameStr) {
    redisTemplate.delete(siteNameStr);
  }
}

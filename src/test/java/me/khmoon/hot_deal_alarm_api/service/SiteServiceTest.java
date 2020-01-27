package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.common.BaseServiceTest;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteKoreanName;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SiteServiceTest extends BaseServiceTest {
  @Autowired
  private SiteService siteService;
  @Autowired
  private ApplicationProperties applicationProperties;

  private String siteListUrl;
  private String siteViewUrl;

  @PostConstruct
  public void init() {
    siteListUrl = applicationProperties.getPpomppu().getUrl().getList();
    siteViewUrl = applicationProperties.getPpomppu().getUrl().getView();
  }


  private SiteName siteName = SiteName.PPOMPPU;
  private SiteKoreanName siteKoreanName = SiteKoreanName.뽐뿌;


  @Test
  @DisplayName("추가하기")
  void add() {
    Site site = Site.builder().siteName(siteName).build();
    siteService.add(site);
    Site site1 = siteService.findOne(site.getId());
    assertEquals(site1.getSiteName(), siteName);
    assertEquals(site1.getSiteListUrl(), siteListUrl);
    assertEquals(site1.getSiteViewUrl(), siteViewUrl);
    assertEquals(siteKoreanName, site1.getSiteKoreanName());
  }

  @Test
  @DisplayName("유니큐 조건")
  void uniqueViolation() {
    Site site = Site.builder().siteName(siteName).build();
    siteService.add(site);
    Site site2 = Site.builder().siteName(siteName).build();
    assertThrows(DataIntegrityViolationException.class, () -> siteService.add(site2));
  }

  @Test
  @DisplayName("url format test")
  void ppomppuUrlStringFormat() {
    String ppomppuListUrl = String.format(siteListUrl, "ppomppu", 1);
    String ppomppuViewUrl = String.format(siteViewUrl, "ppomppu", 339349);
    assertEquals(ppomppuListUrl, "https://m.ppomppu.co.kr/new/bbs_list.php?id=ppomppu&page=1");
    assertEquals(ppomppuViewUrl, "https://m.ppomppu.co.kr/new/bbs_view.php?id=ppomppu&no=339349");
  }

  @Test
  void checkProperty() {
    assertEquals(siteListUrl, applicationProperties.getPpomppu().getUrl().getList());
    assertEquals(siteViewUrl, applicationProperties.getPpomppu().getUrl().getView());
  }
}
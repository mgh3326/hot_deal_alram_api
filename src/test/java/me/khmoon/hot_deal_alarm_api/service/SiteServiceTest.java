package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test") // Like this
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class SiteServiceTest {
  @Autowired
  private SiteService siteService;
  @Autowired
  private SiteRepository siteRepository;
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


  @Test
  @DisplayName("추가하기")
  void add() {
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    Site site1 = siteRepository.findOne(site.getId());
    assertEquals(site1.getSiteName(), siteName);
    assertEquals(site1.getSiteListUrl(), siteListUrl);
    assertEquals(site1.getSiteViewUrl(), siteViewUrl);
  }

  @Test
  @DisplayName("유니큐 조건")
  void unique_violation() {
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    Site site2 = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    assertThrows(DataIntegrityViolationException.class, () -> siteService.add(site2));
  }

  @Test
  @DisplayName("url format test")
  void ppomppu_url_string_format() {
    String ppomppuListUrl = String.format(siteListUrl, "ppomppu", 1);
    String ppomppuViewUrl = String.format(siteViewUrl, "ppomppu", 339349);
    assertEquals(ppomppuListUrl, "http://m.ppomppu.co.kr/new/bbs_list.php?id=ppomppu&page=1");
    assertEquals(ppomppuViewUrl, "http://m.ppomppu.co.kr/new/bbs_view.php?id=ppomppu&no=339349");
  }

  @Test
  void check_property() {
    assertEquals(siteListUrl, applicationProperties.getPpomppu().getUrl().getList());
    assertEquals(siteViewUrl, applicationProperties.getPpomppu().getUrl().getView());
  }
}
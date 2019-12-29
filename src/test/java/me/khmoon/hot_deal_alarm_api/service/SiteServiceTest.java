package me.khmoon.hot_deal_alarm_api.service;

import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Like this
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class SiteServiceTest {
  @Autowired
  SiteService siteService;
  @Autowired
  SiteRepository siteRepository;
  @Value("${ppomppu.url.list}")
  private String siteListUrl;
  @Value("${ppomppu.url.view}")
  private String siteViewUrl;

  @Test
  void add() {
    SiteName siteName = SiteName.PPOMPPU;
    Site site = Site.builder().siteName(siteName).siteListUrl(siteListUrl).siteViewUrl(siteViewUrl).build();
    siteService.add(site);
    Site site1 = siteRepository.findOne(site.getId());
    assertEquals(site1.getSiteName(), siteName);
    assertEquals(site1.getSiteListUrl(), siteListUrl);
    assertEquals(site1.getSiteViewUrl(), siteViewUrl);
  }

  @Test
  void ppomppu_url_string_format() {
    String ppomppuListUrl = String.format(siteListUrl, "ppomppu", 1);
    String ppomppuViewUrl = String.format(siteViewUrl, "ppomppu", 339349);
    assertEquals(ppomppuListUrl, "http://m.ppomppu.co.kr/new/bbs_list.php?id=ppomppu&page=1");
    assertEquals(ppomppuViewUrl, "http://m.ppomppu.co.kr/new/bbs_view.php?id=ppomppu&no=339349");
  }
}
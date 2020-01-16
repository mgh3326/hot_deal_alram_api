package me.khmoon.hot_deal_alarm_api;


import me.khmoon.hot_deal_alarm_api.common.BaseServiceTest;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import me.khmoon.hot_deal_alarm_api.service.InitService;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InitDbTest extends BaseServiceTest {
  @Autowired
  protected SiteRepository siteRepository;
  @Autowired
  private PageService pageService;
  @Autowired
  protected BoardRepository boardRepository;
  @Autowired
  private InitService initService;

  @Test
  void init() {
    // init
    int pageNumSize = 5;
    initService.dbInitPpomppu(1, pageNumSize);
    initService.dbInitDealbada(1, pageNumSize);
    initService.dbInitClien(0, pageNumSize);
    initService.dbInitCoolenjoy(0, pageNumSize);
    List<Page> pages = pageService.findAll();
    assertEquals(6 * pageNumSize, pages.size());

  }
}
package me.khmoon.hot_deal_alarm_api;


import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")

@SpringBootTest
@Transactional
class InitDbTest {
  @Autowired
  private InitDb.InitService initService;
  @Autowired
  private PageService pageService;

  @Test
  void init() {
    // init
    initService.dbInitPpomppu();
    initService.dbInitDealbada();
    initService.dbInitClien();
    initService.dbInitCoolenjoy();
    List<Page> pages = pageService.findAll();
    assertEquals(6 * 5, pages.size());

  }
}
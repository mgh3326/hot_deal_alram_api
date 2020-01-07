package me.khmoon.hot_deal_alarm_api;


import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
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
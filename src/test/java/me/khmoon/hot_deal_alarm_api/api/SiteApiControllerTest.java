package me.khmoon.hot_deal_alarm_api.api;

import me.khmoon.hot_deal_alarm_api.common.BaseControllerTest;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.service.InitService;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SiteApiControllerTest extends BaseControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  private PageService pageService;
  @Autowired
  private InitService initService;

  @Test
  void sites() throws Exception {
    int pageNumSize = 5;
    initService.dbInitPpomppu(1, pageNumSize);
    initService.dbInitDealbada(1, pageNumSize);
    initService.dbInitClien(0, pageNumSize);
    initService.dbInitCoolenjoy(0, pageNumSize);
    List<Page> pages = pageService.findAll();
    assertEquals(6 * pageNumSize, pages.size());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/sites/").contentType(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(4))
            .andExpect(jsonPath("$[0].siteKoreanName").value("뽐뿌"))
            .andExpect(jsonPath("$[0].boards").exists())
            .andExpect(jsonPath("$[0].boards[0]").exists())
            .andExpect(jsonPath("$[0].boards[0].boardKoreanName").exists())
            .andExpect(jsonPath("$[1].siteKoreanName").value("딜바다"))
            .andExpect(jsonPath("$[1].boards").exists())
            .andExpect(jsonPath("$[1].boards[0]").exists())
            .andExpect(jsonPath("$[1].boards[0].boardKoreanName").exists())
            .andExpect(jsonPath("$[2].siteKoreanName").value("클리앙"))
            .andExpect(jsonPath("$[2].boards").exists())
            .andExpect(jsonPath("$[2].boards[0]").exists())
            .andExpect(jsonPath("$[2].boards[0].boardKoreanName").exists())
            .andExpect(jsonPath("$[3].siteKoreanName").value("쿨엔조이"))
            .andExpect(jsonPath("$[3].boards").exists())
            .andExpect(jsonPath("$[3].boards[0]").exists())
            .andExpect(jsonPath("$[3].boards[0].boardKoreanName").exists())
    ;
  }
}
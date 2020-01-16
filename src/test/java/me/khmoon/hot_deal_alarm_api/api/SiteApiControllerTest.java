package me.khmoon.hot_deal_alarm_api.api;

import me.khmoon.hot_deal_alarm_api.InitDb;
import me.khmoon.hot_deal_alarm_api.common.BaseControllerTest;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.service.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
class SiteApiControllerTest extends BaseControllerTest {
  @Autowired
  private InitDb.InitService initService;
  @Autowired
  private PageService pageService;

  @Test
  void sites() throws Exception {
    int pageNumSize = 5;
    initService.dbInitPpomppu(1, pageNumSize);
    initService.dbInitDealbada(1, pageNumSize);
    initService.dbInitClien(0, pageNumSize);
    initService.dbInitCoolenjoy(0, pageNumSize);
    List<Page> pages = pageService.findAll();
    assertEquals(6 * pageNumSize, pages.size());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/sites/").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(4))
            .andExpect(jsonPath("$[0].siteName").value("PPOMPPU"))
            .andExpect(jsonPath("$[0].boards").exists())
            .andExpect(jsonPath("$[0].boards[0]").exists())
            .andExpect(jsonPath("$[0].boards[0].boardName").exists())
    ;
  }
}
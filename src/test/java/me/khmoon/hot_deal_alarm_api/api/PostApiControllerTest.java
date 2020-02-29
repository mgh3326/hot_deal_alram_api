package me.khmoon.hot_deal_alarm_api.api;

import me.khmoon.hot_deal_alarm_api.common.BaseControllerTest;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostApiControllerTest extends BaseControllerTest {
  @Autowired
  private InitService initService;
  @Autowired
  private PostService postService;
  @Autowired
  private PageService pageService;
  @Autowired
  private BoardService boardService;
  @Autowired
  private CrawlingService crawlingService;

  @Test
  void posts() throws Exception {
    Random random = new Random();
    int i = random.nextInt(4);
    assertThat(i >= 0 && i < 4);
    int pageNumSize = 5;
    if (i == 0) {
      initService.dbInitPpomppu(1, pageNumSize);
    } else if (i == 1) {
      initService.dbInitDealbada(1, pageNumSize);
    } else if (i == 2) {
      initService.dbInitClien(0, pageNumSize);
    } else if (i == 3) {
      initService.dbInitCoolenjoy(0, pageNumSize);
    }
    List<Board> boards = boardService.findAllWithSite();
    if (boards.size() > 0) {//CI 오류 날거 대비
      Board board = boards.get(0);

      List<Page> pages = pageService.findAll();
      if (boards.size() > 1) {
        pages = pages.stream().filter(a -> a.getBoard().getId().equals(board.getId())).collect(Collectors.toList());
      }
      for (Page page : pages) {
        List<Post> posts = crawlingService.parse(page.getId());
        postService.saveParsed(board.getId(), posts);
        Thread.sleep(random.nextInt(1000));
      }
      assertEquals(pageNumSize, pages.size());
      mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/")
              .param("boardId", board.getId().toString())
              .param("page", Integer.toString(0))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
              .andDo(print())
              .andExpect(status().isOk())
      ;
      mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/all")
              .param("page", Integer.toString(0))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
              .andDo(print())
              .andExpect(status().isOk())
      ;
    }

  }
}
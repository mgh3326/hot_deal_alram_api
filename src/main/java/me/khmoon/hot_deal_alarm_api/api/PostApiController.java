package me.khmoon.hot_deal_alarm_api.api;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.repository.post.query.PostAllRes;
import me.khmoon.hot_deal_alarm_api.repository.post.query.PostRes;
import me.khmoon.hot_deal_alarm_api.service.PostService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/posts", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class PostApiController {
  private final PostService postService;

  @GetMapping
  public PostRes postsByBoardId(@RequestParam(value = "boardId") Long boardId,
                                @RequestParam(value = "page", defaultValue = "0") int page) {
    return postService.findPostDtoAllByBoardId(boardId, page);
  }

  @GetMapping("/all")
  public PostAllRes postsAll(@RequestParam(value = "page", defaultValue = "0") int page) {
    return postService.findPostDtoAll(page);// 쿼리가 3번만 나가는게 신기하네 fetch join을 하지 않는게 오히려 효율적인거 같다.
  }
}

package me.khmoon.hot_deal_alarm_api.api;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.repository.post.query.PostRes;
import me.khmoon.hot_deal_alarm_api.service.PostService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
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
  public PostRes posts(@RequestParam(value = "boardId") Long boardId,
                       @RequestParam(value = "page", defaultValue = "0") int page) {
    return postService.findPostDtoAll(boardId, page);
  }
}

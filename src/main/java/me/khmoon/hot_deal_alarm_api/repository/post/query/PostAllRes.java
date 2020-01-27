package me.khmoon.hot_deal_alarm_api.repository.post.query;

import lombok.Data;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostAllRes {
  boolean isLast;
  List<PostAllDto> posts = new ArrayList<>();

  public PostAllRes(Slice<Post> postSlice) {
    List<Post> content = postSlice.getContent();
    this.posts = content.stream().map(PostAllDto::new).collect(Collectors.toList());
    this.isLast = postSlice.isLast(); // 이것도 담아 줘야겠네
  }
}

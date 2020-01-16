package me.khmoon.hot_deal_alarm_api.repository.post.query;

import lombok.Data;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostRes {
  boolean isLast;
  List<PostDto> posts = new ArrayList<>();

  public PostRes(Slice<Post> postSlice) {
    List<Post> content = postSlice.getContent();
    List<PostDto> postDtos = content.stream().map(PostDto::new).collect(Collectors.toList());
    this.isLast = postSlice.isLast(); // 이것도 담아 줘야겠네
    this.posts = postDtos;
  }
}

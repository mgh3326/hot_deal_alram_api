package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import me.khmoon.hot_deal_alarm_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final BoardRepository boardRepository;

  //페이지 추가
  @Transactional
  public Long savePost(Long boardId, Post post) {
    Board board = boardRepository.findOne(boardId);
    post.setBoard(board);//TODO Save랑 Update 분리가 필요하겠다.
    postRepository.save(post);
    return post.getId();
  }

  @Transactional
  public List<Long> savePostAll(Long boardId, List<Post> posts) {
    Board board = boardRepository.findOne(boardId);
    List<Long> result = new ArrayList<>();
    for (Post post : posts) {
      post.setBoard(board);
      result.add(postRepository.save(post));
    }
    return result;
  }

  public List<Post> findAll() {
    return postRepository.findAll();
  }
}
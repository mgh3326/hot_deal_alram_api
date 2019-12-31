package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.repository.BoardRepository;
import me.khmoon.hot_deal_alarm_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
  final PostRepository postRepository;
  final BoardRepository boardRepository;

  //페이지 추가
  @Transactional
  public Long savePost(Long boardId, Post post) {
    Board board = boardRepository.findOne(boardId);
    post.setBoard(board);
    postRepository.save(post);
    return post.getId();
  }
}

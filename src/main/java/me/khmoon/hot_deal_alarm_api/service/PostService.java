package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final BoardService boardService;

  //페이지 추가
  @Transactional
  public Long savePost(Post post) {
    postRepository.save(post);
    return post.getId();
  }

  public Long savePostWithBoardId(Post post, Long boardId) {
    Board board = boardService.findOne(boardId);
    post.setBoard(board);
    return savePost(post);
  }

  @Transactional
  public List<Long> savePostAll(List<Post> posts) {
    return postRepository.saveAll(posts);
  }

  public List<Long> savePostAllWithBoardId(List<Post> posts, Long boardId) {
    Board board = boardService.findOne(boardId);
    for (Post post : posts) {
      post.setBoard(board);
    }
    return savePostAll(posts);
  }

  public Post findOne(Long id) {
    return postRepository.findOne(id);
  }

  public List<Post> findAll() {
    return postRepository.findAll();
  }

  public Post findOneByOriginId(Long postOriginId, Long boardId, Long siteId) {
    return postRepository.findOneByOriginId(postOriginId, boardId, siteId);
  }

}
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
  public List<Post> savePostAll(List<Post> posts) {
    return postRepository.saveAll(posts);
  }

  @Transactional
  public List<Post> savePostAllWithBoardId(List<Post> posts, Long boardId) {
    Board board = boardService.findOne(boardId);
    for (Post post : posts) {
      post.setBoard(board);
    }
    return savePostAll(posts);
  }

  public Post findOne(Long id) {
    return postRepository.findById(id).orElseThrow();
  }

  public List<Post> findAll() {
    return postRepository.findAll();
  }

  public Post findOneByOriginId(Long postOriginId, Long boardId) {
    return postRepository.findOneByOriginId(postOriginId, boardId).orElse(null); //TODO null 말고 Optional을 사용해보도록 하자
  }

  public List<Post> findInOriginIds(List<Long> postOriginIds, Long boardId) {
    return postRepository.findInOriginIds(postOriginIds, boardId);
  }

}
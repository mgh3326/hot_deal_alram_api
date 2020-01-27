package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import me.khmoon.hot_deal_alarm_api.domain.post.PostStatus;
import me.khmoon.hot_deal_alarm_api.repository.PostRepository;
import me.khmoon.hot_deal_alarm_api.repository.post.query.PostQueryRepository;
import me.khmoon.hot_deal_alarm_api.repository.post.query.PostRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final PostQueryRepository postQueryRepository;
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
    if (postOriginIds.size() > 0) {
      return postRepository.findInOriginIds(postOriginIds, boardId);
    } else {
      return new ArrayList<>();
    }
  }


  public void postsAdd(List<Post> posts, String writer, String title, String type, int commentCount, int recommendationCount, int disLikeCount, int originClickCount, PostStatus status, Long originId) {
    Post post = Post.builder()
            .postTitle(title)
            .postType(type)
            .postWriter(writer)
            .postCommentCount(commentCount)
            .postOriginClickCount(originClickCount)
            .postRecommendationCount(recommendationCount)
            .postDisLikeCount(disLikeCount)
            .postStatus(status)
            .postOriginId(originId)
            .build();
    posts.add(post);
  }

  @Transactional
  public List<Post> postUpdatePossible(Long boardId, List<Post> posts) {
    List<Long> postOriginIds = posts.stream().map(Post::getPostOriginId).collect(Collectors.toList());
    List<Post> postInOriginIds = findInOriginIds(postOriginIds, boardId);
    Set<Long> postInOriginIdSet = postInOriginIds.stream().map(Post::getPostOriginId).collect(Collectors.toSet());
    Map<Long, Post> collect = posts.stream()
            .filter(post -> postInOriginIdSet.contains(post.getPostOriginId()))
            .collect(Collectors.toMap(Post::getPostOriginId, post -> post));
    posts = posts.stream().filter(post -> !postInOriginIdSet.contains(post.getPostOriginId())).collect(Collectors.toList());// TODO filter 부분이 아주 유사한데 이부분을 합칠수 없을까?

    for (Post postInOriginId : postInOriginIds) {
      Post post = collect.get(postInOriginId.getPostOriginId());
      postInOriginId.update(post);//이렇게만 해도 수정 되서 저장 할 post 에서는 제외
    }
    return posts;
  }

  public PostRes findPostDtoAllByBoardId(Long boardId, int page) {
    return postQueryRepository.findPostDtoAllByBoardId(boardId, page);

  }

  public void saveParsed(Long boardId, List<Post> posts) {
    posts = postUpdatePossible(boardId, posts);
    savePostAllWithBoardId(posts, boardId);
  }

  public PostRes findPostDtoAll(int page) {
    return postQueryRepository.findPostDtoAll(page);
  }
}
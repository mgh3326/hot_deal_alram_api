package me.khmoon.hot_deal_alarm_api.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.post.Post;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPost {
  @Id
  @GeneratedValue
  @Column(name = "user_post_id")
  private Long id;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "post_id")
  private Post post;
  @ColumnDefault("0")
  private int postClickCount;
}

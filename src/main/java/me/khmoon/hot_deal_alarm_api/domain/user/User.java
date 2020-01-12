package me.khmoon.hot_deal_alarm_api.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;

  @OneToMany(mappedBy = "user")
  private List<UserPost> userPosts = new ArrayList<>();
}

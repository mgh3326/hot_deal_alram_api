package me.khmoon.hot_deal_alarm_api.domain.site;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(indexes = {@Index(name = "siteName", columnList = "siteName", unique = true)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "site_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private SiteName siteName;

  private String siteListUrl;
  private String siteViewUrl;

  @OneToMany(mappedBy = "site")
  private List<Board> boards = new ArrayList<>();

  @Builder
  public Site(SiteName siteName, String siteViewUrl, String siteListUrl) {
    this.siteName = siteName;
    this.siteViewUrl = siteViewUrl;
    this.siteListUrl = siteListUrl;
  }
}

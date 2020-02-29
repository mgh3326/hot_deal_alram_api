package me.khmoon.hot_deal_alarm_api.domain.site;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
  @NotNull
  @Enumerated(EnumType.STRING)
  private SiteName siteName;
  @NotNull
  @Enumerated(EnumType.STRING)
  private SiteKoreanName siteKoreanName;
  @NotBlank
  private String siteListUrl;
  @NotBlank
  private String siteViewUrl;

  @OneToMany(mappedBy = "site")
  private List<Board> boards = new ArrayList<>();

  @Builder
  public Site(SiteName siteName) {
    this.siteName = siteName;
    switch (siteName) {
      case PPOMPPU:
        this.siteKoreanName = SiteKoreanName.뽐뿌;
        break;
      case DEALBADA:
        this.siteKoreanName = SiteKoreanName.딜바다;
        break;
      case CLIEN:
        this.siteKoreanName = SiteKoreanName.클리앙;
        break;
      case COOLENJOY:
        this.siteKoreanName = SiteKoreanName.쿨엔조이;
        break;
    }
  }

  public void addUrl(String siteViewUrl, String siteListUrl) {//이럼 변경이 되려나
    this.siteViewUrl = siteViewUrl;
    this.siteListUrl = siteListUrl;
  }
}

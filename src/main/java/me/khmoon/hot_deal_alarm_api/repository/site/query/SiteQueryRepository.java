package me.khmoon.hot_deal_alarm_api.repository.site.query;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SiteQueryRepository {
  private final SiteRepository siteRepository;

  public List<SiteDto> findSiteDtoAllIn() {//쿼리가 site한번, board 한번 총 2번의 쿼리가 나간다는 것이 단점인것 같다.
    List<Site> result = siteRepository.findAll();// TODO 이게 효율적일까 distinct 쿼리(findSiteDtoAllDistinct)가 효율적일까?
    return result.stream()
            .map(SiteDto::new)
            .collect(Collectors.toList());
  }

  public List<SiteDto> findSiteDtoAllDistinct() {//아마 distinct를 사용하여도 되는데 paging을 하지 못한다는 단점을 갖은 것으로 생각된다.
    List<Site> result = siteRepository.findAllWithBoard();
    return result.stream()
            .map(SiteDto::new)
            .collect(Collectors.toList());
  }
}

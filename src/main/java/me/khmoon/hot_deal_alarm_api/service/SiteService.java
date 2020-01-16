package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import me.khmoon.hot_deal_alarm_api.repository.site.query.SiteDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteService {
  private final SiteRepository siteRepository;

  //게시판 추가
  @Transactional
  public Long add(Site site) {
    siteRepository.save(site);
    return site.getId();
  }

  public Site findOneBySiteName(SiteName siteName) {
    return siteRepository.findBySiteName(siteName).orElseThrow();
  }

  public Site findOne(Long id) {
    return siteRepository.findById(id).orElseThrow();
  }

  public List<Site> findSites() {
    return siteRepository.findAll();
  }

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
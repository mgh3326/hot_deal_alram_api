package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import me.khmoon.hot_deal_alarm_api.repository.site.query.SiteDto;
import me.khmoon.hot_deal_alarm_api.repository.site.query.SiteQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteService {
  private final SiteRepository siteRepository;
  private final SiteQueryRepository siteQueryRepository;
  private final ApplicationProperties applicationProperties;

  //게시판 추가
  @Transactional
  public Long add(Site site) {
    switch (site.getSiteName()) {
      case PPOMPPU:
        site.addUrl(applicationProperties.getPpomppu().getUrl().getView(), applicationProperties.getPpomppu().getUrl().getList());
        break;
      case DEALBADA:
        site.addUrl(applicationProperties.getDealbada().getUrl().getView(), applicationProperties.getDealbada().getUrl().getList());
        break;
      case CLIEN:
        site.addUrl(applicationProperties.getClien().getUrl().getView(), applicationProperties.getClien().getUrl().getList());
        break;
      case COOLENJOY:
        site.addUrl(applicationProperties.getCoolenjoy().getUrl().getView(), applicationProperties.getCoolenjoy().getUrl().getList());
        break;
    }
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

  public List<SiteDto> findSiteDtoAllIn() {
    return siteQueryRepository.findSiteDtoAllIn();
  }

  public List<SiteDto> findSiteDtoAllDistinct() {
    return siteQueryRepository.findSiteDtoAllDistinct();
  }
}
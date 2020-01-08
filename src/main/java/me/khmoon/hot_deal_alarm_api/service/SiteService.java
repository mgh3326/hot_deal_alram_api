package me.khmoon.hot_deal_alarm_api.service;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import me.khmoon.hot_deal_alarm_api.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    return siteRepository.findOneBySiteName(siteName);
  }

  public Site findOne(Long id) {
    return siteRepository.findOne(id);
  }

  public List<Site> findAll() {
    return siteRepository.findAll();
  }
}

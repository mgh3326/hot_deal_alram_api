package me.khmoon.hot_deal_alarm_api.api;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.repository.site.query.SiteDto;
import me.khmoon.hot_deal_alarm_api.repository.site.query.SiteQueryRepository;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class SiteApiController {
  private final SiteService siteService;
  private final SiteQueryRepository siteQueryRepository;

  @GetMapping("/api/sites")
  public List<SiteDto> sites() {
    return siteQueryRepository.findSiteDtoAllDistinct();
  }

}

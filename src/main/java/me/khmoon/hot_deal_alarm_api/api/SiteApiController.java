package me.khmoon.hot_deal_alarm_api.api;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.repository.site.query.SiteDto;
import me.khmoon.hot_deal_alarm_api.service.SiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/sites")
@RequiredArgsConstructor
public class SiteApiController {
  private final SiteService siteService;

  @GetMapping
  public ResponseEntity<?> sites() {
    List<SiteDto> siteDtoAllDistinct = siteService.findSiteDtoAllDistinct();
    return ResponseEntity.ok(siteDtoAllDistinct);
  }

}

package me.khmoon.hot_deal_alarm_api;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.service.InitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile({"beta", "dev", "local"})
@RequiredArgsConstructor
public class InitDb {
  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInitPpomppu(1, 5);
    initService.dbInitDealbada(1, 5);
    initService.dbInitClien(0, 5);
    initService.dbInitCoolenjoy(0, 5);
  }


}
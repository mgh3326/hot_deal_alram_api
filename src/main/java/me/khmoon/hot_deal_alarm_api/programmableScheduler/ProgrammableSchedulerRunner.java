package me.khmoon.hot_deal_alarm_api.programmableScheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component//runner가 항상 실행 되는게 맞을까
@RequiredArgsConstructor
public class ProgrammableSchedulerRunner {
  final ProgrammableScheduler scheduler;


  @PostConstruct
  public void runSchedule() {
    // called by somewhere
    scheduler.startScheduler();
  }
}

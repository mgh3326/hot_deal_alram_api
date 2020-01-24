package me.khmoon.hot_deal_alarm_api.programmableScheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ProgrammableSchedulerRunner {
  final ProgrammableScheduler scheduler;


  @PostConstruct
  public void runSchedule() {
    // called by somewhere
    scheduler.startScheduler();
  }
}

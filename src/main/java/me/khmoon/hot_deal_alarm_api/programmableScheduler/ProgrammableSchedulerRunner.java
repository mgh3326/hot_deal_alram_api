package me.khmoon.hot_deal_alarm_api.programmableScheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProgrammableSchedulerRunner {
  @Autowired
  ProgrammableScheduler scheduler;

  @PostConstruct
  public void runSchedule() {
    // called by somewhere
    scheduler.startScheduler();
  }
}

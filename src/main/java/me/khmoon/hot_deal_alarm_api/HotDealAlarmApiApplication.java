package me.khmoon.hot_deal_alarm_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class HotDealAlarmApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(HotDealAlarmApiApplication.class, args);
  }

}

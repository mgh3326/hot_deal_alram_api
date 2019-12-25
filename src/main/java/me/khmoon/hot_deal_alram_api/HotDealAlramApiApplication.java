package me.khmoon.hot_deal_alram_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class HotDealAlramApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(HotDealAlramApiApplication.class, args);
  }

}

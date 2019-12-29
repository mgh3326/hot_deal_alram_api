package me.khmoon.hot_deal_alarm_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotDealAlarmApiApplicationTests {

  @Test
  void contextLoads() {
  }

  @Test
  public void main() {

    HotDealAlarmApiApplication.main(new String[]{});
    assertTrue(true, "main test");
  }
}

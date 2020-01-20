package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.common.BaseServiceTest;
import me.khmoon.hot_deal_alarm_api.domain.Point;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PointRedisRepositoryTest extends BaseServiceTest {
  @Autowired
  private PointRedisRepository pointRedisRepository;

  @AfterEach
  public void tearDown() throws Exception {
    pointRedisRepository.deleteAll();
  }

  @Test
  public void 기본_등록_조회기능() {
    //given
    String id = "mgh3326";
    LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
    Point point = Point.builder()
            .id(id)
            .amount(1000L)
            .refreshTime(refreshTime)
            .build();

    //when
    pointRedisRepository.save(point);

    //then
    Point savedPoint = pointRedisRepository.findById(id).get();
    assertThat(savedPoint.getAmount()).isEqualTo(1000L);
    assertThat(savedPoint.getRefreshTime()).isEqualTo(refreshTime);
  }

  @Test
  public void 수정기능() {
    //given
    String id = "jojoldu";
    LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
    pointRedisRepository.save(Point.builder()
            .id(id)
            .amount(1000L)
            .refreshTime(refreshTime)
            .build());

    //when
    Point savedPoint = pointRedisRepository.findById(id).get();
    savedPoint.refresh(2000L, LocalDateTime.of(2018, 6, 1, 0, 0));
    pointRedisRepository.save(savedPoint);

    //then
    Point refreshPoint = pointRedisRepository.findById(id).get();
    assertThat(refreshPoint.getAmount()).isEqualTo(2000L);
  }
}
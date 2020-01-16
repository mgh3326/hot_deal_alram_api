package me.khmoon.hot_deal_alarm_api.common;

import me.khmoon.hot_deal_alarm_api.propertiy.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class BaseServiceTest {
  @Autowired
  protected ApplicationProperties applicationProperties;
  @Autowired
  protected EntityManager em;


}

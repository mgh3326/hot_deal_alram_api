package me.khmoon.hot_deal_alarm_api.propertiy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("my-values")
@Getter
@Setter
public class ApplicationProperties {
  private String userAgent;
  private Ppomppu ppomppu;

  @Getter
  @Setter
  public static class Ppomppu {
    Url url;
    Param param;

    @Getter
    @Setter
    public static class Url {
      String list;
      String view;
    }

    @Getter
    @Setter
    public static class Param {
      String domestic;
      String overseas;
    }


  }
}

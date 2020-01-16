package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {
  Optional<Site> findBySiteName(SiteName siteName);

  @Query("select distinct s from Site s" +
          " join fetch s.boards b")
  List<Site> findAllWithBoard();
}

package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {
  @Query("select p from Page p where p.board.site.id=:siteId")
  List<Page> findAllBySiteId(@Param("siteId") Long siteId);

  @Query("select count(p) from Page p where p.board.site.id=:siteId")
  Long countBySiteId(@Param("siteId") Long siteId);

  @Query("select p from Page p where  p.pageRefreshingDateTime <= :now order by p.pageRefreshingDateTime")
  List<Page> findOneForRefreshing(@Param("now") LocalDateTime now, Pageable pageable);

  @Query("select p from Page p" +
          " join fetch p.board b" +
          " where b.site.id=:siteId and p.pageRefreshingDateTime <= :now order by p.pageRefreshingDateTime")
  List<Page> findOneForRefreshingBySiteId(@Param("siteId") Long siteId, @Param("now") LocalDateTime now, Pageable pageable); //TODO now 이거 지울수 있을까?
}

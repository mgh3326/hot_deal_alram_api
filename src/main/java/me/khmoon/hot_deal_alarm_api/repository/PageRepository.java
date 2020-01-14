package me.khmoon.hot_deal_alarm_api.repository;

import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {
  @Query("select p from Page p where p.board.site.id=:siteId")
  List<Page> findAllBySiteId(@Param("siteId") Long siteId);

  @Query("select count(p) from Page p where p.board.site.id=:siteId")
  Long countBySiteId(@Param("siteId") Long siteId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from Page p where  p.pageRefreshingDateTime <= :now order by p.pageRefreshingDateTime")
  List<Page> findOneForRefreshing(@Param("now") LocalDateTime now, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)//TODO Locking하지 않아도 되는 효율적인 방법이 있을까?
  @Query("select p from Page p" +
          " join fetch p.board b" +
          " where b.site.id=:siteId and p.pageRefreshingDateTime <= :now order by p.pageRefreshingDateTime")
    // redis를 이용하여 not in을 활용하는것도 성능 개선의 한 방법이겠다.
  List<Page> findOneForRefreshingBySiteId(@Param("siteId") Long siteId, @Param("now") LocalDateTime now, Pageable pageable); //TODO now 이거 지울수 있을까?

  @Query("select p from Page p" +
          " join fetch p.board b" +
          " join fetch b.site s" +
          " where p.id = :id")
  Optional<Page> findOneWithBoardWithSite(@Param("id") Long id);
}

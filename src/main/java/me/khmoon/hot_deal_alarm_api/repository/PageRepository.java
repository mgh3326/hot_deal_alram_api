package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

import static me.khmoon.hot_deal_alarm_api.helper.JpaResultHelper.getSingleResultOrNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PageRepository {
  private final EntityManager em;

  public void save(Page page) {
    if (page.getId() == null) {
      em.persist(page);
    } else {
      em.merge(page);
    }
  }

  public Page findOne(Long id) {
    return em.find(Page.class, id);
  }

  public List<Page> findAll() {
    return em.createQuery("select p from Page p", Page.class).getResultList();
  }

  public List<Page> findAllBySiteId(Long siteId) {
    return em.createQuery("select p from Page p where p.board.site.id=:siteId", Page.class)
            .setParameter("siteId", siteId)
            .getResultList();
  }

  public Long countBySiteId(Long siteId) {
    return em.createQuery("select count(p) from Page p where p.board.site.id=:siteId", Long.class)
            .setParameter("siteId", siteId)
            .getSingleResult();
  }

  public Page findOneForRefreshing() {
    Query query = em.createQuery("select p from Page p where  p.pageRefreshingDateTime <= :now order by p.pageRefreshingDateTime", Page.class)
            .setMaxResults(1)
            .setParameter("now", LocalDateTime.now());
    return (Page) getSingleResultOrNull(query);
  }

  public Page findOneForRefreshingBySiteId(Long siteId) { // Board를 eager 하게 가져오면 좋을것 같아 변경 (Board 쿼리가 별도로 한 번 더 나가는 현상 제거)
    Query query = em.createQuery("select p from Page p" +
            " join fetch p.board b" +
            " where b.site.id=:siteId and p.pageRefreshingDateTime <= :now order by p.pageRefreshingDateTime", Page.class)
            .setMaxResults(1)
            .setParameter("now", LocalDateTime.now())
            .setParameter("siteId", siteId);
    return (Page) getSingleResultOrNull(query);
  }

}

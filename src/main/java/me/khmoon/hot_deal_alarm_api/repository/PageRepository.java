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

  public Page findOneForRefreshing(int refreshingSecond) {
    LocalDateTime now = LocalDateTime.now();
    Query query = em.createQuery("select p from Page p where p.modifiedDateTime <= :now", Page.class)
            .setParameter("now", now.minusSeconds(refreshingSecond));//TODO 60초 (칼럼에서)를 받아서 넣어야 되는데
    return (Page) getSingleResultOrNull(query);
  }

  public Page findOneBySiteById(Long siteId) {

    Query query = em.createQuery("select p from Page p where p.board.site.id=:siteId and p.pageRefreshSecond < 60", Page.class)
            .setParameter("siteId", siteId);
    return (Page) getSingleResultOrNull(query);
  }

}

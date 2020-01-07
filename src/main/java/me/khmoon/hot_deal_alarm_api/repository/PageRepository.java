package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
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
}

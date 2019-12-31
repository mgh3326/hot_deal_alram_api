package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.page.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}

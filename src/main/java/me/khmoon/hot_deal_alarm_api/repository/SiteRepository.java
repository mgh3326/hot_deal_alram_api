package me.khmoon.hot_deal_alarm_api.repository;

import lombok.RequiredArgsConstructor;
import me.khmoon.hot_deal_alarm_api.domain.board.Board;
import me.khmoon.hot_deal_alarm_api.domain.board.BoardName;
import me.khmoon.hot_deal_alarm_api.domain.site.Site;
import me.khmoon.hot_deal_alarm_api.domain.site.SiteName;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class SiteRepository {
  private final EntityManager em;

  public void save(Site site) {
    em.persist(site);
  }

  public Site findOne(Long id) {
    return em.find(Site.class, id);
  }

  public Board findOneBySiteName(SiteName siteName) {
    return em.createQuery("select s from Site s where s.siteName=:siteName", Board.class).setParameter("siteName", siteName).getSingleResult();
  }
}

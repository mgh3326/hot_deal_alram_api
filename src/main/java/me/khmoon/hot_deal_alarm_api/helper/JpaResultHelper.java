package me.khmoon.hot_deal_alarm_api.helper;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

public class JpaResultHelper {
  public static Object getSingleResultOrNull(Query query) {
    var results = query.getResultList();
    if (results.isEmpty()) return null;
    else if (results.size() == 1) return results.get(0);
    throw new NonUniqueResultException();
  }
}

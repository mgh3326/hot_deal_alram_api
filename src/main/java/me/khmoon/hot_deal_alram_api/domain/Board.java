package me.khmoon.hot_deal_alram_api.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(indexes = {@Index(name = "boardName", columnList = "boardName")})
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String BoradUrl;
  @Enumerated(EnumType.STRING)
  private BoardName boardName;
  @UpdateTimestamp
  private Timestamp updateTime;
  @Basic(optional = false)
  @Column(name = "LastTouched", insertable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastTouched;
}

package me.khmoon.hot_deal_alram_api.repository;

import me.khmoon.hot_deal_alram_api.domain.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}

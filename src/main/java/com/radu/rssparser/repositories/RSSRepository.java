package com.radu.rssparser.repositories;

import com.radu.rssparser.models.RSSItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RSSRepository extends JpaRepository<RSSItem, Long> {
}
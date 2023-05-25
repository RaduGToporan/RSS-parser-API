package com.radu.rssparser.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "rss_items")
public class RSSItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="title")
    private String title;
    @Column(name="description")
    private String description;
    @Column(name="pub_date")
    private Date pubDate;

    public RSSItem(String title, String description, Date pubDate) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    public RSSItem() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getPubDate() {
        return pubDate;
    }
}

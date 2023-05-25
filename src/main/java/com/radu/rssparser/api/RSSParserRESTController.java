package com.radu.rssparser.api;

import com.radu.rssparser.models.RSSItem;
import com.radu.rssparser.repositories.RSSRepository;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController()
@RequestMapping("/news")
public class RSSParserRESTController {
    @Autowired
    private RSSRepository repository;

    private final String nytimesRSSURL = "https://rss.nytimes.com/services/xml/rss/nyt/World.xml";

    @GetMapping("/list")
    public List<RSSItem> getAllArticles(@RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "order", required = false) String order) {
        List<RSSItem> list = getAllRSSItems();
        boolean descending = "desc".equals(order);
        if ("date".equals(sort)) {
            if (descending) {
                list.sort(Comparator.comparing(RSSItem::getPubDate, Comparator.reverseOrder()));
            } else {
                list.sort(Comparator.comparing(RSSItem::getPubDate));
            }
        } else if ("title".equals(sort)) {
            if (descending) {
                list.sort(Comparator.comparing(RSSItem::getTitle, Comparator.reverseOrder()));
            } else {
                list.sort(Comparator.comparing(RSSItem::getTitle));
            }
        }
        return list;
    }

    @GetMapping("search")
    public List<RSSItem> findArticles(@RequestParam("keyword") String keyword) {
        List<RSSItem> list = getAllRSSItems();
        List<RSSItem> filteredList = new ArrayList<>();
        for (RSSItem item : list) {
            if (item.getTitle().contains(keyword) || item.getDescription().contains(keyword)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @PostMapping("persist")
    public String saveAll() {
        List<RSSItem> list = getAllRSSItems();
        repository.deleteAll();
        repository.saveAll(list);
        return "Success";
    }

    private List<RSSItem> getAllRSSItems() {
        try {
            URL feedSource = new URL(nytimesRSSURL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));

            List<RSSItem> list = new ArrayList<>();
            for (Object entry : feed.getEntries()) {
                SyndEntry syndEntry = (SyndEntry) entry;
                RSSItem item = new RSSItem(syndEntry.getTitle(), syndEntry.getDescription().getValue(), syndEntry.getPublishedDate());
                list.add(item);
            }
            return list;
        } catch (IOException | FeedException ioex) {
            System.err.println(ioex.getMessage());
            return new ArrayList<>();
        }
    }
}

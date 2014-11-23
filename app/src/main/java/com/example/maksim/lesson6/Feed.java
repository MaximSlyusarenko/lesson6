package com.example.maksim.lesson6;

public class Feed {

    public String link;
    public String title;
    public String description;

    public Feed(String link, String title, String description) {
        this.link = link;
        this.title = title;
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
package com.example.maksim.lesson6;

public class Channel {

    public final String link;
    public String name;
    public int id;

    public Channel(int id, String link, String name) {
        this.link = link;
        this.name = name;
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
package fr.isep.news.model;

import java.util.ArrayList;

public class News {
    private ArrayList<Newsdetail> news;

    public News(ArrayList<Newsdetail> news) {
        this.news = news;
    }

    public ArrayList<Newsdetail> getNews() {
        return news;
    }

    public void setNews(ArrayList<Newsdetail> news) {
        this.news = news;
    }
}

package fr.isep.news.model;

import java.util.ArrayList;

public class News {
    private String status;
    private int totalResults;
    private ArrayList<Newsdetail> articles;

    public News(String status, int totalResults, ArrayList<Newsdetail> news) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = news;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Newsdetail> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Newsdetail> articles) {
        this.articles = articles;
    }
}

package fr.isep.news.model;

public class Newsdetail {
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String content;

    public Newsdetail(String title, String url, String urlToImage, String description, String content) {
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.description = description;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

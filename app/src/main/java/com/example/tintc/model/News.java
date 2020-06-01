package com.example.tintc.model;

/**
 * Created by TranTien
 * Date 06/01/2020.
 */
public class News {
    int id;
    String url;
    String html;

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public News(int id, String url, String html) {
        this.id = id;
        this.url = url;
        this.html = html;
    }

    public News(String url, String html) {
        this.url = url;
        this.html = html;
    }

    public News() {
    }
}

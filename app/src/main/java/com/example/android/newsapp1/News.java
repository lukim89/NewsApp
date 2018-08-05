package com.example.android.newsapp1;

public class News {

    private String mNewsTitle;
    private String mNewsAuthor;
    private String mUrl;

    public News(String newsTitle, String author, String url) {
        mNewsTitle = newsTitle;
        mNewsAuthor = author;
        mUrl = url;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsAuthor() {
        return mNewsAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

}




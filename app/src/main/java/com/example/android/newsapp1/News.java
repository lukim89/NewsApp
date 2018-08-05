package com.example.android.newsapp1;

public class News {

    private String mNewsTitle;
    private String mAuthor;
    private String mUrl;

    public News (String newsTitle, String author, String url) {
        mNewsTitle = newsTitle;
        mAuthor = author;
        mUrl = url;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }
    public String getAuthor() {
        return mNewsTitle;
    }
    public String getmUrl() {
        return mUrl;
    }

}




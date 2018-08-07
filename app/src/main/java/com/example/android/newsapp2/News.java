package com.example.android.newsapp2;

public class News {

    private String mSection;
    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mTrailText;
    private String mDate;
    private String mThumbnail;

    public News(String section, String newsTitle, String author, String url, String date, String trailText, String thumbnail) {
        mSection = section;
        mTitle = newsTitle;
        mAuthor = author;
        mUrl = url;
        mDate = date;
        mTrailText = trailText;
        mThumbnail = thumbnail;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getTrailText() {
        return mTrailText;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

}
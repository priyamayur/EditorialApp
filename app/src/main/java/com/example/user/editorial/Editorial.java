package com.example.user.editorial;

public class Editorial {

    private String mTitle;
    private String mDesc;
    private String mAuthor;
    private String mTime;
    private String mUrl;


    public Editorial(String title, String desc,String author, String time, String url) {
        mTitle = title;
        mDesc = desc;
        mTime = time;
        mAuthor=author;
        mUrl = url;
    }



    public String getTitle() {
        return mTitle;
    }


    public String getDesc() {
        return mDesc;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTime() {
        return mTime;
    }


    public String getUrl() {
        return mUrl;
    }
}




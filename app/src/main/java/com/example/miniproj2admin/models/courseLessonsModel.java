package com.example.miniproj2admin.models;

public class courseLessonsModel {

    private String TopicName;
    private String videourl;
    private String Search;
    private String key;

    public String getTopicName() {
        return TopicName;
    }

    public void setTopicName(String topicName) {
        TopicName = topicName;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }

    public courseLessonsModel(String topicName, String videourl, String search, String key) {
       this.TopicName = topicName;
        this.videourl = videourl;
        this.Search = search;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public courseLessonsModel() { }


}

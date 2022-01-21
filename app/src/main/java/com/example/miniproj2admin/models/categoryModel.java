package com.example.miniproj2admin.models;

import java.util.List;

public class categoryModel {
    String url,name;
    List<String> set;
    String key;
    String courseDesc;

    public categoryModel() {
    }

    public categoryModel(String url, String name, List<String> set, String key,String courseDesc) {

        this.url = url;
        this.name = name;
        this.set = set;
        this.key = key;
        this.courseDesc=courseDesc;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSet() {
        return set;
    }

    public void setSet(List<String> set) {
        this.set = set;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

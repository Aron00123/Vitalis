package com.example.vitalis.entity;

/**
 * 公告实体类
 */
public class Notice {
    /** 公告ID */
    private String id;
    /** 公告标题 */
    private String title;
    /** 公告内容 */
    private String content;
    /** 发布时间 */
    private String time;
    /** 发布人 */
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
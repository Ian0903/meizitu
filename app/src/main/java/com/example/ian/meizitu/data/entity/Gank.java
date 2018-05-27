package com.example.ian.meizitu.data.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

import java.util.List;

/**
 * Created by Ian on 2018/3/25.
 */

@Table("Ganks") public class Gank extends Soul{
    @Column("createAt") private String createdAt;
    @Column("desc") private String desc;
    @Column("publishedAt") private String publishedAt;
    @Column("source") private String source;
    @Column("type") private String type;
    @Column("url") private String url;
    @Column("used") private boolean used;
    @Column("who") private String who;
    @Column("images") private List<String> images;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages(){
        return images;
    }

    public void setImages(List<String> images){
        this.images = images;
    }

}


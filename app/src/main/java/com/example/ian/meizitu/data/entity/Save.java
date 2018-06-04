package com.example.ian.meizitu.data.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by Ian on 2018/5/28.
 */

@Table("Save") public class Save extends Soul {

    @Column("desc") private String desc;
    @Column("url") private String url;
    @Column("saveTime") private long saveTime;

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setSaveTime(long saveTime){
        this.saveTime = saveTime;
    }

    public long getSaveTime(){
        return saveTime;
    }

}

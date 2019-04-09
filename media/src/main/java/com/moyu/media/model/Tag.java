package com.moyu.media.model;

import org.apache.ibatis.type.Alias;

@Alias("Tag")
public class Tag {
    int id;
    int videoId;
    String choice;
    String hot;
    String free;
    String recommend;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", videoId=" + videoId +
                ", choice='" + choice + '\'' +
                ", hot='" + hot + '\'' +
                ", free='" + free + '\'' +
                ", recommend='" + recommend + '\'' +
                '}';
    }

}

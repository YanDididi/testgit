package com.moyu.media.model;

import org.apache.ibatis.type.Alias;

@Alias("Tags")
public class Tags {
    int id;
    int videoId;
    int tag;
    String tagCode;


    public enum Tag {
        RECOMMEND(1),
        CHOICE(2),
        FREE(3),
        HOT(4);
        public int tag;

        Tag(int tag) {
            this.tag = tag;
        }
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}

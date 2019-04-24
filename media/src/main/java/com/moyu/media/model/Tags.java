package com.moyu.media.model;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("Tags")
public class Tags {
    int id;
    int videoId;
    int tag;
    String tagCode;
    List<Integer> tagCodesIdLis;


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

    public List<Integer> getTagCodesIdLis() {
        return tagCodesIdLis;
    }

    public void setTagCodesIdLis(List<Integer> tagCodesIdLis) {
        this.tagCodesIdLis = tagCodesIdLis;
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

package com.moyu.media.model;

import org.apache.ibatis.type.Alias;

@Alias("TagCodes")
public class TagCodes {

    int id;
    int tag;
    String tagCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }
}


package com.moyu.media.model;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("Video")
public class Video {
    int id;
    String title;
    String path;
    String cover;
    String desc;
    int createTime;
    int updateTime;
    int categoryId;
    String type;
    List<Integer> tagCodesIdLis;

    int status=1;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public List<Integer> getTagCodesIdLis() {
        return tagCodesIdLis;
    }

    public void setTagCodesIdLis(List<Integer> tagCodesIdLis) {
        this.tagCodesIdLis = tagCodesIdLis;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

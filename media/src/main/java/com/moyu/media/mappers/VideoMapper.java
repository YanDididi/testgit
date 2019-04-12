package com.moyu.media.mappers;

import com.moyu.media.model.Video;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoMapper {
    Video selectVideo(int id);
    int updateVideo(Video video);
    int selectCount(@Param("categoryId") int categoryId, @Param("status") int status);

    List<Video> selectVideos(@Param("categoryId") int categoryId,
                             @Param("status") int status,
                             @Param("list") List<Integer> list,
                             @Param("limit") int limit,
                             @Param("offset") int offset);

    int insertVideo(Video video);

    int deleteVideo(@Param("list") List<Integer> idLis);
}

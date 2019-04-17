package com.moyu.media.mappers;

import com.moyu.media.model.Tags;

import com.moyu.media.model.Video;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagsMapper {
    Tags selectTags(@Param("id") int id);
    int insertTags(Tags tags);
    int updateTags(Tags tags);
    int selectCount(@Param("videoId") int videoId);

    int deleteTagsByV(@Param("tag") int tag,@Param("list") List<Integer> vidLis);
    int addTagsByV(@Param("list") List<Tags> TagsLis);
    int deleteTags(@Param("list") List<Integer> idLis);

    List<Tags> getTagsLisByV(@Param("videoId") int videoId,
                              @Param("limit") int limit,
                              @Param("offset") int offset);

    List<Video> getVideoLisByTagNobing(@Param("tagsId") int tagsId, @Param("limit") int limit,
                                       @Param("offset") int offset);

    int getVideoCountLisByTagNobing(@Param("tagsId") int tagsId) ;

    List<Video> getVideoLisByTagbing( @Param("tagsId") int tagsId,
                                        @Param("limit") int limit,
                                        @Param("offset") int offset);

    int getVideoCountLisByTagbing(@Param("tagsId") int tagsId) ;

}

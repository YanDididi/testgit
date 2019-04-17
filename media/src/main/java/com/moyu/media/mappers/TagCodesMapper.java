
package com.moyu.media.mappers;


import com.moyu.media.model.TagCodes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagCodesMapper {
    TagCodes selectTagCodes(@Param("id") int id);
    int insertTagCodes(TagCodes tagCodes);
    int updateTagCodes(TagCodes tagCodes);
    int selectCount(@Param("tagCode") String tagCode);
    int deleteTagCodes(@Param("list") List<Integer> idLis);
    List<TagCodes> getTagCodesLis(@Param("tagCode") String tagCode,
                                  @Param("limit") int limit,
                                  @Param("offset") int offset);

    /*List<Tags> getTagCodesLisByV(@Param("videoId") int videoId,
                             @Param("list") List<Integer> tagLis,
                             @Param("limit") int limit,
                             @Param("offset") int offset);

    int deleteTagsByV(@Param("tag") int tag, @Param("list") List<Integer> vidLis);
    int addTagsByV(@Param("list") List<Tags> TagsLis);



    List<Video> getVideoLisByTagNobing(@Param("tagsId") int tagsId);

    int getVideoCountLisByTagNobing(@Param("tagsId") int tagsId) ;

    List<Video> getVideoLisByTagbing(@Param("tagsId") int tagsId,
                                     @Param("limit") int limit,
                                     @Param("offset") int offset);

    int getVideoCountLisByTagbing(@Param("tagsId") int tagsId) ;*/


}


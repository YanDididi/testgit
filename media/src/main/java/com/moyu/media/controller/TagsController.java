package com.moyu.media.controller;

import com.alibaba.fastjson.JSONObject;
import com.moyu.media.core.db.DBHelper;
import com.moyu.media.core.result.Result;
import com.moyu.media.core.result.ResultGenerator;
import com.moyu.media.mappers.TagsMapper;
import com.moyu.media.model.Tags;
import com.moyu.media.model.Video;
import com.moyu.media.util.FileUtil;
import com.moyu.media.util.MYUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TagsController {
    @RequestMapping(path = {"/controller/tags/{id}"}, method = RequestMethod.GET)
    public Result getTags(@PathVariable int id) {
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagsMapper mapper = sqlSession.getMapper(TagsMapper.class);
            Tags tags = mapper.selectTags(id);
            return ResultGenerator.success(tags);
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }


    @RequestMapping(path = {"/controller/tags"}, method = RequestMethod.POST)
    public Result addTags(@RequestBody Map<String, Object> map) {
        String[] needParams = {"videoId", "tag"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
            Tags tags = new Tags();
            tags.setTag(MYUtil.ParseInt(MYUtil.GetParam(map, "tag")));
            tags.setVideoId(MYUtil.ParseInt(MYUtil.GetParam(map, "videoId")));

            if (tagsMapper.insertTags(tags) > 0) {
                sqlSession.commit();
                return ResultGenerator.success();
            } else {
                return ResultGenerator.fail("添加资源失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/tags"}, method = RequestMethod.PUT)
    public Result updateTags(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id", "videoId", "tag"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
            Tags tags = new Tags();
            tags.setId(MYUtil.ParseInt(MYUtil.GetParam(map, "id")));
            tags.setTag(MYUtil.ParseInt(MYUtil.GetParam(map, "tag")));
            tags.setVideoId(MYUtil.ParseInt(MYUtil.GetParam(map, "videoId")));
            if (tagsMapper.updateTags(tags) > 0) {
                sqlSession.commit();
                return ResultGenerator.success(tags);
            } else {
                return ResultGenerator.fail("资源修改失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/tags"}, method = RequestMethod.DELETE)
    public Result delTags(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
        try {

            List<Map<String, Integer>> vIds = (List<Map<String, Integer>>) map.get("id");

            List<Integer> idLis = new ArrayList<>();
            for (int i = 0; i < vIds.size(); i++) {
                int vId = vIds.get(i).get("id");
                idLis.add(vId);
            }
            int result1 = tagsMapper.deleteTags(idLis);
            //int result2 = resourceMapper.deleteVideo(vidLis);
            if (result1 > 0) {
                sqlSession.commit();
                return ResultGenerator.success();
            } else {
                return ResultGenerator.fail("del资源失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/deleteTagsByV/{tag}"}, method = RequestMethod.DELETE)
    public Result deleteTagsByV(@PathVariable int tag, @RequestBody Map<String, Object> map) {
        String[] needParams = {"id"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
        try {

            List<Map<String, Integer>> vIds = (List<Map<String, Integer>>) map.get("id");

            List<Integer> vidLis = new ArrayList<>();
            for (int i = 0; i < vIds.size(); i++) {
                int vId = vIds.get(i).get("id");
                vidLis.add(vId);
            }
            int result1 = tagsMapper.deleteTagsByV(tag, vidLis);
            //int result2 = resourceMapper.deleteVideo(vidLis);
            if (result1 > 0) {
                sqlSession.commit();
                return ResultGenerator.success();
            } else {
                return ResultGenerator.fail("del资源失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/editTags"}, method = RequestMethod.PUT)
    public Result editTags(@RequestBody String json) {
        // [{"videoId": 1,"tag": 4},{"videoId": 2,"tag": 4},{"videoId": 3,"tag": 4}]
        //{"videoId":1, "tagCodesIdLis":[ 1, 2, 3 ]}
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
        List<Integer> vIds = new ArrayList<>();
        List<Integer> tagCodesIds ;
        List<Tags> tagsLis = new ArrayList<>();
        try {

            Tags tags = JSONObject.parseObject(json, Tags.class);
            int vId=tags.getVideoId();
            tagCodesIds=tags.getTagCodesIdLis();
            vIds.add(vId);
            tagsMapper.deleteTagsByV(-1,vIds);
            Tags tagss ;

            for (int tagCodesId:tagCodesIds) {
                tagss=new Tags();
                tagss.setTag(tagCodesId);
                tagss.setVideoId(vId);
                tagsLis.add(tagss);
            }
            int result1 = tagsMapper.addTagsByV(tagsLis);

            if (result1 > 0) {
                sqlSession.commit();
                return ResultGenerator.success();
            } else {
                return ResultGenerator.fail("edit资源失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/getVideoLisByTagNobing"}, method = RequestMethod.GET)
    public Result getVideoLisByTagNobing(@RequestParam(value = "tagsId", required = false, defaultValue = "-1") int tagsId,
            /*@RequestBody(required = false) Map<String, Object> map,*/
                                         @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
                                         @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize) {

        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagsMapper mapper = sqlSession.getMapper(TagsMapper.class);
            List<Video> videoLis;
            int offset = (pageIndex - 1) * pageSize;
            videoLis = mapper.getVideoLisByTagNobing(tagsId,status, pageSize, offset);

            if (pageSize > 0) {
                int totalCount = mapper.getVideoCountLisByTagNobing(tagsId,status);
                return ResultGenerator.successPage(pageIndex, pageSize, totalCount, videoLis);
            } else {
                return ResultGenerator.success(videoLis);
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/getVideoLisByTagbing"}, method = RequestMethod.GET)
    public Result getVideoLisByTagbing(@RequestParam(value = "tagsId", required = false, defaultValue = "-1") int tagsId,
            /*@RequestBody(required = false) Map<String, Object> map,*/
                                       @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize) {


        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagsMapper mapper = sqlSession.getMapper(TagsMapper.class);
            List<Video> videoLis;
            int offset = (pageIndex - 1) * pageSize;
            videoLis = mapper.getVideoLisByTagbing(tagsId,status, pageSize, offset);

            if (pageSize > 0) {
                int totalCount = mapper.getVideoCountLisByTagbing(tagsId,status);
                return ResultGenerator.successPage(pageIndex, pageSize, totalCount, videoLis);
            } else {
                return ResultGenerator.success(videoLis);
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/getTagsListByV"}, method = RequestMethod.GET)
    public Result getTagsLisByV(@RequestParam(value = "videoId", required = false, defaultValue = "-1") int videoId,
                                @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize) {


        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {

            TagsMapper mapper = sqlSession.getMapper(TagsMapper.class);
            List<Tags> tagsLis;
            int offset = (pageIndex - 1) * pageSize;
            tagsLis = mapper.getTagsLisByV(videoId, pageSize, offset);

            if (pageSize > 0) {
                int totalCount = mapper.selectCount(videoId);
                return ResultGenerator.successPage(pageIndex, pageSize, totalCount, tagsLis);
            } else {
                return ResultGenerator.success(tagsLis);
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }




    /*@RequestMapping(path = {"/controller/getVideoList"}, method = RequestMethod.GET)
    public Result getResourceList(@RequestParam(value = "categoryId", required = false, defaultValue = "-1") int categoryId,
                                  @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
                                  @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize,
                                 *//* @RequestParam(value = "tag", required = false, defaultValue = "-1") int tag,*//*
                                  @RequestBody(required = false) Map<String, Object> map) {


        *//*String[] needParams = {"tag"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }*//*

        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            if( null != map){
                List<Map<String, Integer>> tags = (List<Map<String, Integer>>) map.get("tag");

                List<Integer> tagLis = new ArrayList<>();
                for (int i = 0; i < tags.size(); i++) {
                    int tag = tags.get(i).get("tag");
                    tagLis.add(tag);
                }
                VideoMapper mapper = sqlSession.getMapper(VideoMapper.class);
                List<Video> videos;
                int offset = (pageIndex - 1) * pageSize;
                videos = mapper.selectVideos(categoryId, status,tagLis,pageSize, offset);

                if(pageSize>0){
                    int totalCount=mapper.selectCount(categoryId,status);
                    return ResultGenerator.successPage(pageIndex,pageSize,totalCount,videos);
                }else{
                    return ResultGenerator.success(videos);
                }
            }else{

                List<Integer> tagLis = null;
                VideoMapper mapper = sqlSession.getMapper(VideoMapper.class);
                List<Video> videos;
                int offset = (pageIndex - 1) * pageSize;
                videos = mapper.selectVideos(categoryId, status,tagLis,pageSize, offset);

                if(pageSize>0){
                    int totalCount=mapper.selectCount(categoryId,status);
                    return ResultGenerator.successPage(pageIndex,pageSize,totalCount,videos);
                }else{
                    return ResultGenerator.success(videos);
                }
            }

        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }*/
}

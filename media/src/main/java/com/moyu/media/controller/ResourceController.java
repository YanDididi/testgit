package com.moyu.media.controller;

import com.moyu.media.core.config.FileConfig;
import com.moyu.media.core.db.DBHelper;
import com.moyu.media.core.result.Result;
import com.moyu.media.core.result.ResultGenerator;
import com.moyu.media.mappers.TagsMapper;
import com.moyu.media.mappers.VideoMapper;
import com.moyu.media.model.Video;
import com.moyu.media.util.MYUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.moyu.media.util.FileUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController

public class ResourceController {

    @Autowired
    private FileConfig fileConfig;

    @RequestMapping(path = {"/controller/video/{id}"}, method = RequestMethod.GET)
    public Result getResource(@PathVariable int id) {
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            VideoMapper mapper = sqlSession.getMapper(VideoMapper.class);
            Video resource = mapper.selectVideo(id);
            return ResultGenerator.success(resource);
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }


    @RequestMapping(path = {"/controller/video"}, method = RequestMethod.POST)
    public Result addResource(@RequestBody Map<String, Object> map) {
        String[] needParams = {"title", "path", "categoryId"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            VideoMapper resourceMapper = sqlSession.getMapper(VideoMapper.class);
            Video video = new Video();
            video.setCategoryId(MYUtil.ParseInt(MYUtil.GetParam(map, "categoryId")));
            video.setCover(MYUtil.GetParam(map, "cover"));
            video.setCreateTime(MYUtil.GetTimeStamps());
            video.setUpdateTime(MYUtil.GetTimeStamps());
            video.setDesc(MYUtil.GetParam(map, "desc"));
            video.setPath(MYUtil.GetParam(map, "path"));
            video.setTitle(MYUtil.GetParam(map, "title"));
            video.setType(MYUtil.GetParam(map, "type"));
            if (resourceMapper.insertVideo(video) > 0) {
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

    @RequestMapping(path = {"/controller/video"}, method = RequestMethod.PUT)
    public Result updateResource(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id", "title", "path", "cover", "categoryId"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            VideoMapper resourceMapper = sqlSession.getMapper(VideoMapper.class);
            Video resource = new Video();
            resource.setId(MYUtil.ParseInt(MYUtil.GetParam(map, "id")));
            resource.setCover(MYUtil.GetParam(map, "cover"));
            resource.setTitle(MYUtil.GetParam(map, "title"));
            resource.setUpdateTime(MYUtil.GetTimeStamps());
            resource.setDesc(MYUtil.GetParam(map, "desc"));
            resource.setType(MYUtil.GetParam(map, "type"));
            resource.setPath(MYUtil.GetParam(map, "path"));
            resource.setCategoryId(MYUtil.ParseInt(MYUtil.GetParam(map, "categoryId")));
            if (resourceMapper.updateVideo(resource) > 0) {
                sqlSession.commit();
                return ResultGenerator.success(resource);
            } else {
                return ResultGenerator.fail("资源修改失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/video"}, method = RequestMethod.DELETE)
    public Result delResource(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        Video video;String videoPath;String coverPath;
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        VideoMapper resourceMapper = sqlSession.getMapper(VideoMapper.class);
        TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
        try {

            List<Map<String, Integer>> vIds = (List<Map<String, Integer>>) map.get("id");

            String targetPath = fileConfig.getFilePath();
            int indexb = targetPath.lastIndexOf("\\");
            String tPath = targetPath.substring(0, indexb)+targetPath.substring(indexb+1, targetPath.length());

            List<Integer> vidLis = new ArrayList<>();
            for (int i = 0; i < vIds.size(); i++) {
                int vId = vIds.get(i).get("id");
                video = resourceMapper.selectVideo(vId);
                if(null != video){
                    videoPath=tPath+video.getPath();coverPath=tPath+video.getCover();
                    FileUtil.delFile(videoPath);
                    FileUtil.delFile(coverPath);
                }
                vidLis.add(vId);
            }
            tagsMapper.deleteTagsByV(-1,vidLis);
            if (resourceMapper.deleteVideo(vidLis) > 0) {
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

    @RequestMapping(path = {"/controller/getVideoList"}, method = RequestMethod.GET)
    public Result getResourceList(@RequestParam(value = "categoryId", required = false, defaultValue = "-1") int categoryId,
                                  @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
                                  @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize,
            /* @RequestParam(value = "tag", required = false, defaultValue = "-1") int tag,*/
                                  @RequestBody(required = false) Map<String, Object> map) {

        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {

            if (null != map) {
                List<Map<String, Integer>> tags = (List<Map<String, Integer>>) map.get("tag");

                List<Integer> tagLis = new ArrayList<>();
                for (int i = 0; i < tags.size(); i++) {
                    int tag = tags.get(i).get("tag");
                    tagLis.add(tag);
                }
                VideoMapper mapper = sqlSession.getMapper(VideoMapper.class);
                List<Video> videos;
                int offset = (pageIndex - 1) * pageSize;
                videos = mapper.selectVideos(categoryId, status, tagLis, pageSize, offset);
                if (pageSize > 0) {
                    int totalCount = mapper.selectCount(categoryId, status);
                    return ResultGenerator.successPage(pageIndex, pageSize, totalCount, videos);
                } else {
                    return ResultGenerator.success(videos);
                }
            } else {
                VideoMapper mapper = sqlSession.getMapper(VideoMapper.class);
                List<Video> videos;
                int offset = (pageIndex - 1) * pageSize;
                videos = mapper.selectVideos(categoryId, status, null, pageSize, offset);
                if (pageSize > 0) {
                    int totalCount = mapper.selectCount(categoryId, status);
                    return ResultGenerator.successPage(pageIndex, pageSize, totalCount, videos);
                } else {
                    return ResultGenerator.success(videos);
                }
            }


        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    /*@RequestMapping(path = "/delFile", method = RequestMethod.DELETE)
    public void delFile(@RequestParam("filePath") String filePath,@RequestParam("fileName") String fileName){
        File file=new File(filePath+"/"+fileName);
        if(file.exists()&&file.isFile())
            file.delete();
    }*/



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


package com.moyu.media.controller;

import com.moyu.media.core.db.DBHelper;
import com.moyu.media.core.result.Result;
import com.moyu.media.core.result.ResultGenerator;
import com.moyu.media.mappers.TagCodesMapper;
import com.moyu.media.model.TagCodes;
import com.moyu.media.util.MYUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController

public class TagCodesController {

    @RequestMapping(path = {"/controller/TagCodes/{id}"}, method = RequestMethod.GET)
    public Result getTagCodes(@PathVariable int id) {
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagCodesMapper mapper = sqlSession.getMapper(TagCodesMapper.class);
            TagCodes tagCodes = mapper.selectTagCodes(id);
            return ResultGenerator.success(tagCodes);
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }


    @RequestMapping(path = {"/controller/addTagCodes"}, method = RequestMethod.POST)
    public Result addTagCodes(@RequestBody Map<String, Object> map) {
        String[] needParams = {"tag", "tagCode"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
            TagCodes tagCodes = new TagCodes();
            tagCodes.setTag(MYUtil.ParseInt(MYUtil.GetParam(map, "tag")));
            tagCodes.setTagCode((MYUtil.GetParam(map, "tagCode")));

            if (tagCodesMapper.insertTagCodes(tagCodes) > 0) {
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

    @RequestMapping(path = {"/controller/updateTagCodes"}, method = RequestMethod.PUT)
    public Result updateTagCodes(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id","tag", "tagCode"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
            TagCodes tagCodes = new TagCodes();
            tagCodes.setId(MYUtil.ParseInt(MYUtil.GetParam(map, "id")));
            tagCodes.setTag(MYUtil.ParseInt(MYUtil.GetParam(map, "tag")));
            tagCodes.setTagCode((MYUtil.GetParam(map, "tagCode")));
            if (tagCodesMapper.updateTagCodes(tagCodes) > 0) {
                sqlSession.commit();
                return ResultGenerator.success(tagCodes);
            } else {
                return ResultGenerator.fail("资源修改失败");
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping(path = {"/controller/delTagsCodes"}, method = RequestMethod.DELETE)
    public Result delTagCodes(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
        try {

            List<Map<String, Integer>> ids = (List<Map<String, Integer>>) map.get("id");

            List<Integer> idLis = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                int id = ids.get(i).get("id");
                idLis.add(id);
            }
            int result1 = tagCodesMapper.deleteTagCodes(idLis);
            //int result2 = resourceMapper.deleteVideo(vidLis);
            if (result1 > 0 ) {
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


    @RequestMapping(path = {"/controller/getTagCodesLis"}, method = RequestMethod.GET)
    public Result getTagCodesLis(@RequestParam(value = "tagCode", required = false, defaultValue = "-1") String tagCode,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize) {

        List<TagCodes> tagCodesLis;
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
        try {
            int offset = (pageIndex - 1) * pageSize;
            tagCodesLis = tagCodesMapper.getTagCodesLis(tagCode,pageSize, offset);


            if(pageSize>0){
                int totalCount=tagCodesMapper.selectCount(tagCode);
                return ResultGenerator.successPage(pageIndex,pageSize,totalCount,tagCodesLis);
            }else{
                return ResultGenerator.success(tagCodesLis);
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }
}


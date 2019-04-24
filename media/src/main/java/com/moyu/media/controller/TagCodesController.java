
package com.moyu.media.controller;

import com.moyu.media.core.db.DBHelper;
import com.moyu.media.core.result.Result;
import com.moyu.media.core.result.ResultGenerator;
import com.moyu.media.mappers.TagCodesMapper;
import com.moyu.media.mappers.TagsMapper;
import com.moyu.media.model.TagCodes;
import com.moyu.media.util.MYUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController

public class TagCodesController {

    @RequestMapping(path = {"/controller/tagCodes/{id}"}, method = RequestMethod.GET)
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


    @RequestMapping(path = {"/controller/tagCodes"}, method = RequestMethod.POST)
    public Result addTagCodes(@RequestBody Map<String, Object> map) {
        String[] needParams = {"tagCode"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
            TagCodes tagCodes = new TagCodes();
            tagCodes.setCreateTime(MYUtil.GetTimeStamps());
            tagCodes.setUpdateTime(MYUtil.GetTimeStamps());
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

    @RequestMapping(path = {"/controller/tagCodes"}, method = RequestMethod.PUT)
    public Result updateTagCodes(@RequestBody Map<String, Object> map) {
        String[] needParams = {"id", "tagCode"};
        if (!MYUtil.IsExistParams(map, needParams)) {
            return ResultGenerator.fail("params empty");
        }
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        try {
            TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
            TagCodes tagCodes = new TagCodes();
            tagCodes.setId(MYUtil.ParseInt(MYUtil.GetParam(map, "id")));

            tagCodes.setUpdateTime(MYUtil.GetTimeStamps());
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

    @RequestMapping(path = {"/controller/tagCodes/{id}"}, method = RequestMethod.DELETE)
    public Result delTagCodes(@PathVariable int id) {
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
        TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);

        try {
            int result1 = tagCodesMapper.deleteTagCodes(id);
            //删除关联tags
            tagsMapper.deleteTagsByT(id);
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


    @RequestMapping(path = {"/controller/getTagCodesLis"}, method = RequestMethod.GET)
    public Result getTagCodesLis(@RequestParam(value = "tagCode", required = false, defaultValue = "-1") String tagCode,
                                 @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") int pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize) {

        List<TagCodes> tagCodesLis;
        SqlSession sqlSession = DBHelper.getSqlSessionFacttory().openSession();
        TagCodesMapper tagCodesMapper = sqlSession.getMapper(TagCodesMapper.class);
        try {
            int offset = (pageIndex - 1) * pageSize;
            tagCodesLis = tagCodesMapper.getTagCodesLis(tagCode, status,pageSize, offset);


            if (pageSize > 0) {
                int totalCount = tagCodesMapper.selectCount(tagCode,status);
                return ResultGenerator.successPage(pageIndex, pageSize, totalCount, tagCodesLis);
            } else {
                return ResultGenerator.success(tagCodesLis);
            }
        } catch (Exception e) {
            return ResultGenerator.fail(e.toString());
        } finally {
            sqlSession.close();
        }
    }
}


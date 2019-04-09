package com.moyu.media.core.result;

public class ResultGenerator {
    public static Result success() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code);
        result.setStatus(Result.Status.SUCCESS.status);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code);
        result.setStatus(Result.Status.SUCCESS.status);
        result.setData(data);
        return result;
    }

    public static PageResult successPage(int pageIndex,int pageSize,int totalCount,Object data){
        PageResult result = new PageResult();
        result.setCode(ResultCode.SUCCESS.code);
        result.setStatus(Result.Status.SUCCESS.status);
        result.setData(data);
        result.setPageIndex(pageIndex);
        result.setPageSize(pageSize);
        result.setTotalCount(totalCount);
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL.code);
        result.setStatus(Result.Status.FAIL.status);
        result.setMsg(msg);
        return result;
    }
    //可增加统一异常处理类
}

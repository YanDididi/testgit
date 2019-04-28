package com.poi.exceldeal.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/12/18 17:31
 */
public class HttpConfig {

    public static void setExcelResponseModel(byte[] outPutByte, HttpServletResponse response) throws IOException {
        HttpConfig.setExcelResponseModel("outPutFile", outPutByte, response);
    }

    public static void setExcelResponseModel(String fileName, byte[] outPutByte, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/msexcel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename= " + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        response.addHeader("Cache-Control", "no-cache");
        response.getOutputStream().write(outPutByte);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}

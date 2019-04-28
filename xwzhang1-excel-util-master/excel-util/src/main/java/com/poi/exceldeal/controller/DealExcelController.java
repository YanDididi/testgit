package com.poi.exceldeal.controller;

import com.poi.exceldeal.util.HttpConfig;
import com.poi.exceldeal.webservice.ExcelWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 可以理解为入口和出口
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/12/20 15:37
 */
@RestController
@RequestMapping("/excel")
@Slf4j
public class DealExcelController {

    public static byte[] outPutByte = new byte[0];

    @Autowired
    private ExcelWebService service;

    @PostMapping(value = "/set")
    public String setErrorFile(HttpServletRequest request, HttpServletResponse response) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {

        MultipartFile uploadFile = ((MultipartHttpServletRequest) request).getFile("uploadFile");

        outPutByte = service.importShopCostPriceScope(uploadFile.getBytes());

        // 异常逻辑自行处理
        return "处理成功";
    }

    @GetMapping(value = "/get")
    public String getErrorFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpConfig.setExcelResponseModel("奖励规则适用范围导入错误数据", outPutByte, response);
        // 各种可能出现异常的请自行判断
        return "导出成功";
    }
}

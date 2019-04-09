package com.moyu.redarmy.controller;

import com.moyu.redarmy.core.config.FileConfig;
import com.moyu.redarmy.core.result.Result;
import com.moyu.redarmy.core.result.ResultGenerator;
import com.moyu.redarmy.util.FileUtil;
import com.moyu.redarmy.util.MYUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class FileController {
    @Autowired
    private FileConfig fileConfig;

    @RequestMapping(path = "/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "type", required = false, defaultValue = "") String type) {
        if (file.isEmpty()) {
            return ResultGenerator.fail("文件为空");
        }
        String webPath = "/";
        try {
            String fileName = file.getOriginalFilename();
            String rootDirName = "upload/other";
            String targetPath = fileConfig.getFilePath();

            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = dateFormat.format(date);

            boolean isNeedUnCompress = false;
            switch (type) {
                case "image": {
                    rootDirName = "upload/images";
                    fileName = dateStr + MYUtil.Random(4) + "." + MYUtil.getExtensionName(fileName);
                    webPath = webPath + rootDirName + "/" + fileName;
                    break;
                }
                case "scene": {
                    isNeedUnCompress = true;
                    rootDirName = "upload/scene";
                    rootDirName = rootDirName + System.getProperty("file.separator") + dateStr + MYUtil.Random(4);
                    webPath = webPath + rootDirName + "/" + MYUtil.getFileNameNoEx(fileName) + "/";
                    break;
                }
                default: {
                    fileName = dateStr + MYUtil.Random(4) + "." + MYUtil.getExtensionName(fileName);
                    webPath = webPath + rootDirName + "/" + fileName;
                    break;
                }
            }
//            System.getProperty("file.separator")
            targetPath += rootDirName;
            FileUtil.uploadFile(file.getBytes(), targetPath, fileName, isNeedUnCompress);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.fail("上传失败");
        }
        return ResultGenerator.success(webPath);
    }
}

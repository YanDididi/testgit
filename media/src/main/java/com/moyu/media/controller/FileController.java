package com.moyu.media.controller;

import com.moyu.media.core.config.FileConfig;
import com.moyu.media.core.result.Result;
import com.moyu.media.core.result.ResultGenerator;
import com.moyu.media.util.FileUtil;
import com.moyu.media.util.MYUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${server.port}")
    private Integer port;

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
                    rootDirName = "upload/" + FileConfig.FILE_TYPE_IMAGE;
                    fileName = dateStr + MYUtil.Random(4) + "." + MYUtil.getExtensionName(fileName);
                    webPath = webPath + rootDirName + "/" + fileName;
                    break;
                }
                case "video": {
                    isNeedUnCompress = true;
                    rootDirName = "upload/" + FileConfig.FILE_TYPE_VIDEO;
                    fileName = dateStr + MYUtil.Random(4) + "." + MYUtil.getExtensionName(fileName);
//                    rootDirName = rootDirName + System.getProperty("file.separator") + dateStr + MYUtil.Random(4);
                    webPath = webPath + rootDirName + "/" + fileName;
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
//            FileUtil.uploadFile(file.getBytes(), targetPath, fileName, isNeedUnCompress);
            FileUtil.uploadBigFile(file, targetPath, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.fail("上传失败");
        }
        return ResultGenerator.success(webPath);
//        return ResultGenerator.success(String.format("http://%s:%d%s",MYUtil.getIp(),port,webPath));
    }
}

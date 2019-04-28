package com.poi.exceldeal.webservice;

import com.poi.exceldeal.facade.ExcelModel;
import com.poi.exceldeal.util.ExcelImportObject;
import com.poi.exceldeal.util.ExcelUtil;
import com.poi.exceldeal.util.ExcelValidator;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/12/11 15:55
 */
@Service
public class ExcelWebService {

    public byte[] importShopCostPriceScope(byte[] uploadFile) throws IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, NoSuchFieldException {

        // 1. [构建条件]构建表格转List的条件对象
        ExcelImportObject<ExcelModel> rule = new ExcelImportObject<>();
        rule.setFileInputStream(uploadFile);
        rule.setFirstRowNum(2);
        rule.setVerifyDuplicationCells(new Integer[]{1});
        // 2. [*获取表中的核心数据]表格核心内容转List
        rule = ExcelUtil.exchangeExcelStreamToList(ExcelModel.class, rule);
        // 3 [数据格式验证]根据注解进行数据格式验证错误
        ExcelValidator.excelValidate(rule);
        // 4 [逻辑验证]表格转换成功后，进行数据逻辑验证
        for (int i = 0; i < rule.getResultList().size(); i++) {
            StringBuffer errorMessage = new StringBuffer();
            // 处理自己的逻辑并将错误信息写入errorMessage
            ExcelUtil.errorMessageDeal(rule, i, errorMessage);
        }
        // 5. [处理错误信息]将错误信息写入文件
        // 6. 处理正常数据

        return ExcelUtil.writeErrorMessageToExcel(rule);
    }
}

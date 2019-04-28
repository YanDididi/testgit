package com.poi.exceldeal.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 关于涉及到表格行列的属性，统一都是从1开始计数
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/12/20 15:39
 */
@Data
public class ExcelImportObject<T extends ExcelModelRequest> {

    /**
     * 待转换的文件流byte
     */
    byte[] fileInputStream;

    /**
     * 转换成功结果List[该结果仅仅是对数据在进行表格到对象之间的转换时成功的条数]
     */
    private List<T> successList = new ArrayList<>();

    /**
     * 转换失败结果List[该结果仅仅是对数据在进行表格到对象之间的转换时失败的条数]
     */
    private List<T> failedList = new ArrayList<>();

    /**
     * 无论该行数据转换失败与否均写入到该List
     */
    private List<T> resultList = new ArrayList<>();

    /**
     * 最终校验成功可用于导入的List对象
     */
    private List<T> importList = new ArrayList<>();

    /**
     * 单元格是否允许有空值
     * 默认不允许
     */
    private Boolean notNullCell = true;

    /**
     * 从第几行开始读取[序号] 为空时， 若标注了标题行，则鼻癌提行下一行为开始读取的行，否则从首行
     * 注意：在选取行的时候请选择主要数据开始的行，不包含标题以及其他备注信息
     */
    private Integer firstRowNum;

    /**
     * 第几行结束读取[序号],选填
     * 当该参数为空时，默认读取到整个表格没有数据的一行
     */
    private Integer lastRowNum;

    /**
     * 首列[从1开始计数]
     */
    private Integer firstCellNum;

    /**
     * 操作的列数
     */
    private Integer cellCount;

    /**
     * 标题行[从0开始计数] 默认为操作开始行的前一行
     */
    private Integer titleRowNum;

    /**
     * 验证成功条数[该结果仅仅是对数据在进行表格到对象之间的转换时成功的条数]
     */
    private int successCount = 0;

    /**
     * 验证失败条数[该结果仅仅是对数据在进行表格到对象之间的转换时失败的条数]
     */
    private int failedCount = 0;

    /**
     * 需要验证重复数据的列序号 如： new Integer[]{1,2,3}
     */
    private Integer[] verifyDuplicationCells = {};

    /**
     * 多列组合后不得有重复，主要列放开头 如： new Integer[]{3,1,2}  表示1,2,3列组合起来不得有重复,其中第三列为重复提示信息，第1和2列仅仅是辅助第3列验证
     */
    private Integer[] uniteVerifyDuplicationCells = {};

    /**
     * true 存在标题行
     * false  不存在标题行
     */
    private Boolean existTitle = true;

    /**
     * true 一条失败全部失败(默认)
     * false 成功执行，失败返回
     */
    private Boolean oneFailAllFail = true;

    /**
     * 数据验证错误信息 KEY为错误的行 VALUE为错误信息
     */
    private Map<Integer, StringBuffer> errorMap = new HashMap<>();
}

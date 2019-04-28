package com.poi.exceldeal.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/12/20 15:39
 */
public class ExcelUtil {

    private static final String LONG = "long";
    private static final String M_LONG = "Long";
    private static final String FLOAT = "Float";
    private static final String M_FLOAT = "float";
    private static final String DOUBLE = "Double";
    private static final String M_DOUBLE = "double";
    private static final String INTEGER = "Integer";
    private static final String INT = "int";
    private static final String BIGDECIMAL = "BigDecimal";
    private static final String LOCALDATE = "LocalDate";
    private static final String LOCALDATETIME = "LocalDateTime";
    private static final String LOCALTIME = "LocalTime";

    private static final String SET_NULL = "SET_NULL";
    private static final String INVALID_DATA = "INVALID_DATA";

    /**
     * 传入类和ExcelTransferRule对象，其中包含文件流
     * 处理中，当某行数据处理出现错误后，该行数据将以空值的形式赋值给目标对象[所以此处要求，开发者在创建对应实体对象的时候不要使用基本数据类型]
     *
     * @param clazz 目标类对象
     * @param rule  表格转换规则对象
     * @param <T>   任意继承了AbstractExcelRequest的实体类
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public static <T extends ExcelModelRequest> ExcelImportObject<T> exchangeExcelStreamToList(Class<T> clazz, ExcelImportObject<T> rule) throws IOException, IllegalAccessException, InstantiationException {
        if (rule.getFileInputStream().length <= 0) {
            // 文件输入流为空时的处理逻辑
        }
        // 1. 将文件流转换成可以处理的Workbook对象
        InputStream inputStream = new ByteArrayInputStream(rule.getFileInputStream());
        Workbook workbook = new XSSFWorkbook(inputStream);
        // 2. 获取工作簿中的第一个工作表（一个表格中会有N个工作表）  本工具默认只处理第一个工作表
        Sheet sheetAt = workbook.getSheetAt(0);
        // 3. 获取Class<T>对象的属性信息
        Field[] fields = clazz.getDeclaredFields();

        // 4. 规范在表格操作中将会使用到的操作首行，操作尾行，操作首列，操作尾列等参数
        // 4.1 开始操作的行
        int firstRow;
        if (rule.getFirstRowNum() == null) {
            if (rule.getTitleRowNum() != null) {
                rule.setFirstRowNum(rule.getTitleRowNum() + 1);
            } else {
                rule.setFirstRowNum(1);
            }
        }
        firstRow = rule.getFirstRowNum() - 1;
        // 4.2 结束操作的行
        int lastRow;
        if (rule.getLastRowNum() == null) {
            rule.setLastRowNum(sheetAt.getLastRowNum() + 1);
        }
        lastRow = rule.getLastRowNum() - 1;
        // 4.3 开始操作的列
        int firstCell;
        if (rule.getFirstCellNum() == null) {
            rule.setFirstCellNum(1);
        }
        firstCell = rule.getFirstCellNum() - 1;
        // 4.4 操作的列数[当自定义的操作列数大于制定对象属性数时，强制将操作列数指定为对象属性个数，嗯~~~ 是有点不符合开发思想，但是管不了辣么多了]
        int cellCount;
        if (rule.getCellCount() == null || rule.getCellCount() > fields.length) {
            rule.setCellCount(fields.length);
        }
        cellCount = rule.getCellCount();
        // 4.5 标题行
        Integer titleRow = null;
        if (firstRow > 0) {
            if (rule.getExistTitle()) {
                if (rule.getTitleRowNum() == null) {
                    rule.setTitleRowNum(firstRow);
                }
                titleRow = rule.getTitleRowNum() - 1;
            }
        }

        // 5. 定义相关输出对象
        // 5.1 转换成功对象
        List<T> successList = new ArrayList<>();
        // 5.2 转换失败对象
        List<T> failedList = new ArrayList<>();
        // 5.3 成功与否都怼进去对象
        List<T> resultList = new ArrayList<>();
        // 5.4 单列验重set
        Set<String> verifyDuplicationSet = new HashSet<>();
        // 5.5 多列组合验重set
        Set<String> uniteVerifyDuplicationSet = new HashSet<>();
        // 5.6 错误信息
        Map<Integer, StringBuffer> errorMap = new HashMap<>();

        // 6. 遍历表格数据
        for (int i = firstRow; i <= lastRow; i++) {
            // 初始化类对象
            T tClass = clazz.newInstance();
            // 定义验证成功与否标志
            boolean isSuccess = true;
            // 经过验证后得到的错误信息
            StringBuffer buffer = new StringBuffer();
            // 单行多列数据拼接，用于多行联合验重
            StringBuilder uniteCellsBuffer = new StringBuilder();
            for (int j = firstCell; j <= firstCell + cellCount - 1; j++) {
                if (sheetAt.getRow(i) == null) {
                    if (!rule.getNotNullCell()) {
                        // 当配置表格数据可以为空
                        sheetAt.createRow(i).createCell(j).setCellValue(ExcelUtil.SET_NULL);
                    } else {
                        // 当配置表格数据不可以为空
                        buffer.append("行不能为空,");
                        isSuccess = false;
                        break;
                    }
                }
                if (sheetAt.getRow(i).getCell(j) == null) {
                    if (!rule.getNotNullCell()) {
                        // 当配置表格数据可以为空
                        sheetAt.getRow(i).createCell(j).setCellValue(ExcelUtil.SET_NULL);
                    } else {
                        if (titleRow != null && sheetAt.getRow(titleRow) != null && sheetAt.getRow(titleRow).getCell(j) != null) {
                            // 当标题存在时，错误信息按照标题提示
                            buffer.append(sheetAt.getRow(titleRow).getCell(j).getStringCellValue()).append("不能为空!");
                        } else {
                            buffer.append("第").append(j + 1).append("列不能有空值,");
                        }
                        isSuccess = false;
                        break;
                    }
                }
                // 允许对对象中的属性进行操作
                fields[j].setAccessible(true);
                // 将每一个单元格中的内容都视为String类型[统一处理，否则传过来的类型会被自动识别成各种乱七八糟的类型，不利于之后的转换]
                sheetAt.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                // 单列验重
                if (Arrays.asList(rule.getVerifyDuplicationCells()).contains(j + 1)) {
                    if (!ExcelUtil.SET_NULL.equals(sheetAt.getRow(i).getCell(j).getStringCellValue().trim())) {
                        // 非空数据验重[为啥 空数据不验证呢 = = 暂时没想好为啥]
                        if (!verifyDuplicationSet.add(j + "_" + sheetAt.getRow(i).getCell(j).getStringCellValue().trim())) {
                            if (titleRow != null && sheetAt.getRow(titleRow) != null && sheetAt.getRow(titleRow).getCell(j) != null) {
                                buffer.append(sheetAt.getRow(titleRow).getCell(j).getStringCellValue()).append("存在重复");
                            } else {
                                buffer.append("第").append(j + 1).append("列数据存在重复,");
                            }
                            isSuccess = false;
                        }
                    }
                }
                // 多列联合验重（思想是，把这几个数据拼接成字符串，最后利用set即可达到验证制定的多列数据的效果）
                if (Arrays.asList(rule.getUniteVerifyDuplicationCells()).contains(j + 1)) {
                    uniteCellsBuffer.append(sheetAt.getRow(i).getCell(j).getStringCellValue().trim());
                }
                // 获取对象中指定属性的属性类型
                Class<?> type = fields[j].getType();
                // 根据属性类型对表格数据进行转换，转换出现异常时记录错误信息
                Object o = typeTransfer(type, sheetAt.getRow(i).getCell(j).getStringCellValue());
                if (ExcelUtil.INVALID_DATA.equals(o)) {
                    if (titleRow != null && sheetAt.getRow(titleRow) != null && sheetAt.getRow(titleRow).getCell(j) != null) {
                        buffer.append(sheetAt.getRow(titleRow).getCell(j).getStringCellValue()).append("数据类型无效，已当做空值处理");
                    } else {
                        buffer.append("第").append(j + 1).append("列数据类型无效，已当做空值处理，请检查,");
                    }
                    isSuccess = false;
                    break;
                }
                // 将值赋给对象中对应属性
                fields[j].set(tClass, o);
            }
            // 处理多列联合验重
            if (uniteCellsBuffer.length() > 0 && !uniteVerifyDuplicationSet.add(uniteCellsBuffer.toString())) {
                if (titleRow != null && sheetAt.getRow(titleRow) != null && sheetAt.getRow(titleRow).getCell(rule.getUniteVerifyDuplicationCells()[0] - 1) != null) {
                    buffer.append(sheetAt.getRow(titleRow).getCell(rule.getUniteVerifyDuplicationCells()[0] - 1).getStringCellValue()).append("数据重复");
                } else {
                    buffer.append("无效数据，第").append(Arrays.toString(rule.getUniteVerifyDuplicationCells())).append("列中数据联和存在重复");
                }
            }
            if (buffer.length() > 0) {
                errorMap.put(i, buffer);
            }
            rule.getErrorMap().putAll(errorMap);
            if (rule.getOneFailAllFail()) {
                // 当一列出错所有数据返回时
                failedList.add(tClass);
            } else {
                if (isSuccess) {
                    successList.add(tClass);
                } else {
                    failedList.add(tClass);
                }
            }
            resultList.add(tClass);
        }

        // 7. 处理结果数据
        rule.setFailedList(failedList);
        rule.setSuccessList(successList);
        rule.setResultList(resultList);
        rule.setFailedCount(rule.getFailedList().size());
        rule.setSuccessCount(rule.getSuccessList().size());
        return rule;
    }

    /**
     * 将字符串str转为clazz对应的类型
     *
     * @param clazz 位置类
     * @param str   源字符串
     * @return 返回转换后得到的对象
     */
    private static Object typeTransfer(Class<?> clazz, String str) {
        String clazzStr = clazz.toString();
        if (str.equals(ExcelUtil.SET_NULL)) {
            return null;
        }
        try {
            if (clazzStr.endsWith(ExcelUtil.INTEGER) || clazzStr.endsWith(ExcelUtil.INT)) {
                return Integer.valueOf(str.trim());
            }
            if (clazzStr.endsWith(ExcelUtil.M_LONG) || clazzStr.endsWith(ExcelUtil.LONG)) {
                return Long.valueOf(str.trim());
            }
            if (clazzStr.endsWith(ExcelUtil.DOUBLE) || clazzStr.endsWith(ExcelUtil.M_DOUBLE)) {
                return Double.valueOf(str.trim());
            }
            if (clazzStr.endsWith(ExcelUtil.FLOAT) || clazzStr.endsWith(ExcelUtil.M_FLOAT)) {
                return Float.valueOf(str.trim());
            }
            if (clazzStr.endsWith(ExcelUtil.BIGDECIMAL)) {
                return new BigDecimal(str.trim());
            }
            if (clazzStr.endsWith(ExcelUtil.LOCALDATE)) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(str, fmt);
            }
            if (clazzStr.endsWith(ExcelUtil.LOCALDATETIME)) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDate.parse(str, fmt);
            }
            if (clazzStr.endsWith(ExcelUtil.LOCALTIME)) {
                return LocalTime.parse(str, DateTimeFormatter.ISO_TIME);
            } else {
                return str;
            }
        } catch (Exception e) {
            return ExcelUtil.INVALID_DATA;
        }
    }


    /**
     * 将写入错误信息的文件流建议存到redis提供下载，本demo中不做演示
     *
     * @param rule
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static byte[] writeErrorMessageToExcel(ExcelImportObject rule) throws IOException {
        // 1. 获取到ExcelRule中错误信息[该错误信息在数据验证中写入]
        Map<Integer, StringBuffer> errorMap = new HashMap<>(rule.getErrorMap());

        // 2. 类似于第一个方法，重新获取到文件流，相当于重新初始化用户上传的文件
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Integer lastCell = rule.getFirstCellNum() + rule.getCellCount() - 1;
        InputStream inputStream = new ByteArrayInputStream(rule.getFileInputStream());
        Workbook workbook = new XSSFWorkbook(inputStream);

        // 3. 定义了一个加粗字体的Style，应用在标题上
        CellStyle titleCellStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleCellStyle.setFont(titleFont);

        // 4. 定义使字体变成红色的Style，把错误的信息用红色字体展示
        CellStyle redFontCellStyle = workbook.createCellStyle();
        Font redFont = workbook.createFont();
        redFont.setColor(Font.COLOR_RED);
        redFontCellStyle.setFont(redFont);

        Sheet sheetAt = workbook.getSheetAt(0);

        // 当标题行存在时，在最后一列的后一列添加标题：失败原因
        Integer titleRow = rule.getTitleRowNum() == null ? null : rule.getTitleRowNum() - 1;
        if (titleRow != null) {
            if (sheetAt.getRow(titleRow) == null) {
                sheetAt.createRow(titleRow);
            }
            sheetAt.getRow(titleRow).createCell(lastCell).setCellValue("失败原因");
            for (int i = 0; i <= lastCell; i++) {
                if (sheetAt.getRow(titleRow).getCell(i) == null) {
                    sheetAt.getRow(titleRow).createCell(i);
                }
                sheetAt.getRow(titleRow).getCell(i).setCellStyle(titleCellStyle);
            }
        }

        // 根据传入的错误信息进行核心内容错误数据写入
        for (Map.Entry<Integer, StringBuffer> entry : errorMap.entrySet()) {
            Integer rowNum = entry.getKey();
            if (sheetAt.getRow(rowNum) == null) {
                sheetAt.createRow(rowNum).createCell(lastCell).setCellValue(entry.getValue().toString());
            } else {
                sheetAt.getRow(rowNum).createCell(lastCell).setCellValue(entry.getValue().toString());
            }
            sheetAt.getRow(rowNum).getCell(lastCell).setCellStyle(redFontCellStyle);
        }

        // 非一错全错时
        if (!rule.getOneFailAllFail()) {
            int i = rule.getFirstRowNum() - 1;
            for (; i < sheetAt.getLastRowNum(); i++) {
                if (sheetAt.getRow(sheetAt.getLastRowNum()).getCell(lastCell) == null) {
                    sheetAt.removeRow(sheetAt.getRow(sheetAt.getLastRowNum()));
                } else {
                    break;
                }
            }
            i = rule.getFirstRowNum() - 1;
            for (; i < sheetAt.getLastRowNum(); i++) {
                int j = i + 1;
                if (sheetAt.getRow(i).getCell(lastCell) == null) {
                    sheetAt.shiftRows(j, sheetAt.getLastRowNum(), -1);
                    i = i - 1;
                }
            }
        }
        // 得到添加了错误信息的表格文件，随后将其转换成文件流
        workbook.write(os);
        inputStream.close();
        os.close();
        return os.toByteArray();
    }

    /**
     * 在对转换得到的对象进行逻辑校验时，使用List遍历校验，传入相应参数自动处理错误信息
     *
     * @param rule         规则参数，填写转换excelStreamToList方法的输出对象
     * @param i            循环的index [0开始]
     * @param errorMessage [错误信息]
     * @param <T>          泛型
     */
    public static <T extends ExcelModelRequest> void errorMessageDeal(ExcelImportObject<T> rule, int i, StringBuffer errorMessage) {
        if (errorMessage.length() > 0) {
            if (rule.getErrorMap().get(i + 1) == null) {
                rule.getErrorMap().put(i + 1, errorMessage);
            } else {
                rule.getErrorMap().put(i + 1, errorMessage.append(rule.getErrorMap().get(i + 1)));
            }
        } else if (rule.getErrorMap().get(i + 1) == null && !rule.getOneFailAllFail()) {
            rule.getImportList().add(rule.getResultList().get(i));
        }
    }
}

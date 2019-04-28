package com.poi.exceldeal.util;

import java.io.Serializable;

/**
 * <p>
 * 导出为excel用于放置参数的对象的父类
 * 凡继承该类，请勿在类中进行单独的序列化
 * 即不要再写 private static final long serialVersionUID = ********
 * 否则接收参数出错
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/12/13 10:03
 */
public class ExcelModelExport implements Serializable {

    private static final long serialVersionUID = -8255316374396955950L;
}

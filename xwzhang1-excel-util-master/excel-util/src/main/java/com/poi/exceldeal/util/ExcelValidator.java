package com.poi.exceldeal.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangxianwen
 * Created 2018/11/14 19:54
 */
public class ExcelValidator {
    private static Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    public static <T extends ExcelModelRequest> ExcelImportObject<T> excelValidate(ExcelImportObject<T> transferResult) {
        return excelValidateImpl(transferResult);
    }

    public static <T extends ExcelModelRequest> ExcelImportObject<T> excelValidateImpl(ExcelImportObject<T> transferResult) {

        if (transferResult.getResultList() == null || transferResult.getResultList().size() <= 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("核心数据为空");
            transferResult.getErrorMap().put(1, sb);
        } else {
            for (int i = 0, length = transferResult.getResultList().size(); i < length; i++) {
                Set<ConstraintViolation<T>> objectSet = validator.validate(transferResult.getResultList().get(i));
                if (objectSet != null && objectSet.size() > 0) {
                    Map<String, StringBuffer> map = dealConstraintViolations(objectSet);
                    Set<String> set = map.keySet();

                    int finalI = i + 1;
                    set.forEach(s -> {
                        if (transferResult.getErrorMap().get(finalI) == null) {
                            transferResult.getErrorMap().put(finalI, map.get(s));
                        } else {
                            transferResult.getErrorMap().put(finalI, transferResult.getErrorMap().get(finalI).append(map.get(s)));
                        }
                    });
                }
            }
        }
        return transferResult;
    }

    private static Map<String, StringBuffer> dealConstraintViolations(Set<? extends ConstraintViolation<?>> constraintViolations) {
        Map<String, StringBuffer> errorMap = new HashMap<>();

        if (constraintViolations != null && constraintViolations.size() > 0) {
            String property;
            for (ConstraintViolation<?> cv : constraintViolations) {
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append(",").append(cv.getMessage());
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap;
    }
}

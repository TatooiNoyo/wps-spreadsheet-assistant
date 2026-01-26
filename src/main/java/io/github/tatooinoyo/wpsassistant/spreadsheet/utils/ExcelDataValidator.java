package io.github.tatooinoyo.wpsassistant.spreadsheet.utils;

import io.github.tatooinoyo.wpsassistant.spreadsheet.ImportError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.*;

/**
 * Excel数据校验工具类
 * 使用Jakarta Validation注解对导入数据进行校验
 * 
 * @author Tatooi Noyo
 * @since 1.3.0
 */
public class ExcelDataValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private ExcelDataValidator() {
        // 工具类，私有构造函数
    }

    /**
     * 校验单个对象
     * @param object 待校验对象
     * @param rowNumber 行号（用于错误定位）
     * @return 错误列表
     */
    public static <T> List<ImportError> validate(T object, int rowNumber) {
        List<ImportError> errors = new ArrayList<>();
        
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        for (ConstraintViolation<T> violation : violations) {
            ImportError error = new ImportError();
            error.setRowNumber(rowNumber);
            error.setFieldName(violation.getPropertyPath().toString());
            error.setErrorMessage(violation.getMessage());
            errors.add(error);
        }
        
        return errors;
    }

    /**
     * 校验单个对象（带原始值）
     * @param object 待校验对象
     * @param rowNumber 行号（用于错误定位）
     * @param originalValues 原始值Map（字段名 -> 原始值）
     * @return 错误列表
     */
    public static <T> List<ImportError> validate(T object, int rowNumber, Map<String, Object> originalValues) {
        List<ImportError> errors = new ArrayList<>();
        
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        for (ConstraintViolation<T> violation : violations) {
            ImportError error = new ImportError();
            error.setRowNumber(rowNumber);
            error.setFieldName(violation.getPropertyPath().toString());
            error.setErrorMessage(violation.getMessage());
            
            // 设置原始值用于定位问题
            String fieldName = violation.getPropertyPath().toString();
            if (originalValues != null && originalValues.containsKey(fieldName)) {
                error.setOriginalValue(originalValues.get(fieldName));
            }
            
            errors.add(error);
        }
        
        return errors;
    }

    /**
     * 校验列表中的所有对象
     * @param objects 待校验对象列表
     * @return 错误列表（按行号排序）
     */
    public static <T> List<ImportError> validateAll(List<T> objects) {
        List<ImportError> allErrors = new ArrayList<>();
        
        for (int i = 0; i < objects.size(); i++) {
            List<ImportError> errors = validate(objects.get(i), i + 1); // 行号从1开始
            allErrors.addAll(errors);
        }
        
        return allErrors;
    }

    /**
     * 判断是否有校验错误
     * @param errors 错误列表
     * @return true表示有错误
     */
    public static boolean hasErrors(List<ImportError> errors) {
        return errors != null && !errors.isEmpty();
    }

    /**
     * 将错误列表转换为友好的错误消息
     * @param errors 错误列表
     * @return 错误消息字符串
     */
    public static String formatErrors(List<ImportError> errors) {
        if (errors == null || errors.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (ImportError error : errors) {
            sb.append(String.format("第%d行 [%s]: %s%n", 
                    error.getRowNumber(), 
                    error.getFieldName(), 
                    error.getErrorMessage()));
        }
        
        return sb.toString();
    }
}

package io.github.dovemy.hami.web.exception;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;

/**
 * 全局异常处理器
 *
 * @author xuhaoming
 * @since 2020/04/22
 */
@Slf4j
@Component
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity businessException(HttpServletRequest req, BusinessException e) {
        log.warn(e.getMsg());
        return ResponseEntity.status(e.getStatus()).contentType(MediaType.APPLICATION_JSON).body(new ErrorResponse(e.getCode(), e.getMsg()));
    }

    /**
     * RequestBody @valid 参数校验
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("methodArgumentNotValidException:", e);
        StringBuilder message = new StringBuilder();
        if (!CollectionUtils.isEmpty(e.getBindingResult().getAllErrors())) {
            for (ObjectError error : e.getBindingResult().getAllErrors()) {
               message.append(error.getDefaultMessage()).append(";");
            }
        } else {
            message.append(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("参数异常：" + message);
    }

    /**
     * Controller方法入参校验
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException e) {
        log.warn("constraintViolationException:{}", e.getMessage());
        String message = formatConstraintViolationException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("参数异常：" + message);
    }

    /**
     * 参数适配错误
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("methodArgumentNotValidException:{}", e.getMessage());
        String valueClass = e.getValue() != null ? e.getValue().getClass().getSimpleName() : "value class";
        String requiredClass = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "required class";
        String message = String.format(":%s[%s(%s)]无法转为%s", e.getName(), e.getValue(), valueClass, requiredClass);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("参数异常：" + message);
    }

    /**
     * 文件上传大小超出限制异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("maxUploadSizeExceededException:", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("超出文件上传大小最大值");
    }


    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity nullPointerException(NullPointerException e) {
        log.error(" null pointer exception ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("服务器异常，请稍后再试");
    }

    /**
     * 其余异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity globalException(Exception e) {
        log.error("", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body(e.getMessage());
    }

    /**
     * 格式化validation约束异常信息
     * @param e 异常
     * @return 异常信息 e.g. name不能为空;size不能小于0
     */
    public static String formatConstraintViolationException(ConstraintViolationException e) {
        StringBuffer message = new StringBuffer();
        String detailMessage = e.getMessage();
        if (StrUtil.isNotBlank(detailMessage)) {
            String[] errorList = detailMessage.split(",");
            for (String error : errorList) {
                message.append(error.substring(error.indexOf(":") + 1)).append(";");
            }
        }
        return message.toString();
    }

}

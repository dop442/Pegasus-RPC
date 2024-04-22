package com.zc.rpc.admin.server.config;

import com.zc.rpc.admin.bean.exception.*;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-01
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConfigurationParsingException.class)
    public ResponseEntity<Result<String>> handleConfigurationParsingException(ConfigurationParsingException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "配置信息错误", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Result<String>> handleClassNotFoundException(ClassNotFoundException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "不支持该参数类型", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Result<String>> handleNumberFormatException(NumberFormatException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "参数类型错误", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(ConfigureDuplicateException.class)
    public ResponseEntity<Result<String>> handleConfigureDuplicateException(ConfigureDuplicateException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "配置错误", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(ParametersCountException.class)
    public ResponseEntity<Result<String>> handleParametersCountException(ParametersCountException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "参数数量错误", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(TokenAuthExpiredException.class)
    public ResponseEntity<Result<String>> handleTokenAuthExpiredException(TokenAuthExpiredException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "token过期", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(TokenAuthEmptyException.class)
    public ResponseEntity<Result<String>> handleTokenAuthEmptyException(TokenAuthEmptyException ex){
        Result<String> result = new Result<>(HttpCode.FAILURE, "token为空", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}

package com.example.vitalis.exception;

import com.example.vitalis.common.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {
    private String message;
    private String code;

    public CustomException(ResultCodeEnum resultCodeEnum) {
        this.message = resultCodeEnum.message;
        this.code = resultCodeEnum.code;
    }

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

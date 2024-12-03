package com.example.vitalis.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "成功"),

    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "参数缺失"),

    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "用户名已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    PLAN_EXIST_ERROR("5006", "该医生已有当天的排班，请勿重复添加"),
    DEPARTMENT_EXIST_ERROR("5007", "该科室已存在"),
    DISEASE_EXIST_ERROR("5008", "该疾病已存在"),
    MEDICINE_EXIST_ERROR("5009", "该药品已存在"),
    PRESCRIPTION_EXIST_ERROR("5010", "该处方已存在"),
    ;

    public String code;
    public String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

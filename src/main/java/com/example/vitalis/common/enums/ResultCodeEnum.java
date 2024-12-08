package com.example.vitalis.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "成功"),

    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("402", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "参数缺失"),
    PARAM_ROLE_ERROR("4002", "角色错误"),

    SYSTEM_ERROR("500", "系统异常"),

    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),

    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),

    USER_EXIST_ERROR("5001", "用户名已存在"),
    DEPARTMENT_EXIST_ERROR("5002", "该科室已存在"),
    DISEASE_EXIST_ERROR("5004", "该疾病已存在"),
    MEDICINE_EXIST_ERROR("5006", "该药品已存在"),
    PRESCRIPTION_EXIST_ERROR("5007", "该处方已存在"),
    REGISTRATION_EXIST_ERROR("5008", "该挂号已存在"),

    USER_NOT_EXIST_ERROR("50011", "用户不存在"),
    DEPARTMENT_NOT_EXIST_ERROR("5012", "不存在此id的科室"),
    DOCTOR_NOT_EXIST_ERROR("5013", "不存在此id的医生"),
    PATIENT_NOT_EXIST_ERROR("5014", "不存在此id的患者"),
    DISEASE_NOT_EXIST_ERROR("5015", "不存在此id的疾病"),
    MEDICINE_NOT_EXIST_ERROR("5016", "不存在此id的药品"),
    PRESCRIPTION_NOT_EXIST_ERROR("5017", "不存在此id的处方"),
    REGISTRATION_NOT_EXIST_ERROR("5018", "不存在此id的挂号")
    ;

    public String code;
    public String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

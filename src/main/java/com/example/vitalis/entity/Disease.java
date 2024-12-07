package com.example.vitalis.entity;

/**
 * 疾病实体类
 */
public class Disease {

    /** 疾病ID */
    private Integer id;
    /** 疾病名称 */
    private String name;
    /** 症状 */
    private String symptoms;
    /** 描述 */
    private String description;
    /** 预防方法 */
    private String prevent;
    /** 病因 */
    private String cause;
    /** 传染性 */
    private String epidemic;
    /** 推荐检查 */
    private String check;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrevent() {
        return prevent;
    }

    public void setPrevent(String prevent) {
        this.prevent = prevent;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getEpidemic() {
        return epidemic;
    }

    public void setEpidemic(String epidemic) {
        this.epidemic = epidemic;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }



}
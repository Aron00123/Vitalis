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
    /** 后遗症 */
    private String sequelae;
    /** 潜伏期 */
    private String incubationPeriod;

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

    public String getSequelae() {
        return sequelae;
    }

    public void setSequelae(String sequelae) {
        this.sequelae = sequelae;
    }

    public String getIncubationPeriod() {
        return incubationPeriod;
    }

    public void setIncubationPeriod(String incubationPeriod) {
        this.incubationPeriod = incubationPeriod;
    }
}
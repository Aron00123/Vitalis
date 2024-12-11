package com.example.vitalis.entity;

/**
 * 医生实体类
 */
public class Doctor implements Cloneable{
    /** 身份证号 */
    private String id;
    /** 姓名 */
    private String name;
    /** 性别 */
    private String gender;
    /** 职称 */
    private String title;
    /** 主治疾病 */
    private String specialty;
    /** 科室ID */
    private Integer departId;
    /** 就诊限额 */
    private Integer consultLimit;
    /** 坐诊日 */
    private String consultDays;
    /** 联系电话 */
    private String phone;
    /** 照片 */
    private String photo;

    private String description;

    private String departmentName;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); // 浅克隆
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public Integer getConsultLimit() {
        return consultLimit;
    }

    public void setConsultLimit(Integer consultLimit) {
        this.consultLimit = consultLimit;
    }

    public String getConsultDays() {
        return consultDays;
    }

    public void setConsultDays(String consultDays) {
        this.consultDays = consultDays;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
package com.example.vitalis.entity;

/**
 * 患者实体类
 */
public class Patient {
    /** 身份证号 */
    private String id;
    /** 性别 */
    private String gender;
    /** 姓名 */
    private String name;
    /** 年龄 */
    private Integer age;
    /** 家庭住址 */
    private String address;
    /** 联系电话 */
    private String phone;
    /** 紧急联系人电话 */
    private String emergencyPhone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public Patient(String id, String gender, String name, Integer age, String address, String phone, String emergencyPhone) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.emergencyPhone = emergencyPhone;
    }
}
package com.example.vitalis.entity;

/**
 * 预约挂号实体类
 */
public class Registration {
    private  Integer registrationId;
    /** 预约科室ID */
    private Integer departmentId;
    /** 预约医生身份证号 */
    private String doctorId;
    /** 病人身份证号 */
    private String patientId;
    /** 就诊日期 */
    private String appointDate;
    /** 就诊地址 */
    private String appointAddress;

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getAppointAddress() {
        return appointAddress;
    }

    public void setAppointAddress(String appointAddress) {
        this.appointAddress = appointAddress;
    }
}
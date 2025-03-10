package com.example.vitalis.entity;

import java.util.List;

/**
 * 处方实体类
 */
public class Prescription {
    /** 处方ID */
    private Integer prescriptionId;
    /** 病人身份证号 */
    private String patientId;
    /** 医生身份证号 */
    private String doctorId;
    /** 所诊疾病ID */
    private String disease;
    /** 所开药品的ID列表 */
    private String medicine;
    /** 对应挂号单ID */
    private Integer registrationId;
    /** 医嘱 */
    private String medicalAdvice;

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public String getMedicalAdvice() {
        return medicalAdvice;
    }

    public void setMedicalAdvice(String medicalAdvice) {
        this.medicalAdvice = medicalAdvice;
    }
}
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
    private Integer diseaseId;
    /** 所开药品的ID列表 */
    private List<Integer> medicineIds;
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

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public List<Integer> getMedicineIds() {
        return medicineIds;
    }

    public void setMedicineIds(List<Integer> medicineIds) {
        this.medicineIds = medicineIds;
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
package com.example.vitalis.entity;

public class RegistrationForShow {
    private  Integer id;
    private String name;
    private  String doctor;
    private  String departmentName;
    private  String date;
    private  String status;

    public RegistrationForShow(Integer id, String name, String doctor, String departmentName, String date, String status) {
        this.id = id;
        this.name = name;
        this.doctor = doctor;
        this.departmentName = departmentName;
        this.date = date;
        this.status = status;
    }

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

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

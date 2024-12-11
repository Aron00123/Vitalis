package com.example.vitalis.entity;

public class DoctorPatientAccount {
    /** 身份证号，作为账号 */
    private String id;
    /** 密码 */
    private String password;
    /** 新密码 */
    private String newPassword;
    /** 角色 */
    private String role;
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
    /** 照片 */
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setByAccountAndPatient(Account account, Patient patient, Doctor doctor) {
        DoctorPatientAccount doctorPatientAccount = this;

        // 设置 Account 中的字段
        doctorPatientAccount.setId(account.getId());  // 账号的身份证号
        doctorPatientAccount.setPassword(account.getPassword());  // 密码
        doctorPatientAccount.setNewPassword(account.getNewPassword());  // 新密码
        doctorPatientAccount.setRole(account.getRole());  // 角色

        // 设置 Patient 中的字段
        if (patient != null) {
            doctorPatientAccount.setGender(patient.getGender());  // 性别
            doctorPatientAccount.setName(patient.getName());  // 姓名
            doctorPatientAccount.setAge(patient.getAge());  // 年龄
            doctorPatientAccount.setAddress(patient.getAddress());  // 家庭住址
            doctorPatientAccount.setPhone(patient.getPhone());  // 联系电话
            doctorPatientAccount.setEmergencyPhone(patient.getEmergencyPhone());  // 紧急联系人电话
        }

        if (doctor != null) {
            doctorPatientAccount.setName(doctor.getName());
            doctorPatientAccount.setGender(doctor.getGender());
            doctorPatientAccount.setTitle(doctor.getTitle());
            doctorPatientAccount.setSpecialty(doctor.getSpecialty());
            doctorPatientAccount.setDepartId(doctor.getDepartId());
            doctorPatientAccount.setConsultLimit(doctor.getConsultLimit());
            doctorPatientAccount.setConsultDays(doctor.getConsultDays());
            doctorPatientAccount.setPhoto(doctor.getPhoto());
            doctorPatientAccount.setConsultDays(doctor.getConsultDays());
        }
    }

    public Account getAccount() {
        Account account = new Account();
        account.setId(this.getId());
        account.setPassword(this.getPassword());
        account.setNewPassword(this.getNewPassword());
        account.setRole(this.getRole());
        return account;
    }

}

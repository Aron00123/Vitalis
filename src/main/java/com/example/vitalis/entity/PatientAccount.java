package com.example.vitalis.entity;

public class PatientAccount {
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

    public void setByAccountAndPatient(Account account, Patient patient) {
        PatientAccount patientAccount = this;

        // 设置 Account 中的字段
        patientAccount.setId(account.getId());  // 账号的身份证号
        patientAccount.setPassword(account.getPassword());  // 密码
        patientAccount.setNewPassword(account.getNewPassword());  // 新密码
        patientAccount.setRole(account.getRole());  // 角色

        // 设置 Patient 中的字段
        patientAccount.setGender(patient.getGender());  // 性别
        patientAccount.setName(patient.getName());  // 姓名
        patientAccount.setAge(patient.getAge());  // 年龄
        patientAccount.setAddress(patient.getAddress());  // 家庭住址
        patientAccount.setPhone(patient.getPhone());  // 联系电话
        patientAccount.setEmergencyPhone(patient.getEmergencyPhone());  // 紧急联系人电话
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

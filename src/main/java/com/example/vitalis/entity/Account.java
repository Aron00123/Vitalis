package com.example.vitalis.entity;

/**
 * 账户实体类
 */
public class Account {
    /** 身份证号，作为账号 */
    private String id;
    /** 密码 */
    private String password;
    /** 新密码 */
    private String newPassword;
    /** 角色 */
    private String role;

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

    public void setNewPassword(String password) {
        this.newPassword = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
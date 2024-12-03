package com.example.vitalis.entity;

/**
 * 科室实体类
 */
public class Department {
    /** 科室ID */
    private Integer departId;
    /** 科室类型 */
    private String type;
    /** 科室主任身份证号 */
    private String directorId;
    /** 科室副主任身份证号 */
    private String deputyId;
    /** 医生数量 */
    private Integer docNum;
    /** 所属楼层 */
    private String floor;

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirectorId() {
        return directorId;
    }

    public void setDirectorId(String directorId) {
        this.directorId = directorId;
    }

    public String getDeputyId() {
        return deputyId;
    }

    public void setDeputyId(String deputyId) {
        this.deputyId = deputyId;
    }

    public Integer getDocNum() {
        return docNum;
    }

    public void setDocNum(Integer docNum) {
        this.docNum = docNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
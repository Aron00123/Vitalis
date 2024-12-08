package com.example.vitalis.entity;

/**
 * 药品实体类
 */
public class Medicine {
    /** 药品ID */
    private String id;
    /** 药品名称 */
    private String name;
    /** 有效日期 */
    private String expiryDate;
    /** 使用计量 */
    private String dosage;
    /** 药品作用 */
    private String effects;
    /** 库存量 */
    private Integer stock;
    /** 价格 */
    private Double price;

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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getEffects() {
        return effects;
    }

    public void setEffects(String effects) {
        this.effects = effects;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
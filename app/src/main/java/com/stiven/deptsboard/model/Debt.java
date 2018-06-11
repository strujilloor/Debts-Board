package com.stiven.deptsboard.model;

public class Debt {
    private String id;
    private String name;
    private Double amount;
    private String details;
    private boolean type;

    public Debt() {
    }

//    public Debt(String name, String amount) {
//        this.name = name;
//        this.amount = amount;
//    }
//
//    public Debt(String id, String name, String amount) {
//        this.id = id;
//        this.name = name;
//        this.amount = amount;
//    }

    public Debt(String id, String name, Double amount, boolean type, String details) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type; // true borrowed
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}

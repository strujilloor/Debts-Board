package com.stiven.deptsboard.model;

public class Debt {
    private String id;
    private String name;
    private String amount;

    public Debt() {
    }

    public Debt(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public Debt(String id, String name, String amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}

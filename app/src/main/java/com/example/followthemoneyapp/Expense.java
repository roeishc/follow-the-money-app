package com.example.followthemoneyapp;

public class Expense {

    private String u_id; // unique identifier
    private String categoryName;
    private String amount;
    private String date;
    private String note;

    public Expense(String u_id, String categoryName, String amount, String date, String note){
        this.u_id = u_id;
        this.categoryName = categoryName;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getU_id() {
        return u_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

}

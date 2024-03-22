package com.example.followthemoneyapp;

public class Category {
    private String name; // unique identifier
    private String amount; // how many expenses there are in this category

    public Category(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public void incrementAmount(int delta){
        int a = Integer.parseInt(amount);
        a += delta;
        amount = a + "";
    }

    public String getName(){
        return name;
    }

    public String getAmount() {return amount;}

    @Override
    public String toString(){
        return name;
    }
}

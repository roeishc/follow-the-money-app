package com.example.followthemoneyapp;

import java.util.ArrayList;

public class ExpensesSingleton {

    private static ExpensesSingleton instance;
    private ArrayList<Expense> expenses = new ArrayList<>();

    private ExpensesSingleton(){}

    public static synchronized ExpensesSingleton getInstance(){
        if (instance == null)
            instance = new ExpensesSingleton();
        return instance;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public String getSmsDescription(){
        int total = getTotalExpensesCost();
        return "Using the app \"Follow the Money\", I'm now tracking " + expenses.size() + " expenses, costing a total of " + total + "!";
    }

    public int getTotalExpensesCost(){
        int total = 0;
        for (int i = 0; i < expenses.size(); i++)
            total += Integer.parseInt(expenses.get(i).getAmount());
        return total;
    }
}

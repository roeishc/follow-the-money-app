package com.example.followthemoneyapp;

import java.util.ArrayList;

public class CategoriesSingleton {

    private static CategoriesSingleton instance;
    private ArrayList<Category> categories = new ArrayList<>();

    private CategoriesSingleton(){}

    public static synchronized CategoriesSingleton getInstance(){
        if (instance == null)
            instance = new CategoriesSingleton();
        return instance;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}

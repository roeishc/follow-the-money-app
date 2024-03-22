package com.example.followthemoneyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    ArrayList<Expense> expenses = new ArrayList<>();
    ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        RelativeLayout relativeLayout = findViewById(R.id.splashScreen);
        relativeLayout.setBackgroundColor(Color.WHITE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        getExpensesFromDb();
                        ExpensesSingleton.getInstance().setExpenses(expenses);
                        getCategoriesFromDb();
                        CategoriesSingleton.getInstance().setCategories(categories);
                        while(!isFinishedLoadingDataFromDb()) // wait for data from db
                            wait(500);
                        Intent i = new Intent (SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getExpensesFromDb(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(AddExpenseActivity.EXPENSE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if there any expenses. if there are, fetch them
                if (snapshot.exists()){
                    collectExpenses((Map<String, Object>) snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Cannot fetch data, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Expense getExpense(String u_id){
        for (int i = 0; i < expenses.size(); i++){
            if (expenses.get(i).getU_id().equals(u_id))
                return expenses.get(i);
        }
        return null;
    }

    private void collectExpenses(Map<String, Object> expensesFromDb){
        for (Map.Entry<String, Object> entry: expensesFromDb.entrySet()){
            // expense map
            Map singleExpense = (Map)entry.getValue();

            Expense expense = getExpense((String)singleExpense.get("u_id"));
            if (expense == null){   // if the expense isn't in the arraylist, add it
                // get all data members
                expenses.add(new Expense(
                        (String)singleExpense.get("u_id"),
                        (String)singleExpense.get("categoryName"),
                        (String)(singleExpense.get("amount") + ""),
                        (String)singleExpense.get("date"),
                        (String)singleExpense.get("note")));
            }
        }
    }

    public void getCategoriesFromDb(){
        // fetch expenses from db
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(com.example.followthemoneyapp.ExpensesSummaryActivity.CATEGORY);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear(); // for reloading data after removing an element. prevents duplication in recyclerview
                if (snapshot.exists()) {
                    collectCategories((Map<String, Object>) snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Cannot fetch data, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void collectCategories(Map<String, Object> categoriesFromDb){
        for (Map.Entry<String, Object> entry: categoriesFromDb.entrySet()){
            // category map
            Map singleCategory = (Map)entry.getValue();
            // get all data members
            categories.add(new Category((String)singleCategory.get("name"),
                    (String)(singleCategory.get("amount") + "")));
        }
    }

    private boolean isFinishedLoadingDataFromDb(){
        return categories.size() > 0 && expenses.size() > 0;
    }
}
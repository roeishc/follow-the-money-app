package com.example.followthemoneyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class ViewExpensesActivity extends AppCompatActivity implements ViewExpensesRecyclerViewInterface {

    private boolean reloadNeeded = false;

    RecyclerView recyclerView;
    ViewExpensesRecyclerViewAdapter adapter;
    ArrayList<Expense> expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);
        
        expenses = ExpensesSingleton.getInstance().getExpenses();

        // create recycler view
        recyclerView = findViewById(R.id.ViewExpensesRecyclerView);
        adapter = new ViewExpensesRecyclerViewAdapter(this, ExpensesSingleton.getInstance().getExpenses(), this);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (expenses.isEmpty())
            Toast.makeText(getApplicationContext(), "No expenses to display", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int position) {
        reloadNeeded = true;
        Intent i = new Intent (ViewExpensesActivity.this, AddExpenseActivity.class);
        i.putExtra(AddExpenseActivity.U_ID, expenses.get(position).getU_id());
        i.putExtra(AddExpenseActivity.CATEGORY_NAME, expenses.get(position).getCategoryName());
        i.putExtra(AddExpenseActivity.AMOUNT, expenses.get(position).getAmount());
        i.putExtra(AddExpenseActivity.DATE, expenses.get(position).getDate());
        i.putExtra(AddExpenseActivity.NOTE, expenses.get(position).getNote());
        startActivity(i);
    }

    @Override
    public void onItemLongClick(int position) {
        // remove expense from the db
        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference(AddExpenseActivity.EXPENSE).child(expenses.get(position).getU_id());
        dbNode.removeValue();

        // decrement amount in category in db
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(com.example.followthemoneyapp.ExpensesSummaryActivity.CATEGORY);
        myRef.child(expenses.get(position).getCategoryName()).child(AddExpenseActivity.AMOUNT).setValue(ServerValue.increment(-1));

        // remove expense from the recyclerview
        expenses.remove(position);
        adapter.notifyItemRemoved(position);

        Toast.makeText(getApplicationContext(),"Removed", Toast.LENGTH_SHORT).show();
    }

    // update info if returning from the edit activity
    @Override
    public void onResume(){
        super.onResume();
        if (reloadNeeded)
            adapter.notifyDataSetChanged();
        reloadNeeded = false;
    }
}
package com.example.followthemoneyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesSummaryRecyclerViewAdapter extends RecyclerView.Adapter<ExpensesSummaryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category> categories;
    ArrayList<Expense> expenses;

    public ExpensesSummaryRecyclerViewAdapter(Context context, ArrayList<Category> categories, ArrayList<Expense> expenses){
        this.context = context;
        this.categories = categories;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpensesSummaryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout (give the look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expenses_summary_recycler_view_row, parent, false);
        return new ExpensesSummaryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesSummaryRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assign values to each row as they return to the screen (during scrolling)
        holder.tvName.setText(categories.get(position).getName());
//        holder.tvAmount.setText(categories.get(position).getAmount());
        int total = 0;
        for (int i = 0; i < expenses.size(); i++){
            if (expenses.get(i).getCategoryName().equals(categories.get(position).getName())){
                total += Integer.parseInt(expenses.get(i).getAmount());
            }
        }
        String description = null;
        String amount = categories.get(position).getAmount();
        if (amount.equals("0"))
            description = "No expenses in this category yet.";
        else if (amount.equals("1"))
            description = "1 expense for a total of " + total + ".";
        else
            description = categories.get(position).getAmount() + " expenses for a total of " + total + ".";
        holder.tvAmount.setText(description);
//        holder.tvTotal.setText("Total: " + total);
    }

    @Override
    public int getItemCount() {
        // the number of items displayed
        return categories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // similar to onCreate method - assign values to each row in the recyclerview
        TextView tvName;
        TextView tvAmount;
//        TextView tvTotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.category_recycler_view_name);
            tvAmount = itemView.findViewById(R.id.category_recycler_view_amount);
//            tvTotal = itemView.findViewById(R.id.category_recycler_view_total);
        }
    }

}

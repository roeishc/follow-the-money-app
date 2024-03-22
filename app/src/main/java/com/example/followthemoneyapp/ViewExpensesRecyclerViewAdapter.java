package com.example.followthemoneyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewExpensesRecyclerViewAdapter extends RecyclerView.Adapter<ViewExpensesRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Expense> expenses;
    private final ViewExpensesRecyclerViewInterface recyclerViewInterface;

    public ViewExpensesRecyclerViewAdapter(Context context, ArrayList<Expense> expenses,
                                           ViewExpensesRecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.expenses = expenses;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewExpensesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout (giving a look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_expenses_recycler_view_row, parent, false);
        return new ViewExpensesRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
//        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewExpensesRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assign values to the views created in the view_expenses_recycler_view_row layout file
        // based on the position of the recycler view
        Expense expense = expenses.get(position);
        holder.tvDate.setText(expense.getDate());
        holder.tvAmount.setText(expense.getAmount());
        holder.tvCategory.setText(expense.getCategoryName());
        holder.tvNote.setText(expense.getNote());
    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items went want displayed
        return expenses.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from view_expenses_recycler_view_row layout file
        // similar to onCreate method

        TextView tvDate;
        TextView tvAmount;
        TextView tvCategory;
        TextView tvNote;

        public MyViewHolder(@NonNull View itemView, ViewExpensesRecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.expense_recycler_view_date);
            tvAmount = itemView.findViewById(R.id.expense_recycler_view_amount);
            tvCategory = itemView.findViewById(R.id.expense_recycler_view_category);
            tvNote = itemView.findViewById(R.id.expense_recycler_view_note);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (verifyRecyclerViewPosition(recyclerViewInterface)){
                        recyclerViewInterface.onItemClicked(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    if (verifyRecyclerViewPosition(recyclerViewInterface)){
                        recyclerViewInterface.onItemLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }

        private boolean verifyRecyclerViewPosition(ViewExpensesRecyclerViewInterface recyclerViewInterface){
            return recyclerViewInterface != null && getAdapterPosition() != RecyclerView.NO_POSITION;
        }
    }
}

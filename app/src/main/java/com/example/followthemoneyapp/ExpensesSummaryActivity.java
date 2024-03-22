package com.example.followthemoneyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class ExpensesSummaryActivity extends AppCompatActivity {

    public static final String CATEGORY = "categories";

    private boolean reloadNeeded = false;

    RecyclerView recyclerView;
    ExpensesSummaryRecyclerViewAdapter adapter;

    private ArrayList<Category> categories;
    private ArrayList<Expense> expenses;

    Pie pie;
    private List<DataEntry> pieChartData = new ArrayList<>();
    AnyChartView anyChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_summary);

        categories = CategoriesSingleton.getInstance().getCategories();
        expenses = ExpensesSingleton.getInstance().getExpenses();

        // create recycler view
        recyclerView = findViewById(R.id.EditCategoriesRecyclerView);
        adapter = new ExpensesSummaryRecyclerViewAdapter(this, categories, expenses);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create pie chart
        pie = AnyChart.pie();
        initPieChartData();
        pie.data(pieChartData);
        anyChartView = (AnyChartView) findViewById(R.id.expenses_summary_chart);
        anyChartView.setChart(pie);

//        pie.legend().itemsLayout("vertical");
        pie.background().enabled(true);
        pie.background().fill("#D7FBFF");
    }

    private void initPieChartData(){
        int cost = 0;
        for(int i = 0; i < categories.size(); i++){
            for (int j = 0; j < expenses.size(); j++){ // find the total cost of the category
                if (expenses.get(j).getCategoryName().equals(categories.get(i).getName()))
                    cost += Integer.parseInt(expenses.get(j).getAmount());
            }
            pieChartData.add(new ValueDataEntry(categories.get(i).getName(), cost));
            cost = 0;
        }
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
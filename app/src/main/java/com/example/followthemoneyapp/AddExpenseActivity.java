package com.example.followthemoneyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    public static final String EXPENSE = "expenses";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String U_ID = "u_id";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String NOTE = "note";

    private String oldCategory;

    private final Calendar calendar = Calendar.getInstance();

    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initUI();
    }

    private void initUI(){

        // category
        ArrayList<String> categoriesNames = new ArrayList<>();
        ArrayList<Category> categories = CategoriesSingleton.getInstance().getCategories();
        for (int i = 0; i < categories.size(); i++)
            categoriesNames.add(categories.get(i).getName());
        spinner = (Spinner)findViewById(R.id.add_expense_category);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        for (String s : categoriesNames)
            spinnerAdapter.add(s);
        spinner.setAdapter(spinnerAdapter);

        // date
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateDate();
            }
        };

        findViewById(R.id.add_expense_date_wrapper).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddExpenseActivity.this,
                        date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // populate views if editing of existing expense from intent
        Expense expense;
        if (getIntent().hasExtra(U_ID)){
//            displayToast("Came to edit expense", Toast.LENGTH_SHORT);
            expense = getExpenseFromIntent();
            setExpenseDetails(expense);
        }
        else{
//            displayToast("Came from main page", Toast.LENGTH_SHORT);
        }

        // save button
        oldCategory = spinner.getSelectedItem().toString();
        findViewById(R.id.add_expense_save_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean toasted = false; // for showing only 1 toast message at a time
                String categoryName = spinner.getSelectedItem().toString();
                String amount = null;
                String date = null;
                if (isAmountEmpty()){
                    displayToast("Enter amount", Toast.LENGTH_SHORT);
                    toasted = true;
                }
                else{
                    amount = getAmount();
                }
                if (!toasted && isDateEmpty()){
                    displayToast("Enter date", Toast.LENGTH_SHORT);
                }
                else{
                    date = getDate();
                }
                String note = getNote(); // allow empty note
                if (getIntent().hasExtra(U_ID)){ // if editing existing expense
                    // update db according to expense.u_id
                    updateExpenseInDb(getIntent().getStringExtra(U_ID),categoryName,amount,date,note);
                    updateExpenseInMemory(getIntent().getStringExtra(U_ID),categoryName,amount,date,note);
                    if (!oldCategory.equals(categoryName)){
                        updateCategoriesInDb(oldCategory, categoryName);
                        updateCategoriesInMemory(oldCategory, categoryName);
                    }
                    displayToast("Updated", Toast.LENGTH_SHORT);
                }
                else{ // creating a new expense
                    if (amount != null && date != null){
                        // unique id - from db
                        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference(EXPENSE);
                        DatabaseReference newExpenseRef = expenseRef.push();
                        String u_id = newExpenseRef.getKey();

                        saveExpenseToDb(new Expense(u_id, categoryName, amount, date, note));
                        addAmountToCategory(categoryName);
                        displayToast("Saved", Toast.LENGTH_SHORT);

                        // clean UI elements from input
                        cleanUI();
                    }
                }

            }
        });
    }

    private void updateDate(){
        String myFormat = DATE_FORMAT;
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        TextView amount = (TextView)findViewById(R.id.add_expense_date);
        amount.setText(dateFormat.format((calendar.getTime())));
        amount.setTextColor(Color.parseColor("black"));
    }

    private void saveExpenseToDb(Expense expense){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference expenseToAddRef = database.getReference(EXPENSE).child(expense.getU_id());
        expenseToAddRef.child(EXPENSE).child(expense.getU_id());
        expenseToAddRef.setValue(expense);
    }

    private boolean isAmountEmpty(){
        EditText editTextAmount = (EditText)findViewById(R.id.add_expense_amount);
        String amount = editTextAmount.getText().toString();
        if (amount.matches(""))
            return true;
        return false;
    }

    private String getAmount(){
        EditText editTextAmount = (EditText)findViewById(R.id.add_expense_amount);
//        return Integer.parseInt(editTextAmount.getText().toString());
        return editTextAmount.getText().toString();
    }

    private boolean isDateEmpty(){
        String date = getDate();
        if (date.matches(""))
            return true;
        return false;
    }

    private String getDate(){
        TextView textViewDate = (TextView)findViewById(R.id.add_expense_date);
        return textViewDate.getText().toString();
    }

    private String getNote(){
        EditText editTextNote = (EditText)findViewById(R.id.add_expense_note);
        return editTextNote.getText().toString();
    }

    private void displayToast(String str, int length){
        Toast.makeText(getApplicationContext(),str, length).show();
    }

    private void setCategory(String category){
        spinner.setSelection(spinnerAdapter.getPosition(category));
    }

    private void setAmount(String amount){
        EditText editTextAmount = (EditText)findViewById(R.id.add_expense_amount);
        editTextAmount.setText(amount);
    }

    private void setDate(String date){
        TextView textViewDate = (TextView)findViewById(R.id.add_expense_date);
        textViewDate.setText(date);
    }

    private void setNote(String note){
        EditText editTextNote = (EditText)findViewById(R.id.add_expense_note);
        editTextNote.setText(note);
    }

    private void setExpenseDetails(Expense expense){
        setCategory(expense.getCategoryName());
        setAmount(expense.getAmount());
        setDate(expense.getDate());
        setNote(expense.getNote());
    }

    private Expense getExpenseFromIntent(){
        return new Expense(getIntent().getStringExtra(U_ID),
                getIntent().getStringExtra(CATEGORY_NAME),
                getIntent().getStringExtra(AMOUNT),
                getIntent().getStringExtra(DATE),
                getIntent().getStringExtra(NOTE));
    }

    private void cleanUI(){
        ((TextView)findViewById(R.id.add_expense_amount)).setText(""); // amount
        ((TextView)findViewById(R.id.add_expense_date)).setText(""); // date
        ((EditText)findViewById(R.id.add_expense_note)).setText(""); // note
    }

    private void updateExpenseInDb(String u_id, String category, String amount, String date, String note){
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference(EXPENSE);
        expenseRef.child(u_id).child(CATEGORY_NAME).setValue(category);
        expenseRef.child(u_id).child(AMOUNT).setValue(amount);
        expenseRef.child(u_id).child(DATE).setValue(date);
        expenseRef.child(u_id).child(NOTE).setValue(note);
    }

    private void updateCategoriesInDb(String oldCategory, String newCategory){
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference(ExpensesSummaryActivity.CATEGORY);
        categoryRef.child(oldCategory).child(AMOUNT).setValue(ServerValue.increment(-1));
        categoryRef.child(newCategory).child(AMOUNT).setValue(ServerValue.increment(1));
    }

    private void updateCategoriesInMemory(String oldCategory, String newCategory){
        ArrayList<Category> categories = CategoriesSingleton.getInstance().getCategories();
        for (int i = 0; i < categories.size(); i++){
            if (categories.get(i).getName().equals(oldCategory))
                categories.get(i).incrementAmount(-1);
            else if (categories.get(i).getName().equals(newCategory))
                categories.get(i).incrementAmount(1);
        }
    }

    private void updateExpenseInMemory(String u_id, String category, String amount, String date, String note){
        ArrayList<Expense> expenses = ExpensesSingleton.getInstance().getExpenses();
        for (int i = 0; i < expenses.size(); i++){
            if (expenses.get(i).getU_id().equals(u_id)){
                expenses.get(i).setCategoryName(category);
                expenses.get(i).setAmount(amount);
                expenses.get(i).setDate(date);
                expenses.get(i).setNote(note);
                return;
            }
        }
    }

    private void addAmountToCategory(String categoryName){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(com.example.followthemoneyapp.ExpensesSummaryActivity.CATEGORY);
        myRef.child(categoryName).child(AddExpenseActivity.AMOUNT).setValue(ServerValue.increment(1));
    }

}
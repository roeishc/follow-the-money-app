package com.example.followthemoneyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SendSmsDialog.SendSmsDialogListener {

    private ImageView infoButton;
    private ImageView smsButton;
    private boolean smsSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoButton = findViewById(R.id.main_activity_info_button);
        smsButton = findViewById(R.id.main_activity_sms_image);
        smsSuccess = false;



        initUI();

    }

    private void initUI(){
        // add expense
        findViewById(R.id.button_add_expense).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(i);
            }
        });

        // view expenses
        findViewById(R.id.button_view_expense).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewExpensesActivity.class);
                startActivity(i);
            }
        });

        // edit categories
        findViewById(R.id.button_expenses_summary).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ExpensesSummaryActivity.class);
                startActivity(i);
            }
        });

        // info button
        infoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainInfoActivity.class);
                startActivity(i);
            }
        });
        animateImageButton(infoButton);

        // sms button
        smsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openDialog();
            }
        });
        animateImageButton(smsButton);
    }

    private void openDialog(){
        SendSmsDialog sendSmsDialog = new SendSmsDialog();
        sendSmsDialog.show(getSupportFragmentManager(), getString(R.string.send_sms_title));
//        Toast.makeText(getApplicationContext(), "Test 1 2 3", Toast.LENGTH_SHORT);

    }

    private void animateImageButton(ImageView imageView){
        ObjectAnimator mover = ObjectAnimator.ofFloat(imageView, "translationY", 400f, 0f);
        mover.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        fadeIn.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeIn).with(mover);
        animatorSet.start();
    }

    @Override
    public boolean applyText(boolean success) {
        // implemented this interface to know in the main activity if the SMS was successfully sent or not.
        // it was not used in the main activity yet, implemented for future use
        return smsSuccess = success;
    }
}
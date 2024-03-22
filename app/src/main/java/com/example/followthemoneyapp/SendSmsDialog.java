package com.example.followthemoneyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendSmsDialog extends AppCompatDialogFragment {

    private TextView sendSmsDescription;
    private String description;
    private EditText editTextPhoneNumber;

    private SendSmsDialogListener listener;
    private boolean success;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_send_sms_dialog, null);

        sendSmsDescription = view.findViewById(R.id.send_sms_dialog_description);
        editTextPhoneNumber = view.findViewById(R.id.send_sms_dialog_phone);

        description = ExpensesSingleton.getInstance().getSmsDescription();
        success = false;
        sendSmsDescription.setText(description);

        builder.setView(view)
                .setTitle(R.string.send_sms_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(requireContext().getApplicationContext(), "Canceled", Toast.LENGTH_SHORT);
                        Log.d("SendSmsDialog", "Cancel button clicked");
                    }
                })
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phoneNumber = editTextPhoneNumber.getText().toString();
                        sendSMS(phoneNumber, description);
                        listener.applyText(success);
                        boolean validPhoneNumber = isPhoneNumberValid(phoneNumber);
                        if (success && validPhoneNumber)
                            Toast.makeText(requireContext(), R.string.sent, Toast.LENGTH_SHORT).show();
                        else if (!validPhoneNumber){
                            Toast.makeText(requireContext(), R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(requireContext(), R.string.failed_sms, Toast.LENGTH_SHORT).show();
                        Log.d("SendSmsDialog", "Send button clicked with phone number " + phoneNumber);
                    }
                });

        return builder.create();
//        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SendSmsDialogListener) context;
        } catch (ClassCastException e) {
//            e.printStackTrace();
            throw new ClassCastException(context + "must implement SendSmsDialogListener");
        }
    }

    public interface SendSmsDialogListener{
        boolean applyText(boolean success);
    }

    public void sendSMS(String phoneNumber, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
            success = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber){
        String regex = "^05\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}

package com.example.project_app;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class Account_Fragment extends Fragment {
    EditText editTextUsr, editTextMail, editTextPass, editTextBirthday;
    Spinner spinnerGender;
    Button registerButton, buttonSelectBirthdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account_, container, false);

        // Initialize EditText fields and Register button
        editTextUsr = rootView.findViewById(R.id.editTextText);
        editTextMail = rootView.findViewById(R.id.editText2);
        editTextPass = rootView.findViewById(R.id.editText3);
        editTextBirthday = rootView.findViewById(R.id.editTextBirthday);
        spinnerGender = rootView.findViewById(R.id.spinnerGender);
        registerButton = rootView.findViewById(R.id.button2);
        buttonSelectBirthdate = rootView.findViewById(R.id.buttonSelectBirthdate);

        // Set onClickListener for date picker button
        buttonSelectBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Set onItemSelectedListener for the gender spinner
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Selected gender: " + selectedGender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set onClickListener for the registerButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the username from the EditText field
                String username = editTextUsr.getText().toString().trim();

                // Clear the EditText fields
                editTextUsr.setText("");
                editTextMail.setText("");
                editTextPass.setText("");
                editTextBirthday.setText("");
                spinnerGender.setSelection(0);

                // Send a notification
                sendNotification(username);
            }
        });

        return rootView;
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextBirthday.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void sendNotification(String username) {
        String welcomeMessage = "Welcome, " + username + "! Check out our large brands of products";
        String emailConfirmationMessage = "Check your e-mail to confirm your account";

        // Create an intent to open your MainActivity when the notification is clicked
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "Account notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Apply BigTextStyle to display longer text in the expanded notification
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(welcomeMessage + "\n" + emailConfirmationMessage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "channel_id")
                .setSmallIcon(R.drawable.ic_account)
                .setContentTitle("Welcome!")
                .setContentText(welcomeMessage)
                .setStyle(bigTextStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Get the NotificationManagerCompat
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

        // Send the notification
        notificationManager.notify(1, builder.build());
    }
}

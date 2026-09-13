package com.iyaadam.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText phoneInput;
    private EditText addressInput;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        preferences = getSharedPreferences(
                "iyaadam_profile",
                Context.MODE_PRIVATE
        );

        ImageView backBtn =
                findViewById(R.id.back_btn);

        nameInput =
                findViewById(R.id.name_input);

        phoneInput =
                findViewById(R.id.phone_input);

        addressInput =
                findViewById(R.id.address_input);

        Button saveBtn =
                findViewById(R.id.save_profile_btn);

        Button ordersBtn =
                findViewById(R.id.my_orders_btn);

        backBtn.setOnClickListener(v -> finish());

        loadProfile();

        saveBtn.setOnClickListener(v -> saveProfile());

        ordersBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ProfileActivity.this,
                            OrderHistoryActivity.class
                    );

            startActivity(intent);
        });
    }

    private void loadProfile() {

        nameInput.setText(
                preferences.getString(
                        "name",
                        ""
                )
        );

        phoneInput.setText(
                preferences.getString(
                        "phone",
                        ""
                )
        );

        addressInput.setText(
                preferences.getString(
                        "address",
                        ""
                )
        );
    }

    private void saveProfile() {

        String name =
                nameInput.getText()
                        .toString()
                        .trim();

        String phone =
                phoneInput.getText()
                        .toString()
                        .trim();

        String address =
                addressInput.getText()
                        .toString()
                        .trim();

        if (name.isEmpty()) {

            nameInput.setError(
                    "Enter your name"
            );

            return;
        }

        if (phone.isEmpty()) {

            phoneInput.setError(
                    "Enter your phone number"
            );

            return;
        }

        preferences.edit()
                .putString("name", name)
                .putString("phone", phone)
                .putString("address", address)
                .apply();

        Toast.makeText(
                this,
                "Profile saved successfully.",
                Toast.LENGTH_SHORT
        ).show();
    }
}

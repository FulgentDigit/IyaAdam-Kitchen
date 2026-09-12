package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView menuRecycler;
    private DishAdapter adapter;
    private ImageView backBtn;
    private TextView customOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        backBtn = findViewById(R.id.back_btn);
        customOrderBtn = findViewById(R.id.custom_order_btn);
        menuRecycler = findViewById(R.id.menu_recycler);

        backBtn.setOnClickListener(v -> finish());

        customOrderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CustomOrderActivity.class);
            startActivity(intent);
        });

        menuRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<Dish> allDishes = createAllDishes();

        adapter = new DishAdapter(
                this,
                allDishes,
                new DishAdapter.OnDishClickListener() {

                    @Override
                    public void onDishClick(Dish dish) {
                        CartManager.getInstance().addToCart(dish);
                    }

                    @Override
                    public void onAddToCart(Dish dish) {
                        CartManager.getInstance().addToCart(dish);
                    }
                }
        );

        menuRecycler.setAdapter(adapter);
    }

    private List<Dish> createAllDishes() {

        List<Dish> dishes = new ArrayList<>();

        dishes.add(new Dish(
                "1",
                "Fufu & Ewedu",
                2500,
                "Traditional leafy soup",
                "images",
                4.7,
                152,
                20,
                true
        ));

        dishes.add(new Dish(
                "2",
                "Jollof Rice & Chicken",
                3000,
                "Spiced rice perfection",
                "images8",
                4.8,
                198,
                25,
                true
        ));

        dishes.add(new Dish(
                "3",
                "Goat Meat Pepper Sauce",
                3500,
                "Tender meat in spicy broth",
                "images13",
                4.6,
                87,
                30,
                true
        ));

        dishes.add(new Dish(
                "4",
                "Creamy Pasta & Chicken",
                3500,
                "Italian meets African",
                "images234",
                4.5,
                124,
                15,
                true
        ));

        dishes.add(new Dish(
                "5",
                "Moi Moi",
                1500,
                "Steamed bean pudding",
                "images10",
                4.4,
                76,
                20,
                true
        ));

        dishes.add(new Dish(
                "6",
                "Pepper Soup",
                2000,
                "Hot traditional broth",
                "images11",
                4.3,
                95,
                15,
                true
        ));

        dishes.add(new Dish(
                "7",
                "Pounded Yam & Egusi",
                4000,
                "Smooth yam with soup",
                "images13",
                4.9,
                213,
                25,
                true
        ));

        dishes.add(new Dish(
                "8",
                "Rice & Vegetable",
                2200,
                "Creamy mixed vegetables",
                "images15",
                4.2,
                64,
                18,
                true
        ));

        dishes.add(new Dish(
                "9",
                "Amala & Ewedu",
                2800,
                "Cornmeal with leafy soup",
                "amala_with_ewedu_and_goat_meat",
                4.6,
                141,
                22,
                true
        ));

        dishes.add(new Dish(
                "10",
                "Egusi Soup",
                2200,
                "Melon seed delicacy",
                "egusi_soup",
                4.5,
                108,
                18,
                true
        ));

        return dishes;
    }
}

I also corrected the "goat_meat_pepper_soup" image reference to "images13", since we already know "images13" exists in your project.

---

2. Replace "activity_menu.xml"

:::writing{variant="standard" id="31854" title="activity_menu.xml"}

<?xml version="1.0" encoding="utf-8"?>

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="#002D21">

    <!-- PREMIUM HEADER -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="64dp"
        android:background="@color/header_green"
        android:gravity="center_vertical"
        android:paddingStart="14dp"
        android:paddingEnd="14dp"
        android:orientation="horizontal">

        <ImageView
            android:id="@+id/back_btn"
            android:layout_width="38dp"
            android:layout_height="38dp"
            android:padding="7dp"
            android:src="@drawable/ic_back"
            android:contentDescription="Back"
            android:background="?android:attr/selectableItemBackgroundBorderless" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="10dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="🍴  IyaAdam"
                android:textColor="#FFFFFF"
                android:textSize="21sp"
                android:textStyle="bold" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="FULL MENU"
                android:textColor="@color/gold"
                android:textSize="10sp"
                android:textStyle="bold"
                android:letterSpacing="0.18" />

        </LinearLayout>

    </LinearLayout>

    <!-- RED ACCENT -->
    <View
        android:layout_width="match_parent"
        android:layout_height="2dp"
        android:background="@color/alert_red" />

    <!-- CUSTOM ORDER CARD -->
    <LinearLayout
        android:id="@+id/custom_order_btn"
        android:layout_width="match_parent"
        android:layout_height="92dp"
        android:layout_marginStart="12dp"
        android:layout_marginTop="12dp"
        android:layout_marginEnd="12dp"
        android:layout_marginBottom="8dp"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:padding="15dp"
        android:background="#FFFFFF"
        android:clickable="true"
        android:focusable="true">

        <TextView
            android:layout_width="52dp"
            android:layout_height="52dp"
            android:gravity="center"
            android:text="🎤"
            android:textSize="28sp"
            android:background="#FFF3D6" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="13dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Can't find what you want?"
                android:textColor="#002D21"
                android:textSize="16sp"
                android:textStyle="bold" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Make a custom request • Type or send a voice note"
                android:textColor="#666666"
                android:textSize="12sp"
                android:layout_marginTop="3dp" />

        </LinearLayout>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="›"
            android:textColor="#C62828"
            android:textSize="30sp"
            android:textStyle="bold" />

    </LinearLayout>

    <!-- SECTION TITLE -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:gravity="center_vertical"
        android:paddingStart="16dp"
        android:paddingEnd="16dp">

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="OUR MENU"
            android:textColor="#FFFFFF"
            android:textSize="17sp"
            android:textStyle="bold"
            android:letterSpacing="0.08" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Fresh • Tasty • Local"
            android:textColor="@color/gold"
            android:textSize="11sp" />

    </LinearLayout>

    <!-- MENU LIST -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/menu_recycler"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:clipToPadding="false"
        android:paddingStart="8dp"
        android:paddingEnd="8dp"
        android:paddingBottom="12dp"
        android:background="#002D21" />

</LinearLayout>

---

3. Add "CustomOrderActivity.java"

Create:

"app/src/main/java/com/iyaadam/app/CustomOrderActivity.java"

:::writing{variant="standard" id="56291" title="CustomOrderActivity.java"}

package com.iyaadam.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.media.MediaRecorder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class CustomOrderActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_REQUEST = 1001;

    private EditText requestText;
    private TextView recordingStatus;
    private Button recordButton;
    private Button submitButton;
    private ImageView backButton;

    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order);

        backButton = findViewById(R.id.custom_back_btn);
        requestText = findViewById(R.id.custom_request_text);
        recordingStatus = findViewById(R.id.recording_status);
        recordButton = findViewById(R.id.record_button);
        submitButton = findViewById(R.id.submit_custom_order);

        backButton.setOnClickListener(v -> finish());

        recordButton.setOnClickListener(v -> {

            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }

        });

        submitButton.setOnClickListener(v -> submitRequest());
    }

    private void startRecording() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_REQUEST
            );

            return;
        }

        File audioFile = new File(
                getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "custom_order_" + System.currentTimeMillis() + ".m4a"
        );

        audioFilePath = audioFile.getAbsolutePath();

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(audioFilePath);

        try {

            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;

            recordButton.setText("⏹ Stop Recording");
            recordingStatus.setText("🔴 Recording your voice request...");
            recordingStatus.setTextColor(0xFFC62828);

        } catch (IOException e) {

            Toast.makeText(
                    this,
                    "Unable to start recording",
                    Toast.LENGTH_SHORT
            ).show();

            releaseRecorder();
        }
    }

    private void stopRecording() {

        if (mediaRecorder != null) {

            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                // Ignore invalid short recordings
            }

            releaseRecorder();
        }

        isRecording = false;

        recordButton.setText("🎤 Record Voice Request");
        recordingStatus.setText("✓ Voice request recorded");
        recordingStatus.setTextColor(0xFF2E7D32);

        Toast.makeText(
                this,
                "Voice request recorded",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void releaseRecorder() {

        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void submitRequest() {

        String request = requestText.getText().toString().trim();

        if (request.isEmpty() && audioFilePath == null) {

            Toast.makeText(
                    this,
                    "Please type your request or record a voice note",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        /*
         * For now we confirm the custom request locally.
         *
         * The next stage will connect this request to the Cart
         * and Checkout system so the restaurant receives:
         *
         * - Customer request
         * - Voice recording
         * - Customer details
         * - Order/payment information
         */

        Toast.makeText(
                this,
                "Custom request received! We will prepare it for checkout.",
                Toast.LENGTH_LONG
        ).show();

        finish();
    }

    @Override
    protected void onDestroy() {

        if (isRecording) {
            stopRecording();
        }

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == RECORD_AUDIO_REQUEST) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startRecording();

            } else {

                Toast.makeText(
                        this,
                        "Microphone permission is required for voice requests",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}

---

4. Add "activity_custom_order.xml"

Create:

"app/src/main/res/layout/activity_custom_order.xml"

:::writing{variant="standard" id="90417" title="activity_custom_order.xml"}

<?xml version="1.0" encoding="utf-8"?>

<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#002D21"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="18dp">

        <!-- HEADER -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="58dp"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <ImageView
                android:id="@+id/custom_back_btn"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:padding="7dp"
                android:src="@drawable/ic_back"
                android:contentDescription="Back" />

            <TextView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:layout_marginStart="10dp"
                android:text="Custom Order"
                android:textColor="#FFFFFF"
                android:textSize="23sp"
                android:textStyle="bold" />

        </LinearLayout>

        <View
            android:layout_width="60dp"
            android:layout_height="4dp"
            android:background="@color/alert_red"
            android:layout_marginTop="4dp" />

        <!-- INTRO -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:text="Tell us exactly what you want 👨🏾‍🍳"
            android:textColor="@color/gold"
            android:textSize="21sp"
            android:textStyle="bold" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Can't find your preferred meal on our menu? No problem. Describe your special request below or simply record a voice note."
            android:textColor="#D8E8E2"
            android:textSize="14sp"
            android:lineSpacingExtra="3dp" />

        <!-- TEXT REQUEST -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="25dp"
            android:text="YOUR REQUEST"
            android:textColor="#FFFFFF"
            android:textSize="13sp"
            android:textStyle="bold"
            android:letterSpacing="0.1" />

        <EditText
            android:id="@+id/custom_request_text"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            android:layout_marginTop="8dp"
            android:background="#FFFFFF"
            android:gravity="top|start"
            android:hint="Example: I want pounded yam with extra egusi soup, 3 pieces of meat and little pepper..."
            android:padding="15dp"
            android:textColor="#222222"
            android:textColorHint="#888888"
            android:textSize="14sp"
            android:inputType="textMultiLine"
            android:maxLines="7" />

        <!-- VOICE SECTION -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:text="VOICE REQUEST"
            android:textColor="#FFFFFF"
            android:textSize="13sp"
            android:textStyle="bold"
            android:letterSpacing="0.1" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="125dp"
            android:layout_marginTop="8dp"
            android:gravity="center"
            android:orientation="vertical"
            android:background="#FFFFFF"
            android:padding="12dp">

            <TextView
                android:id="@+id/recording_status"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Tap below to record your request"
                android:textColor="#555555"
                android:textSize="13sp" />

            <Button
                android:id="@+id/record_button"
                android:layout_width="wrap_content"
                android:layout_height="48dp"
                android:layout_marginTop="10dp"
                android:text="🎤 Record Voice Request"
                android:textAllCaps="false"
                android:textColor="#FFFFFF"
                android:backgroundTint="@color/alert_red" />

        </LinearLayout>

        <!-- SUBMIT -->
        <Button
            android:id="@+id/submit_custom_order"
            android:layout_width="match_parent"
            android:layout_height="55dp"
            android:layout_marginTop="28dp"
            android:text="✓  Submit Custom Request"
            android:textAllCaps="false"
            android:textColor="#FFFFFF"
            android:textSize="16sp"
            android:textStyle="bold"
            android:backgroundTint="@color/gold" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="18dp"
            android:gravity="center"
            android:text="IyaAdam Kitchen • Home of African & Continental Dishes"
            android:textColor="#AFC6BC"
            android:textSize="11sp" />

    </LinearLayout>

</ScrollView>

5. Add microphone permission

In "AndroidManifest.xml", before the "<application>" tag, add:

<uses-permission android:name="android.permission.RECORD_AUDIO" />

And inside "<application>" add:

<activity
    android:name=".CustomOrderActivity"
    android:exported="false" />

Important

This gives us the front end and actual voice recording now.

The next step should be to connect the custom request to the Cart → Checkout → Order system, so a customer can submit:

Food items + custom instructions + voice note → one order.

That is the part that will make this feel like a real restaurant ordering app rather than just a menu.

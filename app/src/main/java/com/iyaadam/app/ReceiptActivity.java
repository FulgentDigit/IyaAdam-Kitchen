package com.iyaadam.app;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    private JSONObject order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        findViewById(R.id.back_btn)
                .setOnClickListener(v -> finish());

        String json =
                getIntent().getStringExtra("order_json");

        try {

            order = new JSONObject(json);

            showReceipt();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to open receipt.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        }

        findViewById(R.id.save_receipt_btn)
                .setOnClickListener(v -> saveReceipt());

        findViewById(R.id.share_receipt_btn)
                .setOnClickListener(v -> saveAndShareReceipt());
    }

    private void showReceipt() throws Exception {

        TextView orderNumber =
                findViewById(R.id.receipt_order_number);

        TextView customer =
                findViewById(R.id.receipt_customer);

        TextView phone =
                findViewById(R.id.receipt_phone);

        TextView address =
                findViewById(R.id.receipt_address);

        TextView date =
                findViewById(R.id.receipt_date);

        TextView status =
                findViewById(R.id.receipt_status);

        TextView items =
                findViewById(R.id.receipt_items);

        TextView total =
                findViewById(R.id.receipt_total);

        orderNumber.setText(
                "Order #" +
                        order.optString("order_number")
        );

        customer.setText(
                "Customer: " +
                        order.optString("customer_name")
        );

        phone.setText(
                "Phone: " +
                        order.optString("customer_phone")
        );

        address.setText(
                "Delivery: " +
                        order.optString("delivery_address")
        );

        date.setText(
                "Date: " +
                        order.optString("created_at")
        );

        status.setText(
                "Status: " +
                        capitalize(
                                order.optString("status")
                        )
        );

        JSONArray array =
                order.optJSONArray("items");

        StringBuilder itemText =
                new StringBuilder();

        if (array != null) {

            for (int i = 0; i < array.length(); i++) {

                JSONObject item =
                        array.getJSONObject(i);

                String name =
                        item.optString("dish_name");

                int quantity =
                        item.optInt("quantity");

                double subtotal =
                        item.optDouble("subtotal");

                itemText
                        .append(quantity)
                        .append(" × ")
                        .append(name)
                        .append("    ₦")
                        .append(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        subtotal
                                )
                        )
                        .append("\n");
            }
        }

        items.setText(itemText.toString());

        total.setText(
                String.format(
                        Locale.getDefault(),
                        "₦%.2f",
                        order.optDouble("total_amount")
                )
        );
    }

    private String capitalize(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        return value.substring(0, 1).toUpperCase()
                + value.substring(1);
    }

    private File createReceiptPdf() throws Exception {

        PdfDocument document =
                new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(
                        595,
                        842,
                        1
                ).create();

        PdfDocument.Page page =
                document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();

        paint.setTextSize(22);
        paint.setFakeBoldText(true);

        canvas.drawText(
                "IyaAdam Kitchen",
                40,
                50,
                paint
        );

        paint.setTextSize(12);
        paint.setFakeBoldText(false);

        canvas.drawText(
                "Home of African and Continental Dishes",
                40,
                72,
                paint
        );

        int y = 110;

        paint.setTextSize(15);
        paint.setFakeBoldText(true);

        canvas.drawText(
                "ORDER RECEIPT",
                40,
                y,
                paint
        );

        y += 35;

        paint.setTextSize(12);
        paint.setFakeBoldText(false);

        canvas.drawText(
                "Order: " +
                        order.optString("order_number"),
                40,
                y,
                paint
        );

        y += 22;

        canvas.drawText(
                "Customer: " +
                        order.optString("customer_name"),
                40,
                y,
                paint
        );

        y += 22;

        canvas.drawText(
                "Phone: " +
                        order.optString("customer_phone"),
                40,
                y,
                paint
        );

        y += 22;

        canvas.drawText(
                "Date: " +
                        order.optString("created_at"),
                40,
                y,
                paint
        );

        y += 35;

        paint.setFakeBoldText(true);

        canvas.drawText(
                "ITEMS",
                40,
                y,
                paint
        );

        y += 25;

        paint.setFakeBoldText(false);

        JSONArray array =
                order.optJSONArray("items");

        if (array != null) {

            for (int i = 0;
                 i < array.length();
                 i++) {

                JSONObject item =
                        array.getJSONObject(i);

                String line =
                        item.optInt("quantity")
                                + " x "
                                + item.optString("dish_name")
                                + "   ₦"
                                + String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        item.optDouble("subtotal")
                                );

                canvas.drawText(
                        line,
                        40,
                        y,
                        paint
                );

                y += 23;
            }
        }

        y += 20;

        paint.setFakeBoldText(true);
        paint.setTextSize(17);

        canvas.drawText(
                "TOTAL: ₦" +
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                order.optDouble("total_amount")
                        ),
                40,
                y,
                paint
        );

        y += 35;

        paint.setTextSize(12);
        paint.setFakeBoldText(false);

        canvas.drawText(
                "Delivery: " +
                        order.optString("delivery_address"),
                40,
                y,
                paint
        );

        y += 35;

        canvas.drawText(
                "Thank you for ordering from IyaAdam Kitchen.",
                40,
                y,
                paint
        );

        y += 20;

        canvas.drawText(
                "Developed by fdc: +2347089364492",
                40,
                y,
                paint
        );

        document.finishPage(page);

        File directory =
                new File(
                        getExternalFilesDir(
                                Environment.DIRECTORY_DOCUMENTS
                        ),
                        "IyaAdam Receipts"
                );

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename =
                "IyaAdam_Receipt_"
                        + order.optString("order_number")
                        + ".pdf";

        File file =
                new File(directory, filename);

        FileOutputStream output =
                new FileOutputStream(file);

        document.writeTo(output);

        output.close();

        document.close();

        return file;
    }

    private void saveReceipt() {

        try {

            File file =
                    createReceiptPdf();

            Toast.makeText(
                    this,
                    "Receipt saved successfully.",
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to save receipt.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void saveAndShareReceipt() {

        try {

            File file =
                    createReceiptPdf();

            Intent intent =
                    new Intent(Intent.ACTION_SEND);

            intent.setType("application/pdf");

            intent.putExtra(
                    Intent.EXTRA_STREAM,
                    androidx.core.content.FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".fileprovider",
                            file
                    )
            );

            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            startActivity(
                    Intent.createChooser(
                            intent,
                            "Share receipt"
                    )
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to share receipt.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

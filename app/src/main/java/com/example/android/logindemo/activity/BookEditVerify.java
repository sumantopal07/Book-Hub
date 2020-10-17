package com.example.android.logindemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.logindemo.R;

public class BookEditVerify extends AppCompatActivity {


    private EditText editText;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit_verify);

        Intent intent = getIntent();
        final String pin = intent.getExtras().getString("PIN_KEY");
        final String upi = intent.getExtras().getString("UPI_KEY");
        final String name = intent.getExtras().getString("NAME_KEY");
        final String desc = intent.getExtras().getString("DESC_KEY");
        final String price = intent.getExtras().getString("PRICE_KEY");
        final String image = intent.getExtras().getString("IMAGE_KEY");
        final String phone = intent.getExtras().getString("WHATSAPP_KEY");
        final String unique = intent.getExtras().getString("UNIQUE_KEY");


        editText = findViewById(R.id.pinEditText);
        enter = findViewById(R.id.enterButton);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pin != null && pin.equals(editText.getText().toString()) == true && editText.getText().toString() != null) {
                    Intent intent = new Intent(BookEditVerify.this, EditBookActivity.class);
                    intent.putExtra("UNIQUE_KEY", unique);
                    intent.putExtra("PIN_KEY", pin);
                    intent.putExtra("NAME_KEY", name);
                    intent.putExtra("DESC_KEY", desc);
                    intent.putExtra("PRICE_KEY", price);
                    intent.putExtra("UPI_KEY", upi);
                    intent.putExtra("WHATSAPP_KEY", phone);
                    intent.putExtra("IMAGE_KEY", image);
                    startActivity(intent);
                } else {
                    Toast.makeText(BookEditVerify.this, "WRONG PIN ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

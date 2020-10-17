package com.example.android.logindemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.logindemo.R;

public class PinVerification extends AppCompatActivity {

    private EditText editText;
    private Button enter;
    private String firstPin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verification);

        Intent intent = this.getIntent();
        firstPin = intent.getExtras().getString("PIN_KEY");
        editText = findViewById(R.id.pinEditText);
        enter = findViewById(R.id.enterButton);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstPin != null && firstPin.equals(editText.getText().toString()) == true && editText.getText().toString() != null) {
                    Intent intent = new Intent();
                    intent.putExtra("kyare", "ohyeah");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(PinVerification.this, "WROND PIN!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

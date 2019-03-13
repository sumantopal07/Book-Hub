package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class PinVerification extends AppCompatActivity {

    private EditText pinzza ;
    private TextView q;
    private Button enter;
    private String pin1,pin2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verification);

        Intent i=this.getIntent();
        pin1=i.getExtras().getString("PIN_KEY");
        pinzza=findViewById(R.id.pinEditText);
        //pinzza.setText("jfnwjnfjnf");
        //q=findViewById(R.id.test1);
        enter=findViewById(R.id.enterButton);



        //pin2 = pin.getText().toString();


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pin2 = ;
                //q.setText(pin1);
                if( pin1!=null && pin1.equals(pinzza.getText().toString())==true && pinzza.getText().toString()!=null)
                {
                    Intent intent = new Intent();
                    intent.putExtra("kyare","ohyeah");
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else {
                    Toast.makeText(PinVerification.this,"WROND PIN!!" , Toast.LENGTH_SHORT).show();
                }

            }
        });

        //pin.setText(pin1);


    }
}

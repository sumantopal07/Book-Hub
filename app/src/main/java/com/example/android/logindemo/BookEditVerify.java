package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookEditVerify extends AppCompatActivity {


    private EditText pinzza ;
    private TextView q;
    private Button enter;
    private String pin1,pin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit_verify);

        Intent intent=getIntent();
        final String pin=intent.getExtras().getString("PIN_KEY");
        final String upi=intent.getExtras().getString("UPI_KEY");
        final String name=intent.getExtras().getString("NAME_KEY");
        final String desc=intent.getExtras().getString("DESC_KEY");
        final String price=intent.getExtras().getString("PRICE_KEY");
        final String image=intent.getExtras().getString("IMAGE_KEY");
        final String phone=intent.getExtras().getString("WHATSAPP_KEY");
        final String unique=intent.getExtras().getString("UNIQUE_KEY");


////        final String[] uniqueId=intent.getExtras().getString("UNIQUE_KEY");
//        final String[] t=intent.getExtras().getStringArray("teacher");



        pinzza=findViewById(R.id.pinEditText);
        enter=findViewById(R.id.enterButton);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pin2 = ;
                //q.setText(pin1);
                if( pin!=null && pin.equals(pinzza.getText().toString())==true && pinzza.getText().toString()!=null)
                {
                    Intent inten = new Intent(BookEditVerify.this,EditBookActivity.class);
                    inten.putExtra("UNIQUE_KEY",unique);
                    inten.putExtra("PIN_KEY",pin);
                    inten.putExtra("NAME_KEY",name);
                    inten.putExtra("DESC_KEY",desc);
                    inten.putExtra("PRICE_KEY",price);
                    inten.putExtra("UPI_KEY",upi);
                    inten.putExtra("WHATSAPP_KEY",phone);
                    inten.putExtra("IMAGE_KEY",image);
//                    inten1.putExtra("UNIQUE_KEY",t[7]);
//                    inten1.putExtra("teacher1",t);
                    startActivity(inten);
                }
                else {
                    //Toast.makeText(BookEditVerify.this,"WROND PIN!!" , Toast.LENGTH_SHORT).show();
                    Toast.makeText(BookEditVerify.this,"WRONG PIN ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

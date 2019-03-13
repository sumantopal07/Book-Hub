package com.example.android.logindemo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.jar.Attributes;

public class DetailsActivity extends AppCompatActivity {

    TextView name,description,date,contact,price,tezid;
    ImageView teacherDetailImageView;
    ImageView whatsapp,googlePay;
    Random rand=new Random();
    String price1,tezid1;
    private static final int TEZ_REQUEST_CODE = 123;
    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private void initializeWidgets(){
        name= findViewById(R.id.nameDetailTextView);
        description= findViewById(R.id.descriptionDetailTextView);
        date= findViewById(R.id.dateDetailTextView);
        contact= findViewById(R.id.contactDetailTextView);
        tezid= findViewById(R.id.tezEditText);
        price= findViewById(R.id.priceDetailTextView);
        //pin=findViewById(R.id.pinEditText);

        //categoryDetailTextView= findViewById(R.id.categoryDetailTextView);
        teacherDetailImageView=findViewById(R.id.teacherDetailImageView);
        whatsapp=(ImageView) findViewById(R.id.whatsAppImageView);
        googlePay=(ImageView) findViewById(R.id.googlePayImageView);
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeWidgets();

        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        final String name1=i.getExtras().getString("NAME_KEY");
        price1=i.getExtras().getString("PRICE_KEY");
        final String contactdetails1=i.getExtras().getString("CONTACT_DETAILS");
        String description1=i.getExtras().getString("DESCRIPTION_KEY");
        String imageURL=i.getExtras().getString("IMAGE_KEY");
        tezid1=i.getExtras().getString("TEZ_KEY");
       // String pin1=i.getExtras().getString("PIN_KEY");


        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        name.setText("BOOK NAME: "+name1);
        price.setText("PRICE: "+price1);
        contact.setText("PHONE: "+contactdetails1);
        description.setText("DETAILS: "+description1);
        //tezid.setText("UPI: "+tezid1);

        date.setText("DATE: "+getDateToday());
        //categoryDetailTextView.setText("CATEGORY: "+getRandomCategory());
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "https://api.whatsapp.com/send?phone=+91"+contactdetails1+"&text=I'm%20interested%20in%20your%20book%20"+name1+"%20for%20sale"
                        )));
            }
        });
        googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", tezid1)
                                .appendQueryParameter("pn", "SELLER OF BOOK")
                                .appendQueryParameter("mc", Integer.toString(rand.nextInt(1000)))
                                .appendQueryParameter("tr", Integer.toString(rand.nextInt(1000)))
                                .appendQueryParameter("tn", "Im buying your "+name1+" book from BOOKHUB")
                                .appendQueryParameter("am", price1)
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "your-transaction-url")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                startActivityForResult(intent, TEZ_REQUEST_CODE);

            }
        });
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data.getStringExtra("Status"));
        }
    }

}

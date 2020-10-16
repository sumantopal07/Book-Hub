package com.example.android.logindemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.logindemo.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DetailsActivity extends AppCompatActivity {

    private final static int TEZ_REQUEST_CODE = 123;
    private final static String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    TextView name, description, date, contact, price, tezId;
    ImageView teacherDetailImageView;
    ImageView whatsApp, googlePay;
    Random rand = new Random();
    String itemPrice, itemTezId;

    private void initializeWidgets() {
        name = findViewById(R.id.nameDetailTextView);
        description = findViewById(R.id.descriptionDetailTextView);
        date = findViewById(R.id.dateDetailTextView);
        contact = findViewById(R.id.contactDetailTextView);
        tezId = findViewById(R.id.tezEditText);
        price = findViewById(R.id.priceDetailTextView);

        teacherDetailImageView = findViewById(R.id.teacherDetailImageView);
        whatsApp = (ImageView) findViewById(R.id.whatsAppImageView);
        googlePay = (ImageView) findViewById(R.id.googlePayImageView);
    }

    private String getDateToday() {
        Date date = new Date();
        String today = dateFormat.format(date);
        return today;
    }


    @Override
    /**
     * <li>
     *     RECEIVE DATA FROM ITEMS ACTIVITY VIA INTENT
     * </li>
     * <li>
     *     SET RECEIVED DATA TO TEXT VIEWS AND IMAGE VIEWS
     * </li>
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeWidgets();

        Intent intent = this.getIntent();
        final String itemName = intent.getExtras().getString("NAME_KEY");
        itemPrice = intent.getExtras().getString("PRICE_KEY");
        final String itemContactDetails = intent.getExtras().getString("CONTACT_DETAILS");
        String itemDescription = intent.getExtras().getString("DESCRIPTION_KEY");
        String imageURL = intent.getExtras().getString("IMAGE_KEY");
        itemTezId = intent.getExtras().getString("TEZ_KEY");


        name.setText("BOOK NAME: " + itemName);
        price.setText("PRICE: " + itemPrice);
        contact.setText("PHONE: " + itemContactDetails);
        description.setText("DETAILS: " + itemDescription);


        date.setText("DATE: " + getDateToday());
        whatsApp.setOnClickListener(getWhatsAppOnClickListener(itemName, itemContactDetails));
        googlePay.setOnClickListener(getGooglePlayOnClickListener(itemName));
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);
    }

    private View.OnClickListener getWhatsAppOnClickListener(final String itemName, final String itemContactDetails) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "https://api.whatsapp.com/send?phone=+91" + itemContactDetails +
                                        "&text=I'm%20interested%20in%20your%20book%20" +
                                        itemName + "%20for%20sale"
                        )));
            }
        };
    }

    private View.OnClickListener getGooglePlayOnClickListener(final String itemName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", itemTezId)
                                .appendQueryParameter("pn", "SELLER OF BOOK")
                                .appendQueryParameter("mc", Integer.toString(rand.nextInt(1000)))
                                .appendQueryParameter("tr", Integer.toString(rand.nextInt(1000)))
                                .appendQueryParameter("tn", "Im buying your " + itemName + " book from BOOKHUB")
                                .appendQueryParameter("am", itemPrice)
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "your-transaction-url")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                startActivityForResult(intent, TEZ_REQUEST_CODE);

            }
        };
    }

    /**
     * Process based on the data in response.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {
            Log.d("result", data.getStringExtra("Status"));
        }
    }

}

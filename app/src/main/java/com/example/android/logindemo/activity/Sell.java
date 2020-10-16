package com.example.android.logindemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.logindemo.R;
import com.example.android.logindemo.model.SellerDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Sell extends AppCompatActivity {

    private EditText name, price, description, contactDetails;
    private ImageView bookPic;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private Button seller;
    private StorageReference storageReference;
    private static int PICK_IMAGE = 123;
    private String bookName, bookPrice, bookDescription, bookContactDetails;
    Uri imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                bookPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupUIViews() {
        name = (EditText) findViewById(R.id.btnBookName);
        price = (EditText) findViewById(R.id.btnBookPrice);
        description = (EditText) findViewById(R.id.btnBookDescription);
        seller = (Button) findViewById(R.id.btnSell);
        contactDetails = (EditText) findViewById(R.id.btnBookContactDetails);
        bookPic = (ImageView) findViewById(R.id.btnBookPic);

        ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();


        bookPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
            }
        });
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                    sendUserData();
                    Toast.makeText(Sell.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(Sell.this, SecondActivity.class));
                } else {
                    Toast.makeText(Sell.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validate() {
        Boolean result = false;

        bookName = name.getText().toString();
        bookPrice = price.getText().toString();
        bookDescription = description.getText().toString();
        bookContactDetails = contactDetails.getText().toString();


        if (bookName.isEmpty() || bookPrice.isEmpty() || bookDescription.isEmpty() || bookContactDetails.isEmpty() || imagePath == null) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());//requesting userId
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sell.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(Sell.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });
        SellerDetails SellerDetails = new SellerDetails(bookName, bookDescription, bookPrice, bookContactDetails);
        myRef.setValue(SellerDetails);
    }
}

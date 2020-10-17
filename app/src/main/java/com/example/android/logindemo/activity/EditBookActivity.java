package com.example.android.logindemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.logindemo.R;
import com.example.android.logindemo.model.Teacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditBookActivity extends AppCompatActivity {

    private EditText newName, newDesc, newPin, newPrice, newUpi, newWhatsApp;
    private Button save;
    private FirebaseDatabase firebaseDatabase;
    private ImageView updateBookPic;
    private static int PICK_IMAGE = 123;
    private FirebaseAuth firebaseAuth;
    private ProgressBar uploadProgressBar;
    Uri imagePath;
    private StorageReference storageReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Intent intent = getIntent();
        final String pin = intent.getExtras().getString("PIN_KEY");
        final String upi = intent.getExtras().getString("UPI_KEY");
        final String name = intent.getExtras().getString("NAME_KEY");
        final String desc = intent.getExtras().getString("DESC_KEY");
        final String price = intent.getExtras().getString("PRICE_KEY");
        final String image = intent.getExtras().getString("IMAGE_KEY");
        final String phone = intent.getExtras().getString("WHATSAPP_KEY");
        final String unique = intent.getExtras().getString("UNIQUE_KEY");

        newName = findViewById(R.id.etBookUpdate);
        newDesc = findViewById(R.id.etDescUpdate);
        newPin = findViewById(R.id.etPinUpdate);
        newPrice = findViewById(R.id.etPriceUpdate);
        newUpi = findViewById(R.id.etUpiUpdate);
        newWhatsApp = findViewById(R.id.etNumberUpdate);
        save = findViewById(R.id.btnSave);
        uploadProgressBar = findViewById(R.id.progress_bar);
        updateBookPic = findViewById(R.id.ivBookUpdate);

        newName.setText(name);
        newDesc.setText(desc);
        newPin.setText(pin);
        newPrice.setText(price);
        newUpi.setText(upi);
        newWhatsApp.setText(phone);
        Picasso.with(EditBookActivity.this)
                .load(image)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(updateBookPic);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("book_uploads");

        final DatabaseReference databaseReference = firebaseDatabase.getReference("book_uploads").child(firebaseAuth.getUid());

        save.setOnClickListener(getSaveButtonOnClickListener(image, unique, databaseReference));
        updateBookPic.setOnClickListener(getUpdateBookPicOnClickListener());


    }

    private View.OnClickListener getUpdateBookPicOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        };
    }

    private View.OnClickListener getSaveButtonOnClickListener(final String image, final String unique, final DatabaseReference databaseReference) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditBookActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                String name1 = newName.getText().toString();
                String price1 = newPrice.getText().toString();
                String phone1 = newWhatsApp.getText().toString();
                String desc1 = newDesc.getText().toString();
                String google1 = newUpi.getText().toString();
                String pin1 = newPin.getText().toString();

                Teacher userProfile = new Teacher(name1, price1, phone1, image, desc1, google1, pin1, unique);

                databaseReference.setValue(userProfile);

                StorageReference imageReference = storageReference.child(firebaseAuth.getUid());
                UploadTask uploadTask = imageReference.putFile(imagePath);
                uploadTask.addOnSuccessListener(getUploadTaskOnSuccessListener())
                        .addOnFailureListener(getUploadTaskOnFailureListener())
                        .addOnProgressListener(getUploadTaskOnProgressListener());
                startActivity(new Intent(EditBookActivity.this, ItemsActivity.class));
            }
        };
    }

    private OnProgressListener<UploadTask.TaskSnapshot> getUploadTaskOnProgressListener() {
        return new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                uploadProgressBar.setProgress((int) progress);
            }
        };
    }

    private OnFailureListener getUploadTaskOnFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private OnSuccessListener<UploadTask.TaskSnapshot> getUploadTaskOnSuccessListener() {
        return new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadProgressBar.setVisibility(View.VISIBLE);
                        uploadProgressBar.setIndeterminate(false);
                        uploadProgressBar.setProgress(0);
                    }
                }, 500);
                startActivity(new Intent(EditBookActivity.this, ItemsActivity.class));
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                updateBookPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

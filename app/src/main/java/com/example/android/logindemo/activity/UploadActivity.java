package com.example.android.logindemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button chooseImageBtn;
    private Button uploadBtn;
    private FirebaseAuth firebaseAuth;
    private EditText nameEditText;
    private EditText descriptionEditText, pin;
    private EditText Price;
    private EditText ContactDetails;
    private EditText googlePay;
    private ImageView chosenImageView;
    private ProgressBar uploadProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        chooseImageBtn = findViewById(R.id.button_choose_image);
        uploadBtn = findViewById(R.id.uploadBtn);
        Price = findViewById(R.id.PriceEditText);
        ContactDetails = findViewById(R.id.contactEditText);
        nameEditText = findViewById(R.id.nameEditText);
        googlePay = findViewById(R.id.tezEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        chosenImageView = findViewById(R.id.chosenImageView);
        uploadProgressBar = findViewById(R.id.progress_bar);
        pin = findViewById(R.id.pinEditText);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("book_uploads");
        mStorageRef = FirebaseStorage.getInstance().getReference("book_uploads");

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(chosenImageView);
        }
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final String uploadId = firebaseAuth.getUid();
            StorageReference fileReference = mStorageRef.child(uploadId);
            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setIndeterminate(true);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(getUploadTaskOnSuccessListener(uploadId))
                    .addOnFailureListener(getUploadTaskOnFailureListener())
                    .addOnProgressListener(getUploadTaskOnProgressListener());
        } else {
            Toast.makeText(this, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private OnSuccessListener<UploadTask.TaskSnapshot> getUploadTaskOnSuccessListener(final String uploadId) {
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

                Toast.makeText(UploadActivity.this, "Book  Upload successful", Toast.LENGTH_LONG).show();
                Teacher upload = new Teacher(nameEditText.getText().toString().trim(), Price.getText().toString().trim(), ContactDetails.getText().toString().trim(),
                        taskSnapshot.getDownloadUrl().toString(),
                        descriptionEditText.getText().toString(),
                        googlePay.getText().toString().trim(),
                        pin.getText().toString().trim(), uploadId);

                mDatabaseRef.child(uploadId).setValue(upload);

                uploadProgressBar.setVisibility(View.INVISIBLE);
                openImagesActivity();

            }
        };
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

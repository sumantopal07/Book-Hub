package com.example.android.logindemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.logindemo.R;
import com.example.android.logindemo.model.Teacher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ItemsActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Teacher> mTeachers;
    private int poo;
    private FirebaseAuth firebaseAuth;

    private void openDetailActivity(String[] data) {


        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY", data[0]);
        intent.putExtra("PRICE_KEY", data[1]);
        intent.putExtra("CONTACT_DETAILS", data[2]);
        intent.putExtra("DESCRIPTION_KEY", data[3]);
        intent.putExtra("IMAGE_KEY", data[4]);
        intent.putExtra("TEZ_KEY", data[5]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Toast.makeText(ItemsActivity.this, "PRESS AND HOLD TO VIEW OPTIONS", Toast.LENGTH_LONG).show();

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapter(ItemsActivity.this, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ItemsActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("book_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTeachers.clear();
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Teacher upload = teacherSnapshot.getValue(Teacher.class);
                    upload.setKey(teacherSnapshot.getKey());
                    mTeachers.add(upload);
                }
                Collections.reverse(mTeachers);
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onItemClick(int position) {
        Teacher clickedTeacher = mTeachers.get(position);
        String[] teacherData = {clickedTeacher.getName(), clickedTeacher.getPrice(), clickedTeacher.getContactDetails(), clickedTeacher.getDescription(), clickedTeacher.getImageUrl(), clickedTeacher.getGooglePay()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        Teacher clickedTeacher = mTeachers.get(position);
        String[] teacherData = {clickedTeacher.getName(), clickedTeacher.getPrice(), clickedTeacher.getContactDetails(), clickedTeacher.getDescription(), clickedTeacher.getImageUrl(), clickedTeacher.getGooglePay()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onEditItemClick(int position) {
        Teacher clickedTeacher = mTeachers.get(position);

        Intent intent = new Intent(ItemsActivity.this, BookEditVerify.class);

        intent.putExtra("UNIQUE_KEY", clickedTeacher.getUniqueId());
        intent.putExtra("PIN_KEY", clickedTeacher.getPin());
        intent.putExtra("NAME_KEY", clickedTeacher.getName());
        intent.putExtra("DESC_KEY", clickedTeacher.getDescription());
        intent.putExtra("PRICE_KEY", clickedTeacher.getPrice());
        intent.putExtra("UPI_KEY", clickedTeacher.getGooglePay());
        intent.putExtra("WHATSAPP_KEY", clickedTeacher.getContactDetails());
        intent.putExtra("IMAGE_KEY", clickedTeacher.getImageUrl());
        startActivity(intent);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Teacher clickedTeacher = mTeachers.get(position);
        poo = position;
        Intent inten = new Intent(this, PinVerification.class);
        inten.putExtra("PIN_KEY", clickedTeacher.getPin());
        startActivityForResult(inten, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Teacher selectedItem = mTeachers.get(poo);
            final String selectedKey = selectedItem.getKey();

            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(ItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.SEARCH: {

                startActivity(new Intent(ItemsActivity.this, SearchActivity.class));
                break;
            }
            case R.id.EDIT: {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference("book_uploads").child(firebaseAuth.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(ItemsActivity.this, EditBookActivity.class);
                        Teacher userProfile = dataSnapshot.getValue(Teacher.class);
                        intent.putExtra("UNIQUE_KEY", userProfile.getUniqueId());
                        intent.putExtra("PIN_KEY", userProfile.getPin());
                        intent.putExtra("NAME_KEY", userProfile.getName());
                        intent.putExtra("DESC_KEY", userProfile.getDescription());
                        intent.putExtra("PRICE_KEY", userProfile.getPrice());
                        intent.putExtra("UPI_KEY", userProfile.getGooglePay());
                        intent.putExtra("WHATSAPP_KEY", userProfile.getContactDetails());
                        intent.putExtra("IMAGE_KEY", userProfile.getImageUrl());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ItemsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.DELETE: {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference("book_uploads").child(firebaseAuth.getUid());
                databaseReference.removeValue();
                Toast.makeText(ItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemsActivity.this, ItemsActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


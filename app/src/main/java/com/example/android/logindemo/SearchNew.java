package com.example.android.logindemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.List;

public class SearchNew extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Teacher> mTeachers;
    private int poo;
    private String x;
    private FirebaseAuth firebaseAuth;
    private void openDetailActivity(String[] data){


        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("PRICE_KEY",data[1]);
        intent.putExtra("CONTACT_DETAILS",data[2]);
        intent.putExtra("DESCRIPTION_KEY",data[3]);
        intent.putExtra("IMAGE_KEY",data[4]);
        intent.putExtra("TEZ_KEY",data[5]);
        startActivity(intent);
    }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);


        Intent intent=getIntent();
        final String best=intent.getExtras().getString("SEARCH_KEY");
        //temp=new String(unique);

        Toast.makeText(SearchNew.this, "PRESS AND HOLD TO VIEW OPTIONS", Toast.LENGTH_LONG).show();

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapter (SearchNew.this, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(SearchNew.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("book_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTeachers.clear();
                int i=0;
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Teacher upload = teacherSnapshot.getValue(Teacher.class);
                    if(upload.getName().toLowerCase().contains(best.toLowerCase()))
                    {
                        upload.setKey(teacherSnapshot.getKey());
                        mTeachers.add(i,upload);
                    }
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchNew.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void onItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getName(),clickedTeacher.getPrice(),clickedTeacher.getContactDetails(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl(),clickedTeacher.getGooglepay()};
//        x=clickedTeacher.getPin();
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        //
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getName(),clickedTeacher.getPrice(),clickedTeacher.getContactDetails(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl(),clickedTeacher.getGooglepay()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onEditItemClick(int position)
    {
        Teacher clickedTeacher=mTeachers.get(position);

        Intent inten = new Intent(SearchNew.this, BookEditVerify.class);

        //String[] teacherData={clickedTeacher.getName(),clickedTeacher.getPrice(),clickedTeacher.getContactDetails(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl(),clickedTeacher.getGooglepay(),clickedTeacher.getPin(),clickedTeacher.getUniqueId()};
        inten.putExtra("UNIQUE_KEY",clickedTeacher.getUniqueId());
        inten.putExtra("PIN_KEY",clickedTeacher.getPin());
        inten.putExtra("NAME_KEY",clickedTeacher.getName());
        inten.putExtra("DESC_KEY",clickedTeacher.getDescription());
        inten.putExtra("PRICE_KEY",clickedTeacher.getPrice());
        inten.putExtra("UPI_KEY",clickedTeacher.getGooglepay());
        inten.putExtra("WHATSAPP_KEY",clickedTeacher.getContactDetails());
        inten.putExtra("IMAGE_KEY",clickedTeacher.getImageUrl());


//        inten.putExtra("Teacher",teacherData);
        startActivity(inten);
    }
    @Override
    public void onDeleteItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        poo=position;
        Intent inten = new Intent(this, PinVerification.class);
        inten.putExtra("PIN_KEY",clickedTeacher.getPin());
        //startActivity(inten);
        //Intent intent = new Intent(ItemsActivity.this, PinVerification.class);
        startActivityForResult(inten,1);
        //onDestroy();;
//        intent.putExtra("number1", number1);
//        startActivity(new Intent(ItemsActivity.this, PinVerification.class));
//        //openDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            String z= data.getStringExtra("kyare");
            Teacher selectedItem = mTeachers.get(poo);
            final String selectedKey = selectedItem.getKey();

            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(SearchNew.this, "Item deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

//    private void Logout(){
//        firebaseAuth.signOut();
//        finish();
//        startActivity(new Intent(ItemsActivity.this, MainActivity.class));
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
//            case R.id.logoutMenu:{
//                Logout();
//                break;
//            }
            case R.id.SEARCH:
            {
                ///Toast.makeText(SearchNew.this, "TRY", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SearchNew.this, SearchActivity.class));
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.shun.twitterclone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Read extends Activity implements View.OnClickListener {

    private TextView textViewPostdata;
    private EditText editTextPostedtweets;
    private Button buttonUpdate;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        textViewPostdata = (TextView) findViewById(R.id.textViewPostdata);
        editTextPostedtweets = (EditText)findViewById(R.id.editTextPostedtweets);
        buttonUpdate.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                   // Toast.makeText(Read.this, "User signed in " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(Read.this, "Nobody is logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Read.this, Login.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tweet");
        //final String tweets =  editTextPostedtweets.getText().toString();

        myRef.child("tweets").orderByKey().limitToLast(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Tweets tweets = dataSnapshot.getValue(Tweets.class);
                String val = editTextPostedtweets.getText().toString();
                val = val + "\n \n Post: " + tweets.post;
                editTextPostedtweets.setText(val);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intentPost = new Intent(Read.this, Post.class);
        if (mAuth.getCurrentUser() != null) {
            if (item.getItemId() == R.id.menuLogout) {
                mAuth.signOut();
            } else if (item.getItemId() == R.id.menuRead) {
                Toast.makeText(this, "You are in Read Page already.", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.menuPost) {
                Toast.makeText(this, "Go to post tweets.", Toast.LENGTH_SHORT).show();
                startActivity(intentPost);
            } else {
                Toast.makeText(this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
        @Override
            public boolean onCreateOptionsMenu (Menu menu){
                getMenuInflater().inflate(R.menu.menu, menu);
                return super.onCreateOptionsMenu(menu);
            }

    }


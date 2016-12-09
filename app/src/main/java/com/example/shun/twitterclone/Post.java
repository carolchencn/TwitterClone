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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

    public class Post extends Activity implements View.OnClickListener {
    private Button buttonSubmit;
    private EditText editTextPost;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        editTextPost = (EditText) findViewById(R.id.editTextPost);
        buttonSubmit.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =  firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Toast.makeText(Post.this, "User signed in " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(Post.this, "Nobody is logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Post.this, Login.class);
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
        String post = editTextPost.getText().toString();
        Tweets tweets = new Tweets(post);
        Toast.makeText(this, "Your post has been submitted.", Toast.LENGTH_SHORT).show();
        editTextPost.setText("");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tweet");
        DatabaseReference dataNewTweets = myRef.push();
        dataNewTweets.setValue(tweets);
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Intent intentRead = new Intent(Post.this, Read.class);
            if (mAuth.getCurrentUser() != null) {
                if (item.getItemId() == R.id.menuLogout) {
                    mAuth.signOut();
                } else if (item.getItemId() == R.id.menuPost) {
                    Toast.makeText(this, "You are in Post Page already.", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.menuRead) {
                    Toast.makeText(this, "Go to read tweets.", Toast.LENGTH_SHORT).show();
                    startActivity(intentRead);
                } else {
                    Toast.makeText(this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
                }


            } return super.onOptionsItemSelected(item);
        }
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

    }



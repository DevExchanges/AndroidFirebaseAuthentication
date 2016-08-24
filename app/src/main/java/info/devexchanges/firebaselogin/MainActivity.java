package info.devexchanges.firebaselogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ImageView imageView;
    private TextView email;
    private TextView name;
    private View btnLogOut;
    private TextView userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView) findViewById(R.id.displayed_name);
        email = (TextView) findViewById(R.id.email_field);
        btnLogOut = findViewById(R.id.logout);
        userId = (TextView) findViewById(R.id.user_id);
        imageView = (ImageView) findViewById(R.id.user_photo);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        //add a auth listener
        authListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("MainActivity", "onAuthStateChanged");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setDataToView(user);

                    //loading image by Picasso
                    if (user.getPhotoUrl() != null) {
                        Log.d("MainActivity", "photoURL: " + user.getPhotoUrl());
                        Picasso.with(MainActivity.this).load(user.getPhotoUrl()).into(imageView);
                    }
                } else {
                    //user auth state is not existed or closed, return to Login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        //Signing out
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        email.setText("User Email: " + user.getEmail());
        name.setText("User name: " + user.getDisplayName());
        userId.setText("User id: " + user.getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}

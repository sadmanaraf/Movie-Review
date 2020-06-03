package com.example.moviereview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPass, etName;
    private Button loginBT, signUpBT;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etName = findViewById(R.id.etName);
        loginBT = findViewById(R.id.loginBT);
        signUpBT = findViewById(R.id.signUpBT);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    Intent intent = new Intent(MainActivity.this, Movies.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfLoginFieldIsFilled()){
                    final String email = etEmail.getText().toString();
                    final String password = etPass.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "enter your email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfRegFieldIsFilled()){
                    final String email = etEmail.getText().toString();
                    final String password = etPass.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String user_id = mAuth.getCurrentUser().getUid(); //get a random UID
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                final String uName = etName.getText().toString();

                                Map newPost = new HashMap();
                                newPost.put("userName", uName);


                                current_user_db.setValue(newPost);
                                Toast.makeText(MainActivity.this, "Sign up successful. You will be logged in automatically", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {

                    Toast.makeText(MainActivity.this, "fill up all of the above fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkIfLoginFieldIsFilled(){
        if (TextUtils.isEmpty(etEmail.getText().toString()) ||
                TextUtils.isEmpty(etPass.getText().toString()) ){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkIfRegFieldIsFilled(){
        if (TextUtils.isEmpty(etEmail.getText().toString()) ||
                TextUtils.isEmpty(etName.getText().toString()) ||
                TextUtils.isEmpty(etPass.getText().toString()) ){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}

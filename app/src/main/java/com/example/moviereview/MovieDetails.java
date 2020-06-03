package com.example.moviereview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MovieDetails extends AppCompatActivity {

    private TextView movieName, releaseDate, movieSummary, movieRating, userRev;
    private EditText etScore, etReview;
    private Button scoreBT, reviewBT;
    private String movieID="";

    private String mRating = "";
    private String scorerCount = "";
    private String userReviews = "";
    private String reviewer_name="";


    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        movieID = intent.getStringExtra("movieID");

        movieName = findViewById(R.id.movieName);
        releaseDate = findViewById(R.id.releaseDate);
        movieSummary = findViewById(R.id.movieSummary);
        movieRating = findViewById(R.id.movieRating);
        userRev = findViewById(R.id.userReviews);
        etScore = findViewById(R.id.etScore);
        etReview = findViewById(R.id.etReview);
        scoreBT = findViewById(R.id.scoreBT);
        reviewBT = findViewById(R.id.reviewBT);

        getMovieDetails();

        scoreBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentScore = Double.parseDouble(etScore.getText().toString());

                if (currentScore<1.0 || currentScore>10.0){
                    Toast.makeText(MovieDetails.this, "Please enter a value between 1 and 10", Toast.LENGTH_SHORT).show();
                }
                else {

                    Integer count = Integer.parseInt(scorerCount);
                    Double rating = Double.parseDouble(mRating);

                    count++;

                    rating = ((rating+currentScore)/2);

                    String finalRating = String.valueOf(rating);
                    String finalCount = String.valueOf(count);

                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child(movieID);
                    HashMap map = new HashMap();
                    map.put("rating", finalRating);
                    map.put("scorers", finalCount);
                    mDatabaseReference.updateChildren(map);

                    Toast.makeText(MovieDetails.this, "Your rating was recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reviewBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUserName();

                String currentReview = etReview.getText().toString();


                if (currentReview.isEmpty()){
                    Toast.makeText(MovieDetails.this, "Please enter a review", Toast.LENGTH_SHORT).show();
                }
                else{
                    userReviews = userReviews+"\n"+"\n"+reviewer_name+currentReview;
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child(movieID);
                    HashMap map = new HashMap();
                    map.put("reviews", userReviews);
                    mDatabaseReference.updateChildren(map);
                    Toast.makeText(MovieDetails.this, "Your review was uploaded", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void getMovieDetails(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child(movieID);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        movieName.setText(map.get("name").toString());
                    }
                    if(map.get("release date")!=null){
                        releaseDate.setText("Release Date: "+map.get("release date").toString());
                    }
                    if(map.get("summary")!=null){
                        movieSummary.setText("Summary: "+map.get("summary").toString());
                    }
                    if(map.get("rating")!=null){
                        movieRating.setText("Rating: "+map.get("rating").toString());
                        mRating = map.get("rating").toString();
                    }
                    if(map.get("scorers")!=null){
                        scorerCount = map.get("scorers").toString();
                    }
                    if (map.get("reviews")!=null){
                        userReviews = map.get("reviews").toString();
                        userRev.setText(map.get("reviews").toString());
                    }
                }
                else {
                    Toast.makeText(MovieDetails.this, "Not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserName(){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("userName")!=null){
                        reviewer_name = (map.get("userName").toString())+": ";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MovieDetails.this, Movies.class);
        startActivity(intent);
        finish();
    }
}
